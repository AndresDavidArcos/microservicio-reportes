package co.com.pragma.api;

import co.com.pragma.api.mapper.ReporteMapper;
import co.com.pragma.usecase.reporte.ObtenerReporteUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {
    private final ObtenerReporteUseCase obtenerReporteUseCase;
    private final ReporteMapper reporteMapper;

    public Mono<ServerResponse> obtenerReporte(ServerRequest serverRequest) {
        return obtenerReporteUseCase.ejecutar()
                .map(reporteMapper::toDTO)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto));
    }
}
