package co.com.pragma.model.reporte;
import lombok.*;
//import lombok.NoArgsConstructor;

import lombok.Builder;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    private String id;
    private Long totalAprobadas;
}
