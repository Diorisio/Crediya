package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.gateways.SolicitudRepository;
import co.com.pragma.model.solicitud.gateways.TipoPrestamoRepository;
import co.com.pragma.model.solicitud.gateways.UsuarioClient;
import co.com.pragma.usecase.solicitud.excepcions.TipoPrestamoNotFoundException;
import co.com.pragma.usecase.solicitud.excepcions.UsuarioNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudUseCaseTest {


    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    private SolicitudUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SolicitudUseCase(solicitudRepository, usuarioClient, tipoPrestamoRepository);
    }

    @Test
    void shouldSaveSolicitudWhenUsuarioAndTipoPrestamoExist() {
        Solicitud solicitud = Solicitud.builder()
                .identificacion(BigInteger.valueOf(32123))
                .idTipoPrestamo(BigInteger.ONE)
                .correoElectronico("test@test.com")
                .monto(5000.0)
                .plazo(12)
                .build();

        when(usuarioClient.existeUsuario(BigInteger.valueOf(32123)))
                .thenReturn(Mono.just(true));

        when(tipoPrestamoRepository.existsByIdTipoPrestamo(BigInteger.ONE))
                .thenReturn(Mono.just(true));

        when(solicitudRepository.save(solicitud))
                .thenReturn(Mono.just(solicitud));

        // when / then
        StepVerifier.create(useCase.save(solicitud))
                .expectNext(solicitud)
                .verifyComplete();

        verify(usuarioClient).existeUsuario(BigInteger.valueOf(32123));
        verify(tipoPrestamoRepository).existsByIdTipoPrestamo(BigInteger.ONE);
        verify(solicitudRepository).save(solicitud);
    }

    @Test
    void shouldThrowWhenUsuarioDoesNotExist() {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdentificacion(BigInteger.valueOf(32123));

        when(usuarioClient.existeUsuario(BigInteger.valueOf(32123))).thenReturn(Mono.just(false));

        Mono<Solicitud> result = useCase.save(solicitud);

        StepVerifier.create(result)
                .expectError(UsuarioNotFoundException.class)
                .verify();

        verify(usuarioClient).existeUsuario(BigInteger.valueOf(32123));
        verifyNoInteractions(tipoPrestamoRepository, solicitudRepository);
    }

    @Test
    void shouldThrowWhenTipoPrestamoDoesNotExist() {
        Solicitud solicitud = Solicitud.builder()
                .identificacion(BigInteger.valueOf(32123))
                .idTipoPrestamo(BigInteger.ONE)
                .correoElectronico("test@test.com")
                .monto(5000.0)
                .plazo(12)
                .build();

        when(usuarioClient.existeUsuario(BigInteger.valueOf(32123)))
                .thenReturn(Mono.just(true));

        when(tipoPrestamoRepository.existsByIdTipoPrestamo(BigInteger.ONE))
                .thenReturn(Mono.just(false));

        // when / then
        StepVerifier.create(useCase.save(solicitud))
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();
    }
}

