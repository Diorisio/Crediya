package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.RequestGuardarUsuarioDto;
import co.com.pragma.model.usuario.Usuario;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-27T20:53:12-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class UsuarioEntityMapperImpl implements UsuarioEntityMapper {

    @Override
    public Usuario toDTO(Usuario entity) {
        if ( entity == null ) {
            return null;
        }

        Usuario.UsuarioBuilder usuario = Usuario.builder();

        usuario.nombre( entity.getNombre() );
        usuario.apellido( entity.getApellido() );
        usuario.correoElectronico( entity.getCorreoElectronico() );
        usuario.fechaNacimiento( entity.getFechaNacimiento() );
        usuario.direccion( entity.getDireccion() );
        usuario.telefono( entity.getTelefono() );
        usuario.salarioBase( entity.getSalarioBase() );

        return usuario.build();
    }

    @Override
    public Usuario toDomain(RequestGuardarUsuarioDto usuario) {
        if ( usuario == null ) {
            return null;
        }

        Usuario.UsuarioBuilder usuario1 = Usuario.builder();

        usuario1.nombre( usuario.nombre() );
        usuario1.apellido( usuario.apellido() );
        usuario1.correoElectronico( usuario.correoElectronico() );
        if ( usuario.fechaNacimiento() != null ) {
            usuario1.fechaNacimiento( LocalDate.parse( usuario.fechaNacimiento() ) );
        }
        usuario1.direccion( usuario.direccion() );
        usuario1.telefono( usuario.telefono() );
        usuario1.salarioBase( usuario.salarioBase() );

        return usuario1.build();
    }
}
