package co.com.pragma.model.solicitud;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Cuota {
    private int numero;
    private double cuota;
    private double interes;
    private double abonoCapital;
    private double saldoRestante;

    @Override
    public String toString() {
        return String.format("Cuota %d -> Total: %.2f | Interés: %.2f | Capital: %.2f | Saldo: %.2f",
                numero, cuota, interes, abonoCapital, saldoRestante);
    }
}
