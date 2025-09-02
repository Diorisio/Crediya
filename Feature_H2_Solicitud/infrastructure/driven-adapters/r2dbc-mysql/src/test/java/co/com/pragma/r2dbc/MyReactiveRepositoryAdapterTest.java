package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.r2dbc.entity.SolicitudEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigInteger;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    private MyReactiveRepository myReactiveRepository;
    private MyReactiveRepositoryAdapter adapter;
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        myReactiveRepository = Mockito.mock(MyReactiveRepository.class);
        mapper = Mockito.mock(ObjectMapper.class);
        adapter = new MyReactiveRepositoryAdapter(myReactiveRepository,mapper);
    }

    @Test
    void guardarSolicitudExitosamenteTest() {
        Solicitud solicitud = Solicitud.builder()
                .identificacion(BigInteger.valueOf(123456))
                .correoElectronico("test@mail.com")
                .idTipoPrestamo(BigInteger.valueOf(1))
                .monto(1000.0)
                .plazo(12)
                .build();

        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(10L);
        entity.setCorreoElectronico("test@mail.com");
        entity.setIdTipoPrestamo(BigInteger.valueOf(1));


        // Mockear el comportamiento de saveData para que devuelva un Mono
        when(myReactiveRepository.save(Mockito.any()))
                .thenReturn(Mono.just(entity));

        StepVerifier.create(adapter.save(solicitud))
                .expectNextMatches(saved ->
                        saved.getCorreoElectronico().equals("test@mail.com") &&
                                saved.getIdTipoPrestamo().equals(BigInteger.valueOf(1))
                )
                .verifyComplete();


    }


    @Test
    void guardarSolicitudConDtoNuloLanzaNullPointerTest() {
        StepVerifier.create(Mono.defer(() -> adapter.save(null)))
                .expectError(NullPointerException.class)
                .verify();
    }
}
