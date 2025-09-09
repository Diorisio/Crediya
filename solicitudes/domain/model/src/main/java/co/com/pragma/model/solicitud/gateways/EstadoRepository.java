package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.Estado;
import reactor.core.publisher.Mono;

import java.math.BigInteger;


public interface EstadoRepository {

    Mono<Estado> findByIdEstado(BigInteger idEstado);
}
