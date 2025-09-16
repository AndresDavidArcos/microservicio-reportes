package co.com.pragma.sqs.listener.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SolicitudAprobadaDTO {
    private BigDecimal monto;
}
