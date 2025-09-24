package co.com.pragma.model.reporte.gateways;

import reactor.core.publisher.Mono;

public interface EmailGateway {
    Mono<Void> enviarReporte(String destinatario, String asunto, String cuerpo);
}
