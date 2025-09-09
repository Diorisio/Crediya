package co.com.pragma.usecase.usuario;

import co.com.pragma.model.jwt.gateways.PasswordEncoderRepository;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.usuario.excepcions.EmailAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoderRepository passwordEncoderRepository;

    public Mono<Usuario> saveUsuario(Usuario usuario) {

        String hashedPassword = passwordEncoderRepository.encode(usuario.getPassword());
        usuario.setPassword(hashedPassword);
        return usuarioRepository.existsByCorreoElectronico(usuario.getCorreoElectronico())
                .flatMap(exists -> exists
                        ? Mono.error(new EmailAlreadyRegisteredException(usuario.getCorreoElectronico()))
                        : usuarioRepository.saveUsuario(usuario)
                );
    }

    public Mono<Usuario> findByDocumentoIdentidad(String documento) {
        return usuarioRepository.findByDocumentoIdentidad(documento);
    }



}
