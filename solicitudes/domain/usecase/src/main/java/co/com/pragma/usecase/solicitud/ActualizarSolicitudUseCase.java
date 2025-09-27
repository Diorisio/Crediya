package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.ColaMensajesGateway;
import co.com.pragma.model.solicitud.gateways.EstadoRepository;
import co.com.pragma.model.solicitud.gateways.SolicitudQueueGateway;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.usecase.solicitud.excepcions.EstadoFoundException;
import co.com.pragma.usecase.solicitud.excepcions.SolicitudNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@RequiredArgsConstructor
public class ActualizarSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final ColaMensajesGateway colaMensajesGateway;
    private final EstadoRepository estadoRepository;

    public Mono<Solicitud> actualizarEstado(Long id, BigInteger idEstado) {
        return solicitudRepository.findByIdSolicitud(id)
                .switchIfEmpty(Mono.error(new SolicitudNotFoundException(id)))
                .flatMap(solicitud ->
                        estadoRepository.findByIdEstado(idEstado)
                                .flatMap(estado -> {
                                    if (idEstado.equals(solicitud.getIdEstado())) {
                                        return Mono.error(new EstadoFoundException(estado.getNombre()));
                                    }
                                    solicitud.setIdEstado(idEstado);

                                    return solicitudRepository.save(solicitud, solicitud.getCorreoElectronico())
                                            .flatMap(saved -> {
                                                if (BigInteger.valueOf(2).equals(idEstado) ||
                                                        BigInteger.valueOf(3).equals(idEstado)) {

                                                    return colaMensajesGateway.enviarMensaje(solicitud)
                                                            .thenReturn(saved);
                                                }
                                                return Mono.just(saved);
                                            });
                                })
                );
    }
}
