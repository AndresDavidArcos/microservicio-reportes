package co.com.pragma.sqs.listener;

import co.com.pragma.sqs.listener.dto.SolicitudAprobadaDTO;
import co.com.pragma.usecase.reporte.ActualizarReporteUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.math.BigDecimal;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private final ActualizarReporteUseCase actualizarReporteUseCase;
    private final ObjectMapper mapper;

    @Override
    public Mono<Void> apply(Message message) {
        try {
            log.info("Mensaje de aprobación recibido: {}", message.messageId());

            SolicitudAprobadaDTO solicitud = mapper.readValue(message.body(), SolicitudAprobadaDTO.class);
            BigDecimal monto = solicitud.getMonto();

            if (monto == null) {
                log.warn("El mensaje {} no contiene un monto. Se omitirá.", message.messageId());
                return Mono.empty();
            }

            return actualizarReporteUseCase.ejecutar(monto)
                    .doOnSuccess(v -> log.info("Reportes actualizados exitosamente para el mensaje {}.", message.messageId()))
                    .doOnError(e -> log.error("Error al actualizar los reportes.", e));

        } catch (Exception e) {
            log.error("Error procesando mensaje de aprobación: {}", e.getMessage());
            return Mono.empty();
        }
    }
}
