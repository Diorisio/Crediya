package co.com.pragma.usecase.solicitud;


import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.Usuario;
import co.com.pragma.model.solicitud.gateways.SolicitudQueueGateway;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.pragma.model.solicitud.gateways.UsuarioClient;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigInteger;


@RequiredArgsConstructor
public class ValidacionAutomaticaUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioClient usuarioClient;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final SolicitudQueueGateway solicitudQueueGateway; // Para enviar correo

    private static final BigInteger ESTADO_REVISION_MANUAL = BigInteger.valueOf(1);
    private static final BigInteger ESTADO_APROBADO = BigInteger.valueOf(2);
    private static final BigInteger ESTADO_RECHAZADO = BigInteger.valueOf(3);

    /**
     * Procesa la validación automática de una solicitud.
     */
    public Mono<Solicitud> validarSolicitud(Solicitud solicitud, Usuario usuario) {
        return solicitudRepository.findByIdentificacion(solicitud.getIdentificacion())
                .filter(p -> {
                    boolean valido = BigInteger.valueOf(2).equals(p.getIdEstado());
                    System.out.println("🔍 Evaluando solicitud existente " + p.getIdEstado() + " con estado=" + p.getIdEstado() + " -> " + (valido ? "válida" : "ignorada"));
                    return valido;
                }) // solo solicitudes en estado 1
                .flatMap(p -> tipoPrestamoRepository.findByIdTipoPrestamo(p.getIdTipoPrestamo())
                        .map(tipo -> calcularCuotaMensual(
                                p.getMonto(),
                                tipo.getTasaInteres(),
                                p.getPlazo()
                        )))
                .collectList()
                .flatMap(cuotas -> {
                    double deudaActual = cuotas.stream().mapToDouble(Double::doubleValue).sum();
                    System.out.println("deudaActual " + deudaActual);

                    // Ejemplo: capacidad máxima = 40% del salario base del usuario
                    double capacidadMaxima = usuario.getSalarioBase().doubleValue() * 0.4;
                    double capacidadDisponible = capacidadMaxima - deudaActual;
                    System.out.println("capacidadMaxima " + capacidadMaxima);
                    System.out.println("capacidadDisponible " + capacidadDisponible);
                    return tipoPrestamoRepository.findByIdTipoPrestamo(solicitud.getIdTipoPrestamo())
                            .flatMap(tipoNuevo -> {
                                double cuotaNueva = calcularCuotaMensual(
                                        solicitud.getMonto(),
                                        tipoNuevo.getTasaInteres(),
                                        solicitud.getPlazo()
                                );
                                System.out.println("cuotaNueva " + cuotaNueva);
                                if (cuotaNueva <= capacidadDisponible) {
                                    if (solicitud.getMonto() > usuario.getSalarioBase().doubleValue() * 5) {
                                        solicitud.setIdEstado(ESTADO_REVISION_MANUAL); // revisión manual
                                        return solicitudRepository.save(solicitud, solicitud.getCorreoElectronico());
                                    } else {
                                        solicitud.setIdEstado(ESTADO_APROBADO); // aprobado
                                        return solicitudQueueGateway.enviarSolicitud(solicitud,tipoNuevo.getTasaInteres())
                                                .then(solicitudRepository.save(solicitud, solicitud.getCorreoElectronico()));
                                    }
                                } else {
                                    solicitud.setIdEstado(ESTADO_RECHAZADO); // rechazado
                                    return solicitudRepository.save(solicitud, solicitud.getCorreoElectronico());
                                }
                            });
                });
    }



    private double calcularCuotaMensual(double monto, double tasaAnual, int plazoMeses) {
        double i = tasaAnual / 100.0 / 12.0;
        return monto * i * Math.pow(1 + i, plazoMeses) / (Math.pow(1 + i, plazoMeses) - 1);
    }

}
