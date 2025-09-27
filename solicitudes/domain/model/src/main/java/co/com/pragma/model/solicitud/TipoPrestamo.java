package co.com.pragma.model.solicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class TipoPrestamo {
    private Long idTipoPrestamo;

    private Double montoMaximo;

    private Double montoMinimo;

    private Integer tasaInteres ;

    private String nombre;

    private Boolean validacionAutomatica;
}
