package co.com.pragma.model.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReporteEvento {

    private String id;

    private Solicitud solicitud;

    private String intereses;

    public String getId() {
        if (id == null) {
            if (solicitud != null && solicitud.getIdSolicitud() != null) {
                id = String.valueOf(solicitud.getIdSolicitud()); // evento individual
            } else {
                id = "GLOBAL"; // registro global
            }
        }
        return id;
    }


    public Long getSolicitudId() {
        return solicitud != null ? solicitud.getIdSolicitud() : null;
    }

    public String getDocumento() {
        return solicitud != null ? String.valueOf(solicitud.getIdentificacion()) : null;
    }

    public String getEstados() {
        return solicitud != null && solicitud.getIdEstado() == 2 ? "APROBADO" : "RECHAZADO";
    }

    public BigDecimal getMonto() {
        return solicitud != null ? BigDecimal.valueOf(solicitud.getMonto()) : null;
    }

    public LocalDateTime getFecha() {
        return LocalDateTime.now();
    }
}
