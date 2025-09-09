package co.com.pragma.model.solicitud;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ListaSolicitudes
{
    private String nombre;

    private BigInteger identificacion;

    private Double monto;

    private Integer plazo ;

    private String tipoPrestamo;

    private String correoElectronico;

    private String estado;

    private Double deudaTotal;

    private BigInteger salarioBase;


    public ListaSolicitudes(String nombre, Double monto, Integer plazo, String correoElectronico,
                            String prestamo, BigInteger identificacion, String estado, Double deudaTotal, BigInteger salarioBase) {
            this.monto = monto;
            this.plazo = plazo;
            this.correoElectronico = correoElectronico;
            this.tipoPrestamo = prestamo; // si no lo tienes, o mapear según corresponda
            this.identificacion = identificacion; // si no lo tienes
            this.estado = estado;       // si no lo tienes
            this.deudaTotal = deudaTotal; // si quieres BigInteger
            this.nombre = nombre;
             this.salarioBase = salarioBase;

    }
}
