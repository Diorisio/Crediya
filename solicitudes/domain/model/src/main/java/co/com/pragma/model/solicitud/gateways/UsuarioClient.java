package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.Usuario;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UsuarioClient {

    Mono<Usuario> existeUsuario(BigInteger documentoIdentificacion);
}
