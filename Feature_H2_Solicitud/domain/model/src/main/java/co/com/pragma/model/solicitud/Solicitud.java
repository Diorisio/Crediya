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

    private String documentoIdentificacion;

    private String monto;

    private String plazo ;

    private String tipoPrestamo;

    private String correoElectronico;

}
