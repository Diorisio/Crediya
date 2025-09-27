package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.Estado;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.*;
import co.com.pragma.usecase.solicitud.excepcions.EstadoFoundException;
import co.com.pragma.usecase.solicitud.excepcions.SolicitudNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ActualizarSolicitudUseCaseTest {

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private ColaMensajesGateway colaMensajesGateway;

    private ActualizarSolicitudUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ActualizarSolicitudUseCase(solicitudRepository, colaMensajesGateway, estadoRepository);
    }


    @Test
    void actualizarEstado_cuandoSolicitudNoExiste_lanzaError() {
        Long id = 1L;

        when(solicitudRepository.findByIdSolicitud(id))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.actualizarEstado(id, BigInteger.valueOf(2)))
                .expectError(SolicitudNotFoundException.class)
                .verify();

        verify(solicitudRepository).findByIdSolicitud(id);
    }

    @Test
    void actualizarEstado_cuandoYaTieneElMismoEstado_lanzaError() {
        Long id = 1L;
        BigInteger estadoActual = BigInteger.valueOf(2);

        // Solicitud con estado actual = 2
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(id);
        solicitud.setIdEstado(estadoActual);

        // Mock repositorios
        when(solicitudRepository.findByIdSolicitud(id))
                .thenReturn(Mono.just(solicitud));

        Estado estado = new Estado();
        estado.setId(estadoActual);
        estado.setNombre("Aprobado");

        when(estadoRepository.findByIdEstado(estadoActual))
                .thenReturn(Mono.just(estado));

        // Verificación
        StepVerifier.create(useCase.actualizarEstado(id, estadoActual))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(EstadoFoundException.class);
                    assertThat(error.getMessage()).contains("Aprobado");
                })
                .verify();
    }

    @Test
    void actualizarEstado_cambiaEstadoYEnviaMensaje() {
        // Arrange
        Long id = 1L;
        BigInteger estadoActual = BigInteger.valueOf(1);
        BigInteger estadoNuevo = BigInteger.valueOf(2);

        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(id);
        solicitud.setIdEstado(estadoActual);
        solicitud.setCorreoElectronico("correo@prueba.com"); //

        Estado estado = new Estado();
        estado.setId(estadoNuevo);
        estado.setNombre("APROBADO");

        // Mocks
        when(solicitudRepository.findByIdSolicitud(id))
                .thenReturn(Mono.just(solicitud));

        when(solicitudRepository.save(any(Solicitud.class), eq("correo@prueba.com")))
                .thenReturn(Mono.just(solicitud));

        when(estadoRepository.findByIdEstado(estadoNuevo))
                .thenReturn(Mono.just(estado));

        when(colaMensajesGateway.enviarMensaje(solicitud))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(useCase.actualizarEstado(id, estadoNuevo))
                .expectNextMatches(saved ->
                        saved.getIdEstado().equals(estadoNuevo) //
                )
                .verifyComplete();

        // Verify interactions
        verify(solicitudRepository).findByIdSolicitud(id);
        verify(solicitudRepository).save(any(Solicitud.class), eq("correo@prueba.com"));
        verify(estadoRepository).findByIdEstado(estadoNuevo);
        verify(colaMensajesGateway).enviarMensaje(solicitud); //
    }
}
