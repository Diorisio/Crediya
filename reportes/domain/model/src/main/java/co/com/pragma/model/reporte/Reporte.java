package co.com.pragma.model.reporte;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Reporte {
    private Long solicitudId;

    private String totalAprobadas;

    private BigDecimal totalPrestado;

}
