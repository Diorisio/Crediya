package co.com.pragma.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;

@Table(name = "estados")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EstadoEntity {

    @Id
    @Column("id_estado")
    private BigInteger idEstado;

    private String nombre;

    private String descripcion;
}
