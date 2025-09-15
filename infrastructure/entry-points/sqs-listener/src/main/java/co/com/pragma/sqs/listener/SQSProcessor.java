package co.com.pragma.sqs.listener;

import co.com.pragma.usecase.reporte.ActualizarReporteUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private final ActualizarReporteUseCase actualizarReporteUseCase;

    @Override
    public Mono<Void> apply(Message message) {
        log.info("Mensaje de aprobación recibido: {}", message.messageId());
        return actualizarReporteUseCase.ejecutar()
                .doOnSuccess(v -> log.info("Contador de reportes actualizado exitosamente."))
                .doOnError(e -> log.error("Error al actualizar el contador de reportes.", e));
    }
}
