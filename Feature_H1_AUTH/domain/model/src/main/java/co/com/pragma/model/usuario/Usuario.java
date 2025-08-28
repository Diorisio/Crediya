package co.com.pragma.model.usuario;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario {


    private String nombre;

    private String apellido;

    private String correoElectronico ;

    private LocalDate fechaNacimiento;

    private String direccion;

    private String telefono;

    private BigInteger salarioBase;


}
