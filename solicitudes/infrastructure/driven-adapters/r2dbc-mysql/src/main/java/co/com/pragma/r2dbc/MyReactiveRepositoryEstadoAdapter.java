package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitud.Estado;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.TipoPrestamo;
import co.com.pragma.model.solicitud.gateways.EstadoRepository;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.r2dbc.entity.EstadoEntity;
import co.com.pragma.r2dbc.entity.SolicitudEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Repository
public class MyReactiveRepositoryEstadoAdapter extends ReactiveAdapterOperations<
        Estado,
        EstadoEntity,
        BigInteger,
    MyReactiveRepositoryEstado
> implements EstadoRepository {
    public MyReactiveRepositoryEstadoAdapter(MyReactiveRepositoryEstado repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Estado.class));
    }


    @Override
    public Mono<Estado> findByIdEstado(BigInteger idEstado) {
        return super.repository.findByIdEstado(idEstado);
    }
}
