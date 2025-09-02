package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.pragma.model.solicitud.gateways.UsuarioClient;
import co.com.pragma.usecase.solicitud.excepcions.TipoPrestamoNotFoundException;
import co.com.pragma.usecase.solicitud.excepcions.UsuarioNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioClient usuarioClient;
    private final TipoPrestamoRepository tipoPrestamoRepository;

    public Mono<Solicitud> save(Solicitud solicitud)
        {
            return usuarioClient.existeUsuario(solicitud.getIdentificacion())
                    .flatMap(existe -> {
                        if (!existe) {
                            return Mono.error(new UsuarioNotFoundException(solicitud.getIdentificacion()));
                        }
                        return tipoPrestamoRepository.existsByIdTipoPrestamo(solicitud.getIdTipoPrestamo())
                                .flatMap(exists -> {
                                    if (!exists) {
                                        return Mono.error(new TipoPrestamoNotFoundException(solicitud.getIdTipoPrestamo()));
                                    }
                                    return solicitudRepository.save(solicitud);
                                });
                    });
        }



}
