package co.com.pragma.usecase.reporte;

import co.com.pragma.model.reporte.gateways.ReporteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ActualizarReporteUseCase {
    private final ReporteRepository reporteRepository;
    public Mono<Void> ejecutar() {
        return reporteRepository.incrementarContador();
    }
}
