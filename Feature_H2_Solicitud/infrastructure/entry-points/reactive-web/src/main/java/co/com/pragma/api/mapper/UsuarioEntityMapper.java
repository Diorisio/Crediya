package co.com.pragma.api.mapper;


import co.com.pragma.api.dto.RequestGuardarUsuarioDto;
import co.com.pragma.model.usuario.Usuario;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "Spring")
public interface UsuarioEntityMapper {


    Usuario toDTO(Usuario entity) ;

    Usuario toDomain(RequestGuardarUsuarioDto usuario);
}
