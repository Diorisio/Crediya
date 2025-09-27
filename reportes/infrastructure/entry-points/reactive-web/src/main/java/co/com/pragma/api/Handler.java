package co.com.pragma.api;

import co.com.pragma.usecase.reporte.ReporteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ReporteUseCase useCase;

    public Mono<ServerResponse> listenGETReporte(ServerRequest serverRequest) {
        return useCase.estado()
                .flatMap(reporte -> ServerResponse.ok().bodyValue(reporte))
                .onErrorResume(e -> {
                    System.err.println("Error en listenGETReporte: " + e.getMessage());
                    return ServerResponse.status(500)
                            .bodyValue(Map.of("mensaje", "Error obteniendo el reporte"));
                });
    }
}
