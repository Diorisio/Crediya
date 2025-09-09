package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitud.Estado;
import co.com.pragma.r2dbc.entity.EstadoEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepositoryEstado extends ReactiveCrudRepository<EstadoEntity, BigInteger>, ReactiveQueryByExampleExecutor<EstadoEntity> {

    Mono<Estado> findByIdEstado(BigInteger idEstado);
}
