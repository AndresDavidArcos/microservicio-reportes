package co.com.pragma.usecase.reporte;

import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.EmailGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class GenerarYEnviarReporteUseCaseTest {

    @Mock
    private ObtenerReporteUseCase obtenerReporteUseCase;

    @Mock
    private EmailGateway emailGateway;

    @InjectMocks
    private GenerarYEnviarReporteUseCase generarYEnviarReporteUseCase;

    @Captor
    private ArgumentCaptor<String> cuerpoCorreoCaptor;

    @Test
    @DisplayName("Prueba de generación y envío de reporte exitoso")
    void generarYEnviarReporteExitoso() {
        String destinatario = "admin@crediva.com";
        Reporte reporteDePrueba = Reporte.builder()
                .totalAprobadas(150L)
                .montoTotalAprobado(new BigDecimal("250000000.50"))
                .build();

        when(obtenerReporteUseCase.ejecutar()).thenReturn(Mono.just(reporteDePrueba));
        when(emailGateway.enviarReporte(anyString(), anyString(), anyString())).thenReturn(Mono.empty());

        Mono<Void> resultado = generarYEnviarReporteUseCase.ejecutar(destinatario);

        StepVerifier.create(resultado)
                .verifyComplete();

        verify(emailGateway, times(1)).enviarReporte(anyString(), anyString(), cuerpoCorreoCaptor.capture());

        String cuerpoEnviado = cuerpoCorreoCaptor.getValue();
        assertTrue(cuerpoEnviado.contains("Cantidad Total de Préstamos Aprobados: 150"));
        assertTrue(cuerpoEnviado.contains("Monto Total Prestado: $250,000,000.50"));
        assertTrue(cuerpoEnviado.contains(LocalDate.now().toString()));
    }

    @Test
    @DisplayName("Prueba de fallo cuando no se puede obtener el reporte")
    void generarYEnviarReporteFallo() {
        String destinatario = "admin@crediva.com";
        when(obtenerReporteUseCase.ejecutar()).thenReturn(Mono.error(new RuntimeException("Error en DynamoDB")));

        Mono<Void> resultado = generarYEnviarReporteUseCase.ejecutar(destinatario);

        StepVerifier.create(resultado)
                .expectError(RuntimeException.class)
                .verify();
    }
}
