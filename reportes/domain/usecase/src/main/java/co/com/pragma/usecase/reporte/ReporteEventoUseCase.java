package co.com.pragma.usecase.reporte;

import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.ReporteEvento;
import co.com.pragma.model.reporte.gateways.LoggerGateway;
import co.com.pragma.model.reporte.gateways.ReporteEventoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class ReporteEventoUseCase {

    private final ReporteEventoRepository reporteEventoRepository;
    private final ReporteUseCase reporteUseCase;
    private final LoggerGateway logger;

    public Mono<Void> procesoMensajeAprobadoApplicationsReporteCola(ReporteEvento reporteEvento) {
        Long solicitudId = reporteEvento.getSolicitudId();
        String estado = reporteEvento.getEstados();
        BigDecimal monto = reporteEvento.getMonto();


        if (shouldSkipProcessingMessage(solicitudId, estado, monto)) {
            return Mono.empty();
        }

        return reporteEventoRepository.save(reporteEvento)
                .doOnSuccess(saved -> logger.info("Solicitud reporte guardado exitosamente con solicitudId: {}", saved.getSolicitudId()))
                .doOnError(e -> logger.error("fallar a guardar reporteEvento. Error: {}", e.getMessage()))
                .flatMap(saved -> reporteUseCase.incremento(saved.getMonto())
                        .doOnSuccess(v -> logger.info("Solicitud estado actualizado exitosamente por monto: {}", saved.getMonto()))
                        .doOnError(e -> logger.error("Failed to update loan stats. Error: {}", e.getMessage()))
                )
                .onErrorResume(e -> {
                    logger.error("An error occurred during processing: {}", e.getMessage());
                    return Mono.empty();
                })
                .then();
    }

    private boolean shouldSkipProcessingMessage(Long solicitudId, String status, BigDecimal amount) {
        if (solicitudId == null || status == null || status.trim().isEmpty() || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Skipping message due to invalid fields - solicitudId: {}, status: '{}', amount: {}", solicitudId, status, amount);
            return true;

        }
        if (!"APROBADO".equalsIgnoreCase(status)) {
            logger.warn("The status is not valid. Status: {}", status);
            return true;
        }
        return false;
    }
}
