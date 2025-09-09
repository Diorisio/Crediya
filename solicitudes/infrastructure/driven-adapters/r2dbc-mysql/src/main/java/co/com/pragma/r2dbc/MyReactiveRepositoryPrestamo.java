package co.com.pragma.r2dbc;


import co.com.pragma.model.solicitud.TipoPrestamo;
import co.com.pragma.r2dbc.entity.TipoPrestamoEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigInteger;


public interface MyReactiveRepositoryPrestamo extends ReactiveCrudRepository<TipoPrestamoEntity, Long>, ReactiveQueryByExampleExecutor<TipoPrestamoEntity> {

    Mono<TipoPrestamo> findByIdTipoPrestamo(Long idTipoPrestamo);
}
