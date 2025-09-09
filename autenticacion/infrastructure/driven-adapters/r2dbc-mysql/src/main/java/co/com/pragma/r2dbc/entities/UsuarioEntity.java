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
    private BigInteger idusuario;

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

    @Column("documento_identidad")
    private BigInteger documentoIdentidad;

    @Column("contrasena")
    private String password;

    @Column("id_rol")
    private String idRol;

    private Integer intentos;
}
