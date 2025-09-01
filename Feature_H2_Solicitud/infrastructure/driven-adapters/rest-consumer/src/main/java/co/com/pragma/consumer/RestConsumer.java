package co.com.pragma.consumer;


import co.com.pragma.model.solicitud.gateways.UsuarioClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UsuarioClient {

    private final WebClient client;


    @CircuitBreaker(name = "usuarioClient")
    @Override
    public Mono<Boolean> existeUsuario(String usuarioId) {
        return client.get()
                .uri("/api/v1/usuarios/{id}", usuarioId)
                .retrieve()
                // Captura errores HTTP
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException(
                                        "Error al llamar al microservicio de usuario: " + errorBody))))
                .bodyToMono(Boolean.class)
                // Captura cualquier otra excepción de la cadena
                .onErrorResume(e -> {
                    // Aquí puedes registrar el error y devolver un valor por defecto
                    System.err.println("Fallo al validar usuario: " + e.getMessage());
                    return Mono.just(false); // Devuelve false si hay error
                });
    }


}
