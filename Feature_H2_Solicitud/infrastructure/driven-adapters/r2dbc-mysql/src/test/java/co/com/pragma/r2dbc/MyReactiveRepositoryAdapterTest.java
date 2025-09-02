package co.com.pragma.r2dbc;

import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.r2dbc.entity.SolicitudEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigInteger;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @Mock
    private MyReactiveRepository myReactiveRepository;

    @Mock
    private ObjectMapper mapper;

    private MyReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MyReactiveRepositoryAdapter(myReactiveRepository, mapper);
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
        entity.setCorreoElectronico(solicitud.getCorreoElectronico());
        entity.setIdTipoPrestamo(solicitud.getIdTipoPrestamo());
        entity.setMonto(solicitud.getMonto());
        entity.setPlazo(solicitud.getPlazo());



        Mockito.when(mapper.map(Mockito.any(Solicitud.class), Mockito.eq(SolicitudEntity.class)))
                .thenAnswer(invocation -> {
                    Solicitud s = invocation.getArgument(0);
                    SolicitudEntity e = new SolicitudEntity();
                    e.setCorreoElectronico(s.getCorreoElectronico());
                    e.setIdTipoPrestamo(s.getIdTipoPrestamo());
                    e.setMonto(s.getMonto());
                    e.setPlazo(s.getPlazo());
                    return e;
                });

        Mockito.when(myReactiveRepository.save(Mockito.any(SolicitudEntity.class)))
                .thenReturn(Mono.just(new SolicitudEntity()));


        Mono<Solicitud> result = adapter.save(solicitud);


        Mockito.verify(myReactiveRepository, Mockito.times(1)).save(Mockito.any(SolicitudEntity.class));


    }


    @Test
    void guardarSolicitudConDtoNuloLanzaNullPointerTest() {
        StepVerifier.create(Mono.defer(() -> adapter.save(null)))
                .expectError(NullPointerException.class)
                .verify();
    }
}
