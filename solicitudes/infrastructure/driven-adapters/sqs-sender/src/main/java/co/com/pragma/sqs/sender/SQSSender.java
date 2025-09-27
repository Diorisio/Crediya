package co.com.pragma.sqs.sender;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.ColaMensajesGateway;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements ColaMensajesGateway {

    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public Mono<Void> enviarMensaje(Solicitud solicitud) {
        return Mono.fromCallable(() -> serialize(solicitud))
                .map(this::buildRequest)
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Correo encolado para cliente, messageId={}", response.messageId()))
                .then();
    }


    private String serialize(Solicitud solicitud) {
        try {
            return mapper.writeValueAsString(solicitud);
        } catch (Exception e) {
            log.error("Error serializando Solicitud", e);
            throw new RuntimeException("Error serializando Solicitud", e);
        }
    }
    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }
}

