package co.com.pragma.api;

import co.com.pragma.api.dto.ListaSolicitudesResponse;
import co.com.pragma.api.dto.RequestSolicitud;
import co.com.pragma.api.mapper.SolicitudEntityMapper;
import co.com.pragma.model.solicitud.ListaSolicitudes;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.usecase.solicitud.ActualizarSolicitudUseCase;
import co.com.pragma.usecase.solicitud.SolicitudUseCase;
import co.com.pragma.usecase.solicitud.ValidacionAutomaticaUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

@Log4j2
@Component
@RequiredArgsConstructor
public class Handler {

    private  final SolicitudUseCase useCase;

    private  final ValidacionAutomaticaUseCase validacionAutomaticaUseCase;

    private  final ActualizarSolicitudUseCase actualizarSolicitudUseCase;

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
                .zipWith(ReactiveSecurityContextHolder.getContext()
                        .map(ctx -> ctx.getAuthentication().getName()))
                .flatMap(tuple -> {
                    Solicitud solicitud = tuple.getT1();
                    String emailToken = tuple.getT2(); // viene del JWT
                    return useCase.save(solicitud, emailToken);
                })

                .flatMap(savedSolicitud ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .bodyValue(savedSolicitud)
                )
                .doOnSubscribe(sub -> log.debug("Iniciando flujo de guardado de solicitud"))
                .doOnSuccess(resp -> log.debug("Respuesta construida correctamente"))
                .doOnError(e -> log.error("Error al guardar la solicitud", e));
    }

    public Mono<ServerResponse> listenGETListar(ServerRequest serverRequest) {
        String estadoId = serverRequest.queryParam("estadoId").orElse("1");
        int page = Integer.parseInt(serverRequest.queryParam("page").orElse("0"));
        int size = Integer.parseInt(serverRequest.queryParam("size").orElse("10"));
        String email = serverRequest.queryParam("email").orElse(null);
        BigInteger identificacion = serverRequest.queryParam("identificacion")
                .map(BigInteger::new)
                .orElse(null);

        Flux<ListaSolicitudes> flujoSolicitudes = ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getName())
                .flatMapMany(username -> useCase.listarSolicitudes(
                        estadoId, page, size, email, identificacion
                ));


        return flujoSolicitudes
                .collectList()
                .map(lista -> {
                    BigDecimal deudaTotal = lista.stream()
                            .map(s -> s.getMonto() != null ? BigDecimal.valueOf(s.getMonto()) : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new
                            ListaSolicitudesResponse(lista, deudaTotal);
                })
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> listenPUTactualizar(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        log.info("Recibida petición PUT para actualizar solicitud con id={}", id);

        return request.bodyToMono(RequestSolicitud.class)
                .doOnNext(body -> log.debug("Payload recibido: {}", body))
                .flatMap(body -> {
                    log.info("Llamando a actualizarEstado con id={} y idEstado={}", id, body.id_estado());
                    return actualizarSolicitudUseCase.actualizarEstado(id, body.id_estado());
                })
                .doOnSuccess(updated -> log.info("Solicitud actualizada correctamente: {}", updated))
                .doOnError(error -> log.error("Error actualizando solicitud con id={}: {}", id, error.getMessage(), error))
                .flatMap(updated -> ServerResponse.ok().bodyValue(updated));
    }

}
