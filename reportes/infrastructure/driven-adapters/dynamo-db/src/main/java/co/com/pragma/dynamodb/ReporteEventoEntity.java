package co.com.pragma.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;


@DynamoDbBean
public class ReporteEventoEntity {

    private String id;

    private String totalAprobadas;

    private BigDecimal totalPrestado;

    public ReporteEventoEntity() { }

    public ReporteEventoEntity(String id, String totalAprobadas, BigDecimal totalPrestado) {
        this.id = id;
        this.totalAprobadas = totalAprobadas;
        this.totalPrestado = totalPrestado;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getSolicitudId() { return id; }

    public void setSolicitudId(String solicitudId) { this.id = solicitudId; }


    public String getTotalAprobadas() { return totalAprobadas; }

    public void setTotalAprobadas(String totalAprobadas) { this.totalAprobadas = totalAprobadas; }

    public BigDecimal getTotalPrestado() { return totalPrestado; }

    public void setTotalPrestado(BigDecimal totalPrestado) { this.totalPrestado = totalPrestado; }
}
