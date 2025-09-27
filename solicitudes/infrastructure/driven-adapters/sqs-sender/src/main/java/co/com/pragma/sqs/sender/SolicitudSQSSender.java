package co.com.pragma.sqs.sender;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudQueueGateway;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class SolicitudSQSSender implements SolicitudQueueGateway {

    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public Mono<Void> enviarSolicitud(Solicitud solicitud, Integer intereses) {
        String message = serialize(solicitud, String.valueOf(intereses));

        SendMessageRequest request1 = SendMessageRequest.builder()
                .queueUrl(properties.queueUrlCola())
                .messageBody(message)
                .build();

        SendMessageRequest request2 = SendMessageRequest.builder()
                .queueUrl(properties.queueUrlReporte())
                .messageBody(message)
                .build();

        return Mono.when(
                Mono.fromFuture(() -> client.sendMessage(request1))
                        .doOnNext(r -> log.debug("Mensaje enviado a cola de capacidad, messageId={}", r.messageId())),
                Mono.fromFuture(() -> client.sendMessage(request2))
                        .doOnNext(r -> log.debug("Mensaje enviado a cola de reportes, messageId={}", r.messageId()))
        );
    }

    private String serialize(Solicitud solicitud, String intereses) {
        try {
            Map<String, Object> wrapper = new HashMap<>();
            wrapper.put("solicitud", solicitud);
            wrapper.put("intereses", intereses);

            return mapper.writeValueAsString(wrapper);
        } catch (Exception e) {
            log.error("Error serializando Solicitud", e);
            throw new RuntimeException("Error serializando Solicitud", e);
        }
    }


    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrlCola())
                .messageBody(message)
                .build();
    }
}
