package co.com.pragma.usecase.reporte;


import co.com.pragma.model.reporte.gateways.EmailGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class GenerarYEnviarReporteUseCase {

    private final ObtenerReporteUseCase obtenerReporteUseCase;
    private final EmailGateway emailGateway;

    public Mono<Void> ejecutar(String destinatario) {
        return obtenerReporteUseCase.ejecutar()
                .flatMap(reporte -> {
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

                    return emailGateway.enviarReporte(destinatario, asunto, cuerpo);
                });
    }
}
