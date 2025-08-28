package co.com.pragma.api;

import co.com.pragma.api.dto.RequestGuardarUsuarioDto;
import co.com.pragma.api.mapper.UsuarioEntityMapper;
import co.com.pragma.usecase.usuario.UsuarioUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class Handler {

    private final UsuarioUseCase useCase;
    private final UsuarioEntityMapper mapper;

    private final Validator validator;


    public Mono<ServerResponse> listenPOSTGuardarCase(ServerRequest serverRequest) {

        return serverRequest
                .bodyToMono(RequestGuardarUsuarioDto.class)
                .flatMap(dto -> {
                    Set<ConstraintViolation<RequestGuardarUsuarioDto>> violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        return Mono.error(new ConstraintViolationException(violations));
                    }
                    return Mono.just(dto);
                })
                .doOnNext(user -> log.info("Petición recibida con usuario: {}", user))
                .map(mapper::toDomain)
                .flatMap(useCase::saveUsuario)
                .flatMap(savedUser ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .bodyValue(savedUser)
                )
                .doOnSubscribe(sub -> log.debug("Iniciando flujo de guardado de usuario"))
                .doOnSuccess(resp -> log.debug("Respuesta construida correctamente"))
                .doOnError(e -> log.error("Error al guardar usuario", e));
    }


}
