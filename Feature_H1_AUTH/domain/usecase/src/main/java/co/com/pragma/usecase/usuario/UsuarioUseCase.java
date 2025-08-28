package co.com.pragma.usecase.usuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.usuario.excepcions.EmailAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public Mono<Usuario> saveUsuario(Usuario usuario) {
        return usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())
                .flatMap(exists -> exists
                        ? Mono.error(new EmailAlreadyRegisteredException(usuario.getCorreoElectronico()))
                        : usuarioRepository.saveUsuario(usuario)
                );
    }
}
