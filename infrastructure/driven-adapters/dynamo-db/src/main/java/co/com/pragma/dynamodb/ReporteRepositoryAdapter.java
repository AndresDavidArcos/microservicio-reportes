package co.com.pragma.dynamodb;

import co.com.pragma.dynamodb.helper.TemplateAdapterOperations;
import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.Map;

@Repository
public class ReporteRepositoryAdapter extends TemplateAdapterOperations<Reporte, String, ReporteData> implements ReporteRepository {

    private static final String ID_REPORTE = "TOTAL_SOLICITUDES_APROBADAS";
    private final DynamoDbAsyncClient connectionFactory;
    private final String tableName;

    public ReporteRepositoryAdapter(DynamoDbEnhancedAsyncClient enhancedAsyncClient,
                                    DynamoDbAsyncClient asyncClient,
                                    ObjectMapper mapper,
                                    @Value("${adapter.dynamo-db.tableName}") String tableName) {
        super(enhancedAsyncClient, mapper, d -> mapper.map(d, Reporte.class), tableName);
        this.connectionFactory = asyncClient;
        this.tableName = tableName;
    }

    @Override
    public Mono<Reporte> obtenerReporte() {
        return super.getById(ID_REPORTE);
    }

    @Override
    public Mono<Void> incrementarContador() {
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(this.tableName)
                .key(Map.of("id", AttributeValue.builder().s(ID_REPORTE).build()))
                .updateExpression("ADD totalAprobadas :inc")
                .expressionAttributeValues(Map.of(":inc", AttributeValue.builder().n("1").build()))
                .build();

        return Mono.fromFuture(connectionFactory.updateItem(request)).then();
    }
}
