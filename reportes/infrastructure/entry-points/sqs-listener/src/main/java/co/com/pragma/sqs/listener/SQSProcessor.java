package co.com.pragma.sqs.listener;

import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.ReporteEvento;
import co.com.pragma.usecase.reporte.ReporteEventoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {

    private final ReporteEventoUseCase reporteEventoUseCase;
    private final ObjectMapper mapper;

    @Override
    public Mono<Void> apply(Message message) {
        log.info("Received message from SQS: {}", message.body());

        return Mono.fromCallable(() -> mapper.readValue(message.body(), ReporteEvento.class))
                .flatMap(reporteEventoUseCase::procesoMensajeAprobadoApplicationsReporteCola)
                .doOnSuccess(v -> log.info("Message procesado exitosamente"))
                .doOnError(e -> log.error("Error procesando message: {}", e.getMessage(), e))
                .onErrorResume(e -> Mono.empty());
    }

}
