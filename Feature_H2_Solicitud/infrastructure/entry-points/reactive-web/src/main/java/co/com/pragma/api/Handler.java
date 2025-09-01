package co.com.pragma.api;

import co.com.pragma.api.dto.RequestSolicitud;
import co.com.pragma.api.mapper.SolicitudEntityMapper;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.usecase.solicitud.SolicitudUseCase;
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

@Log4j2
@Component
@RequiredArgsConstructor
public class Handler {

    private  final SolicitudUseCase useCase;

    private final SolicitudEntityMapper mapper;

    private final Validator validator;



    public Mono<ServerResponse> registrarSolicitud(ServerRequest serverRequest) {
        log.info("Solicitud POST /api/v1/solicitud");
        return serverRequest
                .bodyToMono(RequestSolicitud.class)
                .flatMap(dto -> {
                    Set<ConstraintViolation<RequestSolicitud>> violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        return Mono.error(new ConstraintViolationException(violations));
                    }
                    return Mono.just(dto);
                })
                .doOnNext(user -> log.info("Petición recibida para crear peticion: {}", user))
                .map(mapper::toDomain)
                .flatMap(useCase::save)
                .flatMap(savedSolicitud ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .bodyValue(savedSolicitud)
                )
                .doOnSubscribe(sub -> log.debug("Iniciando flujo de guardado de solicitud"))
                .doOnSuccess(resp -> log.debug("Respuesta construida correctamente"))
                .doOnError(e -> log.error("Error al guardar la solicitud", e));
    }
    }
