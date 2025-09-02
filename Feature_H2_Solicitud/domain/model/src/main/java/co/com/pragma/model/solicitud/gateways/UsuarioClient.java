package co.com.pragma.model.solicitud.gateways;

import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UsuarioClient {

    Mono<Boolean> existeUsuario(BigInteger documentoIdentificacion);
}
