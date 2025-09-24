package co.com.pragma.sesadapter;

import co.com.pragma.model.reporte.gateways.EmailGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.*;

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

        return Mono.fromFuture(sesClient.sendEmail(request)).then();
    }
}
