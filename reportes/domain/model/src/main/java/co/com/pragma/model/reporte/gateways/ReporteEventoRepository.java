package co.com.pragma.model.reporte.gateways;

import co.com.pragma.model.reporte.ReporteEvento;
import reactor.core.publisher.Mono;

public interface ReporteEventoRepository {

    Mono<ReporteEvento> save(ReporteEvento reporteEvento);

}
