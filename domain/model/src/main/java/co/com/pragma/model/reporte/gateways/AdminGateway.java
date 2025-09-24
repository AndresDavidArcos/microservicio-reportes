package co.com.pragma.model.reporte.gateways;

import reactor.core.publisher.Flux;

public interface AdminGateway {
    Flux<String> obtenerCorreosAdmin();
}
