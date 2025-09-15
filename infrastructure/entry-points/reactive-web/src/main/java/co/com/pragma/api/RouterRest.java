package co.com.pragma.api;

import co.com.pragma.api.dto.ReporteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/reportes",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "obtenerReporte",
                    operation = @Operation(
                            summary = "Obtener el reporte de solicitudes aprobadas",
                            description = "Devuelve el número total de solicitudes de crédito que han sido aprobadas en el sistema. Requiere rol de ADMIN.",
                            operationId = "obtenerReporte",
                            tags = {"Reportes"},
                            security = @SecurityRequirement(name = "bearerAuth"),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Reporte obtenido exitosamente",
                                            content = @Content(schema = @Schema(implementation = ReporteDTO.class))
                                    ),
                                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                                    @ApiResponse(responseCode = "403", description = "Acceso denegado (rol no es ADMIN)"),
                                    @ApiResponse(responseCode = "404", description = "El reporte no fue encontrado (si aún no se ha generado)")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/v1/reportes"), handler::obtenerReporte);
    }
}
