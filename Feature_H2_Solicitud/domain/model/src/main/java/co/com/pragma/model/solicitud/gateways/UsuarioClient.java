package co.com.pragma.model.solicitud.gateways;

import reactor.core.publisher.Mono;

public interface UsuarioClient {

    Mono<Boolean> existeUsuario(String documentoIdentificacion);
}
