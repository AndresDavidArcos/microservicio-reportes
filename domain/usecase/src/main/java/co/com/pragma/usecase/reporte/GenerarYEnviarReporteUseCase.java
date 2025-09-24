package co.com.pragma.usecase.reporte;


import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.AdminGateway;
import co.com.pragma.model.reporte.gateways.EmailGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
public class GenerarYEnviarReporteUseCase {

    private final ObtenerReporteUseCase obtenerReporteUseCase;
    private final EmailGateway emailGateway;
    private final AdminGateway adminGateway;

    public Mono<Void> ejecutar() {
        return Mono.zip(
                obtenerReporteUseCase.ejecutar(),
                adminGateway.obtenerCorreosAdmin().collectList()
        ).flatMap(tuple -> {
            Reporte reporte = tuple.getT1();
            List<String> destinatarios = tuple.getT2();

            if (destinatarios.isEmpty()) {
                return Mono.empty();
            }

            String fechaHoy = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            String asunto = "CrediYa - Reporte de Rendimiento Diario - " + fechaHoy;

            String cuerpo = String.format(
                    "Hola,\n\nEste es el resumen de rendimiento del negocio hasta la fecha %s:\n\n" +
                            " - Cantidad Total de Préstamos Aprobados: %d\n" +
                            " - Monto Total Prestado: $%,.2f\n\n" +
                            "Saludos,\nEl Equipo de CrediYa.",
                    fechaHoy,
                    reporte.getTotalAprobadas(),
                    reporte.getMontoTotalAprobado()
            );

            return Flux.fromIterable(destinatarios)
                    .flatMap(destinatario -> emailGateway.enviarReporte(destinatario, asunto, cuerpo))
                    .then();
        });
    }
}
