package co.com.pragma.api.dto;

import co.com.pragma.model.solicitud.ListaSolicitudes;

import java.math.BigDecimal;
import java.util.List;

public class ListaSolicitudesResponse {

    private List<ListaSolicitudes> solicitudes;
    private BigDecimal deudaTotal;

    public ListaSolicitudesResponse(List<ListaSolicitudes> solicitudes, BigDecimal deudaTotal) {
        this.solicitudes = solicitudes;
        this.deudaTotal = deudaTotal;
    }

    // Getters y setters
    public List<ListaSolicitudes> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(List<ListaSolicitudes> solicitudes) {
        this.solicitudes = solicitudes;
    }

    public BigDecimal getDeudaTotal() {
        return deudaTotal;
    }

    public void setDeudaTotal(BigDecimal deudaTotal) {
        this.deudaTotal = deudaTotal;
    }
}
