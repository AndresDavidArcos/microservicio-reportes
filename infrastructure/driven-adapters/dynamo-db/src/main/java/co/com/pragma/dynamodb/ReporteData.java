package co.com.pragma.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;

@DynamoDbBean
public class ReporteData {
    private String id;
    private Long totalAprobadas;
    private BigDecimal montoTotalAprobado;

    public BigDecimal getMontoTotalAprobado() {
        return montoTotalAprobado;
    }

    public void setMontoTotalAprobado(BigDecimal montoTotalAprobado) {
        this.montoTotalAprobado = montoTotalAprobado;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTotalAprobadas() {
        return totalAprobadas;
    }


    public void setTotalAprobadas(Long totalAprobadas) {
        this.totalAprobadas = totalAprobadas;
    }
}
