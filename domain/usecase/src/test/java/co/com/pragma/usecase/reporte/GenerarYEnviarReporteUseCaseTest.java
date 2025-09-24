package co.com.pragma.usecase.reporte;

import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.AdminGateway;
import co.com.pragma.model.reporte.gateways.EmailGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerarYEnviarReporteUseCaseTest {

    @Mock
    private ObtenerReporteUseCase obtenerReporteUseCase;
    @Mock
    private EmailGateway emailGateway;
    @Mock
    private AdminGateway adminGateway;

    @InjectMocks
    private GenerarYEnviarReporteUseCase generarYEnviarReporteUseCase;

    @Captor
    private ArgumentCaptor<String> cuerpoCorreoCaptor;
    @Captor
    private ArgumentCaptor<String> destinatarioCaptor;

    @Test
    @DisplayName("Prueba de generación y envío de reporte exitoso a múltiples admins")
    void generarYEnviarReporteExitoso() {
        Reporte reporteDePrueba = Reporte.builder()
                .totalAprobadas(150L)
                .montoTotalAprobado(new BigDecimal("250000000.50"))
                .build();
        List<String> correosAdmin = List.of("admin1@crediva.com", "admin2@crediva.com");

        when(obtenerReporteUseCase.ejecutar()).thenReturn(Mono.just(reporteDePrueba));
        when(adminGateway.obtenerCorreosAdmin()).thenReturn(Flux.fromIterable(correosAdmin));
        when(emailGateway.enviarReporte(anyString(), anyString(), anyString())).thenReturn(Mono.empty());

        Mono<Void> resultado = generarYEnviarReporteUseCase.ejecutar();

        StepVerifier.create(resultado)
                .verifyComplete();

        verify(emailGateway, times(2)).enviarReporte(destinatarioCaptor.capture(), anyString(), cuerpoCorreoCaptor.capture());

        assertTrue(destinatarioCaptor.getAllValues().containsAll(correosAdmin));
        String cuerpoEnviado = cuerpoCorreoCaptor.getValue();
        assertTrue(cuerpoEnviado.contains("Cantidad Total de Préstamos Aprobados: 150"));
        assertTrue(cuerpoEnviado.contains("Monto Total Prestado: $250,000,000.50"));
    }

    @Test
    @DisplayName("Prueba de ejecución exitosa cuando no hay administradores (no envía correo)")
    void generarYEnviarReporte_SinAdmins_NoEnviaCorreo() {
        Reporte reporteDePrueba = Reporte.builder().totalAprobadas(10L).montoTotalAprobado(BigDecimal.TEN).build();

        when(obtenerReporteUseCase.ejecutar()).thenReturn(Mono.just(reporteDePrueba));
        when(adminGateway.obtenerCorreosAdmin()).thenReturn(Flux.empty());

        Mono<Void> resultado = generarYEnviarReporteUseCase.ejecutar();

        StepVerifier.create(resultado)
                .verifyComplete();

        verify(emailGateway, never()).enviarReporte(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Prueba de fallo cuando no se puede obtener el reporte")
    void generarYEnviarReporteFallo() {
        when(obtenerReporteUseCase.ejecutar()).thenReturn(Mono.error(new RuntimeException("Error en DynamoDB")));
        when(adminGateway.obtenerCorreosAdmin()).thenReturn(Flux.empty());

        Mono<Void> resultado = generarYEnviarReporteUseCase.ejecutar();

        StepVerifier.create(resultado)
                .expectError(RuntimeException.class)
                .verify();
    }
}
