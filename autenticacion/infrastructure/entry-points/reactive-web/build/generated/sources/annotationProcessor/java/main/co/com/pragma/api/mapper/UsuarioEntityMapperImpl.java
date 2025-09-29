package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.RequestGuardarUsuarioDto;
import co.com.pragma.model.usuario.Usuario;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T19:37:01-0500",
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

        usuario.idusuario( entity.getIdusuario() );
        usuario.nombre( entity.getNombre() );
        usuario.apellido( entity.getApellido() );
        usuario.correoElectronico( entity.getCorreoElectronico() );
        usuario.fechaNacimiento( entity.getFechaNacimiento() );
        usuario.direccion( entity.getDireccion() );
        usuario.telefono( entity.getTelefono() );
        usuario.salarioBase( entity.getSalarioBase() );
        usuario.documentoIdentidad( entity.getDocumentoIdentidad() );
        usuario.password( entity.getPassword() );
        usuario.idRol( entity.getIdRol() );
        usuario.intentos( entity.getIntentos() );

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
        usuario1.documentoIdentidad( usuario.documentoIdentidad() );
        usuario1.password( usuario.password() );

        return usuario1.build();
    }
}
