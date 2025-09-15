package co.com.pragma.usecase.reporte;

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
class ActualizarReporteUseCaseTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ActualizarReporteUseCase actualizarReporteUseCase;

    @Test
    @DisplayName("Prueba de actualización exitosa del contador")
    void actualizarContadorExitoso() {
        when(reporteRepository.incrementarContador()).thenReturn(Mono.empty());

        Mono<Void> resultado = actualizarReporteUseCase.ejecutar();

        StepVerifier.create(resultado)
                .verifyComplete();
    }
}
