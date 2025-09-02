package co.com.pragma.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;
import java.time.LocalDate;

@Table(name = "solicitud")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SolicitudEntity {

    @Id
    @Column("id_solicitud")
    private Long idSolicitud;

    private Double monto;

    private Integer plazo;

    @Column("email")
    private String correoElectronico ;

    @Column("id_estado")
    private BigInteger  idEstado;

    @Column("id_tipo_prestamo")
    private BigInteger  idTipoPrestamo;

    private String  identificacion;

}
