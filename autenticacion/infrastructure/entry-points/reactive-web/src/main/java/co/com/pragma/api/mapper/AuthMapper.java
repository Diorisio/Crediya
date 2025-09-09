package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.AuthRequestDto;
import co.com.pragma.api.dto.AuthResponseDto;
import co.com.pragma.model.jwt.TokenCredenciales;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    TokenCredenciales toDomain(AuthRequestDto tokenCredenciales);

    default AuthResponseDto toResponse(String token) {
        return new AuthResponseDto(token);
    }


}
