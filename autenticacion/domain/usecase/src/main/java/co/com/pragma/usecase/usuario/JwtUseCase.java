package co.com.pragma.usecase.usuario;


import co.com.pragma.model.jwt.TokenCredenciales;
import co.com.pragma.model.jwt.gateways.PasswordEncoderRepository;
import co.com.pragma.model.jwt.gateways.TokenRepository;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.usuario.excepcions.CredencialesInvalidasException;
import co.com.pragma.usecase.usuario.excepcions.CuentaBloqueadaException;
import co.com.pragma.usecase.usuario.excepcions.EmailAlreadyRegisteredException;
import co.com.pragma.usecase.usuario.excepcions.UsuarioNoEncontradoEmailException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class JwtUseCase {

    private final TokenRepository tokenRepository;

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoderRepository passwordEncoderRepository;

    public Mono<String> login(TokenCredenciales tokenCredeciales) {
        return usuarioRepository.findByCorreoElectronico(tokenCredeciales.getEmail())
                .switchIfEmpty(Mono.error(new UsuarioNoEncontradoEmailException(tokenCredeciales.getEmail())))
                .flatMap(usuario -> {
                    if (usuario.getIntentos() >= 3) {
                        return Mono.error(new CuentaBloqueadaException(usuario.getCorreoElectronico()));
                    }

                    if (!passwordEncoderRepository.matches(tokenCredeciales.getPassword(), usuario.getPassword())) {

                        int nuevosIntentos = usuario.getIntentos()+1;
                        usuario.setIntentos(nuevosIntentos);
                        return usuarioRepository.saveUsuario(usuario)
                                .then(Mono.error(new CredencialesInvalidasException(nuevosIntentos)));
                   }


                    String rol = (usuario.getIdRol().equals("1")) ? "ADMIN" : "USER";
                    return tokenRepository.generarToken(usuario.getCorreoElectronico(), rol);
                });
    }


}
