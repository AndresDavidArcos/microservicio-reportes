package co.com.pragma.usecase.reporte;

import co.com.pragma.model.exception.NotFoundException;
import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ObtenerReporteUseCase {
    private final ReporteRepository reporteRepository;
    public Mono<Reporte> ejecutar() {
        return reporteRepository.obtenerReporte()
                .defaultIfEmpty(Reporte.builder()
                        .id("TOTAL_SOLICITUDES_APROBADAS")
                        .totalAprobadas(0L)
                        .build());
    }
}
