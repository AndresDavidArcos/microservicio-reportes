package co.com.pragma.sesadapter;

import co.com.pragma.model.reporte.gateways.EmailGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SESAdapter implements EmailGateway {

    private final SesAsyncClient sesClient;

    @Value("${adapter.ses.remitente-reporte}")
    private String remitente;

    @Override
    public Mono<Void> enviarReporte(String destinatario, String asunto, String cuerpo) {
        Destination destination = Destination.builder().toAddresses(destinatario).build();
        Content subjectContent = Content.builder().data(asunto).build();
        Content bodyContent = Content.builder().data(cuerpo).build();
        Body body = Body.builder().text(bodyContent).build();
        Message message = Message.builder().subject(subjectContent).body(body).build();

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(destination)
                .message(message)
                .source(remitente)
                .build();

        return Mono.fromFuture(sesClient.sendEmail(request))
                .doOnSuccess(response -> log.info("Reporte enviado exitosamente a {}", destinatario))
                .onErrorResume(MessageRejectedException.class, e -> {
                    log.warn("No se pudo enviar el reporte a '{}' porque la dirección no está verificada o es inválida. Causa: {}", destinatario, e.getMessage());
                    return Mono.empty();
                })
                .then();    }
}
