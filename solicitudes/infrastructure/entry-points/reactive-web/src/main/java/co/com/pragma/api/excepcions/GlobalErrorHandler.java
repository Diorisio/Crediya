package co.com.pragma.api.excepcions;


import co.com.pragma.api.dto.ErrorResponse;
import co.com.pragma.usecase.solicitud.excepcions.TipoPrestamoNotFoundException;
import co.com.pragma.usecase.solicitud.excepcions.TokenEmailNotMatch;
import co.com.pragma.usecase.solicitud.excepcions.UsuarioNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;

@Slf4j
@Component
@Order(-2) // Se ejecuta antes que el DefaultErrorWebExceptionHandler
public class GlobalErrorHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalErrorHandler() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()) // soporte para LocalDateTime
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // formateo legible
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Error capturado globalmente: ", ex);

        HttpStatus status;
        ErrorResponse body;

        if (ex instanceof ConstraintViolationException) {
            status = HttpStatus.BAD_REQUEST;
            body = ErrorResponse.builder()
                    .codigo("DOM-001")
                    .mensaje("Error de validación: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
        } else if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
            body = ErrorResponse.builder()
                    .codigo("DOM-002")
                    .mensaje("Petición incorrecta: " + ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
        } else if (ex instanceof TokenEmailNotMatch) {
            status = HttpStatus.NOT_FOUND;
            body = ErrorResponse.builder()
                    .codigo("DOM-003")
                    .mensaje(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

        }else if (ex instanceof UsuarioNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            body = ErrorResponse.builder()
                    .codigo("DOM-004")
                    .mensaje(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();

        }
        else if (ex instanceof TipoPrestamoNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            body = ErrorResponse.builder()
                    .codigo("DOM-005")
                    .mensaje(ex.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
        }
        else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = ErrorResponse.builder()
                    .codigo("DOM-999")
                    .mensaje("Ocurrió un error inesperado. Intente más tarde.")
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] jsonBytes = objectMapper.writeValueAsBytes(body);
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse()
                            .bufferFactory().wrap(jsonBytes)));
        } catch (Exception e) {
            log.error("Error serializando respuesta de error", e);
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }
    }
}

