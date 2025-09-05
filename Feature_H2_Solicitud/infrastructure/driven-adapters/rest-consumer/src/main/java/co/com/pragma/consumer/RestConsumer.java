package co.com.pragma.consumer;


import co.com.pragma.model.solicitud.gateways.UsuarioClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;


import java.math.BigInteger;

@Log4j2
@Service
@RequiredArgsConstructor
public class RestConsumer implements UsuarioClient {

    private final WebClient client;


    @CircuitBreaker(name = "usuarioClient")
    @Override
    public Mono<Boolean> existeUsuario(BigInteger documento) {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getCredentials().toString())
                .flatMap(token ->
                        client.get()
                                .uri("/api/v1/usuarios/{documento}", documento)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .retrieve()
                                .bodyToMono(String.class)
                                .doOnNext(resp -> log.info("Respuesta del micro: {}", resp))
                                .map(resp -> resp.equalsIgnoreCase("Usuario existe"))
                                .onErrorResume(e -> {
                                    log.error("Fallo al validar usuario: {}", e.getMessage());
                                    return Mono.just(false);
                                })
                                .doOnSubscribe(sub -> log.debug("Iniciando flujo de consultar usuario"))
                                .doOnSuccess(resp -> log.debug("Respuesta correctamente"))
                );
    }


}
