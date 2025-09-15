package co.com.pragma.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ReporteDTO {
    private Long totalAprobadas;
}
