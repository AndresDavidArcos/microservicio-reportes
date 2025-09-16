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

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class ActualizarReporteUseCaseTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ActualizarReporteUseCase actualizarReporteUseCase;

    @Test
    @DisplayName("Prueba de actualización exitosa del reporte")
    void actualizarReporteExitoso() {
        BigDecimal montoPrueba = new BigDecimal("5000000.00");
        when(reporteRepository.incrementarContadorYSumarMonto(any(BigDecimal.class))).thenReturn(Mono.empty());

        Mono<Void> resultado = actualizarReporteUseCase.ejecutar(montoPrueba);

        StepVerifier.create(resultado)
                .verifyComplete();

        verify(reporteRepository, times(1)).incrementarContadorYSumarMonto(montoPrueba);    }
}
