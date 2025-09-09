package co.com.pragma.model.solicitud.gateways;

import co.com.pragma.model.solicitud.TipoPrestamo;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface TipoPrestamoRepository {

    Mono<TipoPrestamo> findByIdTipoPrestamo(Long idTipoPrestamo);
}
