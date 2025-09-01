package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.solicitud.gateways.UsuarioClient;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioClient usuarioClient;

    public Mono<Solicitud> save(Solicitud solicitud)
        {
            return usuarioClient.existeUsuario(solicitud.getCorreoElectronico())
                    .flatMap(existe -> {
                        if (existe) {
                            return Mono.error(new RuntimeException("Correo ya registrado"));
                        } else {
                            return solicitudRepository.save(solicitud);
                        }
                    });
        }



}
