package co.com.pragma.model.reporte;
import lombok.*;

import lombok.Builder;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    private String id;
    private Long totalAprobadas;
    private BigDecimal montoTotalAprobado;
}
