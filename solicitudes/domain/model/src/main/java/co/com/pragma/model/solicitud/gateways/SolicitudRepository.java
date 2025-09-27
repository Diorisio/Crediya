package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface SolicitudRepository {

    Mono<Solicitud> save(Solicitud solicitud,String emailToken);

    Flux<Solicitud> findbyIdEstado(String estado);

    Mono<Solicitud> findByIdSolicitud(Long idSolicitud);

    Flux<Solicitud> findByIdentificacion(BigInteger identificacion);

}
