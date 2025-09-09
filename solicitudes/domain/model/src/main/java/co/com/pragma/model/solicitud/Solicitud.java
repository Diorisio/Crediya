package co.com.pragma.model.solicitud;
import lombok.*;
//import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {

    private BigInteger identificacion;

    private Double monto;

    private Integer plazo ;

    private Long idTipoPrestamo;

    private String correoElectronico;

    private BigInteger idEstado;

}
