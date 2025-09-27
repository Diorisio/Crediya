package co.com.pragma.model.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {

    private Long idSolicitud;
    private Long identificacion;
    private Double monto;
    private Integer plazo;
    private Integer idTipoPrestamo;
    private String correoElectronico;
    private Integer idEstado;
}
