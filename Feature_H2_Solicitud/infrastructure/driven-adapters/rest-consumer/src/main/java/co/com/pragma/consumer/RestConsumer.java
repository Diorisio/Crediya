package co.com.pragma.consumer;


import co.com.pragma.model.solicitud.gateways.UsuarioClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Log4j2
@Service
@RequiredArgsConstructor
public class RestConsumer implements UsuarioClient {

    private final WebClient client;


    @CircuitBreaker(name = "usuarioClient")
    @Override
    public Mono<Boolean> existeUsuario(BigInteger documento) {
        return client.get()
                .uri("/api/v1/usuarios/{documento}", documento)
                .retrieve()
                .bodyToMono(String.class) // <-- ahora como String
                .doOnNext(resp -> log.info("Respuesta del micro: {}", resp))
                .map(resp -> resp.equalsIgnoreCase("Usuario existe")) // convierte a boolean
                .onErrorResume(e -> {
                    log.error("Fallo al validar usuario: {}", e.getMessage());
                    return Mono.just(false);
                })
                .doOnSubscribe(sub -> log.debug("Iniciando flujo de consultar usuario"))
                .doOnSuccess(resp -> log.debug("Respuesta correctamente"));
    }


}
