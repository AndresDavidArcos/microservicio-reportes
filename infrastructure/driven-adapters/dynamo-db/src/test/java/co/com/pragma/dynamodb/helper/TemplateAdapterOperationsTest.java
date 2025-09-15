//package co.com.pragma.dynamodb.helper;
//
//import co.com.pragma.dynamodb.ReporteRepositoryAdapter;
//import co.com.pragma.dynamodb.ReporteData;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.reactivecommons.utils.ObjectMapper;
//import reactor.test.StepVerifier;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
//import software.amazon.awssdk.enhanced.dynamodb.Key;
//import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
//
//import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
//import java.util.concurrent.CompletableFuture;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//class TemplateAdapterOperationsTest {
//
//    @Mock
//    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
//
//    @Mock
//    private ObjectMapper mapper;
//
//    @Mock
//    private DynamoDbAsyncTable<ReporteData> customerTable;
//
//    private ReporteData reporteData;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        when(dynamoDbEnhancedAsyncClient.table("table_name", TableSchema.fromBean(ReporteData.class)))
//                .thenReturn(customerTable);
//
//        reporteData = new ReporteData();
//        reporteData.setId("id");
//        reporteData.setAtr1("atr1");
//    }
//
//    @Test
//    void modelEntityPropertiesMustNotBeNull() {
//        ReporteData reporteDataUnderTest = new ReporteData("id", "atr1");
//
//        assertNotNull(reporteDataUnderTest.getId());
//        assertNotNull(reporteDataUnderTest.getAtr1());
//    }
//
//    @Test
//    void testSave() {
//        when(customerTable.putItem(reporteData)).thenReturn(CompletableFuture.runAsync(()->{}));
//        when(mapper.map(reporteData, ReporteData.class)).thenReturn(reporteData);
//
//        ReporteRepositoryAdapter reporteRepositoryAdapter =
//                new ReporteRepositoryAdapter(dynamoDbEnhancedAsyncClient, mapper);
//
//        StepVerifier.create(reporteRepositoryAdapter.save(reporteData))
//                .expectNextCount(1)
//                .verifyComplete();
//    }
//
//    @Test
//    void testGetById() {
//        String id = "id";
//
//        when(customerTable.getItem(
//                Key.builder().partitionValue(AttributeValue.builder().s(id).build()).build()))
//                .thenReturn(CompletableFuture.completedFuture(reporteData));
//        when(mapper.map(reporteData, Object.class)).thenReturn("value");
//
//        ReporteRepositoryAdapter reporteRepositoryAdapter =
//                new ReporteRepositoryAdapter(dynamoDbEnhancedAsyncClient, mapper);
//
//        StepVerifier.create(reporteRepositoryAdapter.getById("id"))
//                .expectNext("value")
//                .verifyComplete();
//    }
//
//    @Test
//    void testDelete() {
//        when(mapper.map(reporteData, ReporteData.class)).thenReturn(reporteData);
//        when(mapper.map(reporteData, Object.class)).thenReturn("value");
//
//        when(customerTable.deleteItem(reporteData))
//                .thenReturn(CompletableFuture.completedFuture(reporteData));
//
//        ReporteRepositoryAdapter reporteRepositoryAdapter =
//                new ReporteRepositoryAdapter(dynamoDbEnhancedAsyncClient, mapper);
//
//        StepVerifier.create(reporteRepositoryAdapter.delete(reporteData))
//                .expectNext("value")
//                .verifyComplete();
//    }
//}
