package co.com.pragma.r2dbc.entities;


import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;
import java.time.LocalDate;

@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioEntity {

    @Id
    @Column("idusuario")
    private Long idusuario;

    private String nombre;

    private String apellido;

    @Column("correoElectronico")
    private String correoElectronico ;

    @Column("fechaNacimiento")
    private LocalDate fechaNacimiento;

    private String direccion;

    private String telefono;


    @Column("salarioBase")
    private BigInteger salarioBase;
}
