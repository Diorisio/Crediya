package co.com.pragma.model.jwt.gateways;

import reactor.core.publisher.Mono;

public interface TokenRepository {

    Mono<String> generarToken(String email, String rol);
    Mono<Boolean> validarToken(String token);
    String extractUserEmail(String token);

    String extractRole(String token);


}
