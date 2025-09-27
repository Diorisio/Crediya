package co.com.pragma.model.solicitud;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlanPago {
    private List<Cuota> cuotas;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Plan de Pagos:\n");
        cuotas.forEach(c -> sb.append(c.toString()).append("\n"));
        return sb.toString();
    }
}
