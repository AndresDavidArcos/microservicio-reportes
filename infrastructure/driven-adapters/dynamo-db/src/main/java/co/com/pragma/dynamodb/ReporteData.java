package co.com.pragma.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class ReporteData {
    private String id;
    private Long totalAprobadas;

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
