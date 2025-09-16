package co.com.pragma.api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class ReporteDTO {
    private Long totalAprobadas;
    private BigDecimal montoTotalAprobado;
}
