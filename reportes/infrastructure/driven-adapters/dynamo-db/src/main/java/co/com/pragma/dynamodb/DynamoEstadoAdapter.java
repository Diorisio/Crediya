package co.com.pragma.dynamodb;


import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.math.BigDecimal;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DynamoEstadoAdapter implements ReporteRepository {

    private static final String TABLE_NAME = "reportes";
    private static final String STATS_ID = "global";

    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    @Override
    public Mono<Reporte> obtenerReporteGlobal() {
        var getRequest = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.fromS(STATS_ID)))
                .build();

        return Mono.fromFuture(() -> dynamoDbAsyncClient.getItem(getRequest))
                .map(response -> {
                    if (!response.hasItem()) {
                        return Reporte.builder()
                                .totalAprobadas("0")
                                .totalPrestado(BigDecimal.ZERO)
                                .build();
                    }

                    Map<String, AttributeValue> item = response.item();
                    return Reporte.builder()
                            .totalAprobadas(item.get("totalAprobadas").n())
                            .totalPrestado(new BigDecimal(item.get("totalPrestado").n()))
                            .build();
                });
    }

    @Override
    public Mono<Void> incrementarAprobadas(BigDecimal monto) {
        var updateRequest = UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.fromS(STATS_ID)))
                .updateExpression("SET totalAprobadas = if_not_exists(totalAprobadas, :zero) + :inc, " +
                        "totalPrestado = if_not_exists(totalPrestado, :zeroMonto) + :amount")
                .expressionAttributeValues(Map.of(
                        ":inc", AttributeValue.fromN("1"),
                        ":amount", AttributeValue.fromN(monto.toPlainString()),
                        ":zero", AttributeValue.fromN("0"),
                        ":zeroMonto", AttributeValue.fromN("0")
                ))
                .build();

        return Mono.fromFuture(() -> dynamoDbAsyncClient.updateItem(updateRequest)).then();
    }
}
