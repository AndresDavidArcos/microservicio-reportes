package co.com.pragma.usecase.reporte;

import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerReporteUseCaseTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ObtenerReporteUseCase obtenerReporteUseCase;

    @Test
    @DisplayName("Prueba de obtención exitosa de un reporte existente")
    void obtenerReporteExitoso() {
        Reporte reporteExistente = Reporte.builder()
                .id("TOTAL_SOLICITUDES_APROBADAS")
                .totalAprobadas(10L)
                .build();
        when(reporteRepository.obtenerReporte()).thenReturn(Mono.just(reporteExistente));

        Mono<Reporte> resultado = obtenerReporteUseCase.ejecutar();

        StepVerifier.create(resultado)
                .expectNextMatches(reporte -> reporte.getTotalAprobadas() == 10L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Prueba de obtención de reporte cuando no existe (debe devolver 0)")
    void obtenerReporteNoExistente() {
        when(reporteRepository.obtenerReporte()).thenReturn(Mono.empty());

        Mono<Reporte> resultado = obtenerReporteUseCase.ejecutar();

        StepVerifier.create(resultado)
                .expectNextMatches(reporte -> reporte.getTotalAprobadas() == 0L &&
                        reporte.getId().equals("TOTAL_SOLICITUDES_APROBADAS"))
                .verifyComplete();
    }
}
