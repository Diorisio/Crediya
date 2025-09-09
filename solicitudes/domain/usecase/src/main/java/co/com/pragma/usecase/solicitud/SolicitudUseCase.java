package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.*;
import co.com.pragma.model.solicitud.gateways.EstadoRepository;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.pragma.model.solicitud.gateways.UsuarioClient;
import co.com.pragma.usecase.solicitud.excepcions.TipoPrestamoNotFoundException;
import co.com.pragma.usecase.solicitud.excepcions.TokenEmailNotMatch;
import co.com.pragma.usecase.solicitud.excepcions.UsuarioNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigInteger;


@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final UsuarioClient usuarioClient;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;


    public Mono<Solicitud> save(Solicitud solicitud,String emailToken)
        {
            if (!solicitud.getCorreoElectronico().equals(emailToken)) {
                return Mono.error(new TokenEmailNotMatch());
            }
            return usuarioClient.existeUsuario(solicitud.getIdentificacion())
                    .switchIfEmpty(Mono.error(new UsuarioNotFoundException(solicitud.getIdentificacion())))
                    .zipWhen(usuario ->
                            tipoPrestamoRepository.findByIdTipoPrestamo(solicitud.getIdTipoPrestamo())
                                    .switchIfEmpty(Mono.error(new TipoPrestamoNotFoundException(solicitud.getIdTipoPrestamo())))
                    )
                    .flatMap(tuple -> solicitudRepository.save(solicitud, emailToken));
        }



    public Flux<ListaSolicitudes> listarSolicitudes(
            String estadoId, int page, int size, String email, BigInteger identificacion) {

        int offset = page * size;

        return solicitudRepository.findbyIdEstado(estadoId)
                .filter(s -> (email == null || (s.getCorreoElectronico() != null
                        && s.getCorreoElectronico().equalsIgnoreCase(email)))
                        && (identificacion == null || (s.getIdentificacion() != null
                        && s.getIdentificacion().equals(identificacion))))
                .skip(offset)
                .take(size)
                .flatMap(solicitud ->
                        Mono.zip(
                                usuarioClient.existeUsuario(solicitud.getIdentificacion()),
                                tipoPrestamoRepository.findByIdTipoPrestamo(solicitud.getIdTipoPrestamo()),
                                estadoRepository.findByIdEstado(solicitud.getIdEstado())

                        ).map(tuple -> {
                            var usuario = tuple.getT1();
                            var tipoPrestamo = tuple.getT2();
                            var estado = tuple.getT3();

                            double cuota = solicitud.getMonto() * ((tipoPrestamo.getTasa_interes() / 100.0) *
                                    Math.pow(1 + (tipoPrestamo.getTasa_interes() / 100.0),
                                            solicitud.getPlazo())) / (Math.pow(1 + (tipoPrestamo.getTasa_interes() / 100.0),
                                    solicitud.getPlazo()) - 1);

                            double cuotaMensual = Math.round(cuota * 100.0) / 100.0;




                            return new ListaSolicitudes(
                                    usuario.getNombre(),
                                    solicitud.getIdentificacion(),
                                    solicitud.getMonto(),
                                    solicitud.getPlazo(),
                                    tipoPrestamo.getNombre(),
                                    solicitud.getCorreoElectronico(),
                                    estado.getNombre(),
                                    cuotaMensual,
                                    usuario.getSalarioBase()
                            );
                        })
                );
    }



}
