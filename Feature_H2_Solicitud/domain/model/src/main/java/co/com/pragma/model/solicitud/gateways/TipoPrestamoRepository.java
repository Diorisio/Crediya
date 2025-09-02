package co.com.pragma.model.solicitud.gateways;

import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface TipoPrestamoRepository {

    Mono<Boolean> existsByIdTipoPrestamo(BigInteger idTipoPrestamo);
}
