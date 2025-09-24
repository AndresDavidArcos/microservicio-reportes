package co.com.pragma.scheduler;

import co.com.pragma.usecase.reporte.GenerarYEnviarReporteUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReporteProgramadoTask {
    private final GenerarYEnviarReporteUseCase generarYEnviarReporteUseCase;

    @Scheduled(cron = "0 0 17 * * ?", zone = "America/Bogota")
    public void generarReporteDiario() {
        log.info("Iniciando tarea programada: Generación de reporte diario...");

        generarYEnviarReporteUseCase.ejecutar()
                .doOnSuccess(v -> log.info("Tarea de reporte diario completada exitosamente."))
                .doOnError(e -> log.error("Error durante la ejecución de la tarea de reporte diario.", e))
                .subscribe();
    }
}
