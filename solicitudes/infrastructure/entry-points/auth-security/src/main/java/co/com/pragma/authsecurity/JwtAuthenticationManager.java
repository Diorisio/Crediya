package co.com.pragma.authsecurity;

import co.com.pragma.model.solicitud.gateways.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenRepository tokenRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        return tokenRepository.validarToken(token)
                .filter(Boolean::booleanValue)
                .map(valid -> {
                    String username = tokenRepository.extractUserEmail(token);
                    String role = tokenRepository.extractRole(token); // ROLE_ADMIN o ROLE_USER

                    return new UsernamePasswordAuthenticationToken(
                            username,
                            token,
                            List.of(new SimpleGrantedAuthority(role))
                    );
                });
    }
}

