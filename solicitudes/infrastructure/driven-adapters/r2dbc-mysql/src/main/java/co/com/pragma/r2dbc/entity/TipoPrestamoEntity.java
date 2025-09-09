package co.com.pragma.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "tipo_prestamo")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TipoPrestamoEntity {

    @Id
    @Column("id_tipo_prestamo")
    private Long idTipoPrestamo;
    @Column("monto_maximo")
    private Double montoMaximo;
    @Column("monto_minimo")
    private Double montoMinimo;
    @Column("tasa_interes")
    private Integer tasaInteres ;

    private String nombre;
    @Column("validacion_automatica")
    private Boolean  validacionAutomatica;
}
