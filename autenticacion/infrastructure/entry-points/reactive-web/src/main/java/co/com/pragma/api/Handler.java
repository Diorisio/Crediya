package co.com.pragma.api;

import co.com.pragma.api.dto.AuthRequestDto;
import co.com.pragma.api.dto.RequestGuardarUsuarioDto;
import co.com.pragma.api.mapper.AuthMapper;
import co.com.pragma.api.mapper.UsuarioEntityMapper;
import co.com.pragma.usecase.usuario.JwtUseCase;
import co.com.pragma.usecase.usuario.UsuarioUseCase;
import co.com.pragma.usecase.usuario.excepcions.UsuarioNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class Handler {

    private final UsuarioUseCase useCase;
    private final UsuarioEntityMapper mapper;

    private  final JwtUseCase useCaseJwt;

    private  final AuthMapper mapperJwt;

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
                                .bodyValue("Usuario guardado correctamente")
                )
                .doOnSubscribe(sub -> log.debug("Iniciando flujo de guardado de usuario"))
                .doOnSuccess(resp -> log.debug("Respuesta construida correctamente"))
                .doOnError(e -> log.error("Error al guardar usuario", e));
    }


    public Mono<ServerResponse> listenGETBuscarUsuario(ServerRequest serverRequest) {
        String documento = serverRequest.pathVariable("documento");
        log.info("Iniciando búsqueda de usuario con documento: {}", documento);

        return useCase.findByDocumentoIdentidad(documento)
                .flatMap(usuario -> {
                    log.info("Usuario encontrado: {}", usuario.getDocumentoIdentidad());
                    return ServerResponse.ok().bodyValue(usuario);
                })
                .switchIfEmpty(Mono.error(new UsuarioNotFoundException(documento))) // si no existe, lanza excepción
                .doOnError(e -> log.error("Error al consultar", e));
    }

    public Mono<ServerResponse> listenPOSTgenerarToken(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AuthRequestDto.class)
                .flatMap(authRequestDto -> {
                    Set<ConstraintViolation<AuthRequestDto>> violations = validator.validate(authRequestDto);
                    if (!violations.isEmpty()) {
                        return Mono.error(new ConstraintViolationException(violations));
                    }
                    return Mono.just(authRequestDto);
                })
                .map(mapperJwt::toDomain)
                .flatMap(useCaseJwt::login)
                .map(mapperJwt::toResponse)
                .flatMap(dto ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(dto)
                )
                .doOnSubscribe(sub -> log.debug("Iniciando flujo para generar token"))
                .doOnSuccess(resp -> log.debug("Respuesta construida correctamente"))
                .doOnError(e -> log.error("Error al general el token", e));
    }
}
