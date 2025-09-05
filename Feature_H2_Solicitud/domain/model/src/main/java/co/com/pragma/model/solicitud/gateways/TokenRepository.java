package co.com.pragma.model.solicitud.gateways;

import reactor.core.publisher.Mono;

public interface TokenRepository {

    Mono<Boolean> validarToken(String token);
    String extractUserEmail(String token);

    String extractRole(String token);


}
