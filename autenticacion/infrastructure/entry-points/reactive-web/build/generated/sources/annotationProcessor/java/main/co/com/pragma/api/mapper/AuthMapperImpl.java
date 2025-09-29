package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.AuthRequestDto;
import co.com.pragma.model.jwt.TokenCredenciales;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T19:37:02-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class AuthMapperImpl implements AuthMapper {

    @Override
    public TokenCredenciales toDomain(AuthRequestDto tokenCredenciales) {
        if ( tokenCredenciales == null ) {
            return null;
        }

        TokenCredenciales.TokenCredencialesBuilder tokenCredenciales1 = TokenCredenciales.builder();

        tokenCredenciales1.email( tokenCredenciales.email() );
        tokenCredenciales1.password( tokenCredenciales.password() );

        return tokenCredenciales1.build();
    }
}
