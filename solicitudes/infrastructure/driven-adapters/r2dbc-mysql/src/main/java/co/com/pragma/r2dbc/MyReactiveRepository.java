package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.r2dbc.entity.SolicitudEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Long>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

    Flux<Solicitud> findByIdEstado(String idTipoPrestamo);


    Mono<Solicitud> findByIdSolicitud(Long idSolicitud);

    Flux<Solicitud> findByIdentificacion(BigInteger identificacion);
}
