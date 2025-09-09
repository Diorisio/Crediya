package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UsuarioRepository {

    Mono<Usuario> saveUsuario(Usuario usuario);

    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);

    Mono<Usuario> findByDocumentoIdentidad(String documentoIdentidad);

    Mono<Usuario> findByCorreoElectronico(String correoElectronico);
}
