package co.com.pragma.usecase.reporte;

import co.com.pragma.model.reporte.Reporte;
import co.com.pragma.model.reporte.gateways.LoggerGateway;
import co.com.pragma.model.reporte.gateways.ReporteRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class ReporteUseCase {

    private final ReporteRepository reporteRepository;
    private final LoggerGateway logger;

    public Mono<Reporte> estado() {
        return reporteRepository.obtenerReporteGlobal()
                .doOnSuccess(reporte -> logger.info("Checking report metrics - total_approved_loans: " +
                        "{}, total_approved_amount: {}", reporte.getTotalAprobadas(), reporte.getTotalPrestado()))
                .doOnError(e -> logger.error("An error occurred while retrieving report metrics. Message: {}", e.getMessage()));
    }

    public Mono<Void> incremento(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("Cannot update record in database amount: {}", monto);
            return Mono.empty();
        }

        return reporteRepository.incrementarAprobadas(monto)
                .doOnSuccess(v -> logger.info("Successfully updating report metrics."))
                .doOnError(e -> logger.error("An error occurred while updating report metrics. Message: {}", e.getMessage()));
    }
}
