package co.com.pragma.model.usuario.gateways;

import co.com.pragma.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {

    Mono<Usuario> saveUsuario(Usuario usuario);

    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
}
