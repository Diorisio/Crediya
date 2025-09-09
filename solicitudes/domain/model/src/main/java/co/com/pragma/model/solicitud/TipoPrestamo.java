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

    private Double monto_maximo;

    private Double monto_minimo;

    private Integer tasa_interes ;

    private String nombre;

    private Boolean validacion_automatica;
}
