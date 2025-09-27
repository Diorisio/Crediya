package co.com.pragma.usecase.solicitud;

import co.com.pragma.model.solicitud.*;
import co.com.pragma.model.solicitud.gateways.*;
import co.com.pragma.usecase.solicitud.excepcions.TipoPrestamoNotFoundException;
import co.com.pragma.usecase.solicitud.excepcions.UsuarioNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudUseCaseTest {


    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private ValidacionAutomaticaUseCase validacionAutomaticaUseCase;

    @Mock
    private ColaMensajesGateway colaMensajesGateway;

    private SolicitudUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SolicitudUseCase(solicitudRepository, usuarioClient, tipoPrestamoRepository,estadoRepository,validacionAutomaticaUseCase);
    }

    @Test
    void shouldSaveSolicitudWhenUsuarioAndTipoPrestamoExist() {
        Solicitud solicitud = Solicitud.builder()
                .identificacion(BigInteger.valueOf(32123))
                .idTipoPrestamo(1L)
                .correoElectronico("test@test.com")
                .monto(5000.0)
                .plazo(12)
                .build();

        Usuario usuario = Usuario.builder()
                .idusuario(1L)
                .nombre("Ana")
                .apellido("García")
                .salarioBase(BigInteger.valueOf(2500))
                .documentoIdentidad("99999")
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(2L)
                .nombre("Hipotecario")
                .tasaInteres(8)
                .montoMaximo(500000.0)
                .montoMinimo(10000.0)
                .validacionAutomatica(true)
                .build();

        when(usuarioClient.existeUsuario(BigInteger.valueOf(32123)))
                .thenReturn(Mono.just(usuario));

        when(tipoPrestamoRepository.findByIdTipoPrestamo(1L))
                .thenReturn(Mono.just(tipoPrestamo));

        when(solicitudRepository.save(solicitud,solicitud.getCorreoElectronico()))
                .thenReturn(Mono.just(solicitud));

        // when / then
        StepVerifier.create(useCase.save(solicitud,solicitud.getCorreoElectronico()))
                .expectNext(solicitud)
                .verifyComplete();

        verify(usuarioClient).existeUsuario(BigInteger.valueOf(32123));
        verify(tipoPrestamoRepository).findByIdTipoPrestamo(1L);
        verify(solicitudRepository).save(solicitud,solicitud.getCorreoElectronico());
    }

    @Test
    void shouldThrowWhenUsuarioDoesNotExist() {
        Solicitud solicitud = Solicitud.builder()
                .identificacion(BigInteger.valueOf(32123))
                .idTipoPrestamo(1L)
                .correoElectronico("test@test.com")
                .monto(5000.0)
                .plazo(12)
                .build();

        // Simulamos que no existe usuario
        when(usuarioClient.existeUsuario(BigInteger.valueOf(32123)))
                .thenReturn(Mono.empty());

        Mono<Solicitud> result = useCase.save(solicitud, solicitud.getCorreoElectronico());

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
                .idTipoPrestamo(1L)
                .correoElectronico("test@test.com")
                .monto(5000.0)
                .plazo(12)
                .build();

        Usuario usuario = Usuario.builder()
                .idusuario(1L)
                .nombre("Ana")
                .apellido("García")
                .salarioBase(BigInteger.valueOf(2500))
                .documentoIdentidad("99999")
                .build();

        // Usuario sí existe
        when(usuarioClient.existeUsuario(BigInteger.valueOf(32123)))
                .thenReturn(Mono.just(usuario));

        // Pero el tipo de préstamo no existe
        when(tipoPrestamoRepository.findByIdTipoPrestamo(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.save(solicitud, solicitud.getCorreoElectronico()))
                .expectError(TipoPrestamoNotFoundException.class)
                .verify();

        verify(usuarioClient).existeUsuario(BigInteger.valueOf(32123));
        verify(tipoPrestamoRepository).findByIdTipoPrestamo(1L);
        verifyNoInteractions(solicitudRepository);
    }

    @Test
    void listarSolicitudes_retornaResultados() {

        Solicitud solicitud = new Solicitud();
        solicitud.setIdentificacion(BigInteger.valueOf(99999));
        solicitud.setMonto(2000.0);
        solicitud.setPlazo(24);
        solicitud.setCorreoElectronico("otro@ejemplo.com");
        solicitud.setIdEstado(BigInteger.valueOf(2));
        solicitud.setIdTipoPrestamo(2L);

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(2L)
                .nombre("Hipotecario")
                .tasaInteres(8)
                .montoMaximo(500000.0)
                .montoMinimo(10000.0)
                .validacionAutomatica(true)
                .build();
        Usuario usuario = Usuario.builder()
                .idusuario(1L)
                .nombre("Juan Pérez")
                .apellido("García")
                .salarioBase(BigInteger.valueOf(2500))
                .documentoIdentidad("99999")
                .build();

        when(solicitudRepository.findbyIdEstado("1"))
                .thenReturn(Flux.just(solicitud));

        when(usuarioClient.existeUsuario(solicitud.getIdentificacion()))
                .thenReturn(Mono.just(usuario));

        when(tipoPrestamoRepository.findByIdTipoPrestamo(solicitud.getIdTipoPrestamo()))
                .thenReturn(Mono.just(tipoPrestamo));

        Estado estado = new Estado(BigInteger.valueOf(2), "APROBADO");
        when(estadoRepository.findByIdEstado(BigInteger.valueOf(2)))
                .thenReturn(Mono.just(estado));

        Flux<ListaSolicitudes> resultado = useCase.listarSolicitudes("1", 0, 10, null, null);


        StepVerifier.create(resultado)
                .expectNextMatches(lista ->
                        lista.getNombre().equals("Juan Pérez") &&
                                lista.getCorreoElectronico().equals("otro@ejemplo.com") &&
                                lista.getEstado().equals("APROBADO")
                )
                .verifyComplete();
    }

    @Test
    void listarSolicitudes_sinResultados() {
        when(solicitudRepository.findbyIdEstado("1"))
                .thenReturn(Flux.empty());

        Flux<ListaSolicitudes> resultado = useCase.listarSolicitudes("1", 0, 10, null, null);

        StepVerifier.create(resultado)
                .verifyComplete();
    }

    @Test
    void listarSolicitudes_filtradoPorEmail() {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdentificacion(BigInteger.valueOf(99999));
        solicitud.setMonto(2000.0);
        solicitud.setPlazo(24);
        solicitud.setCorreoElectronico("otro@ejemplo.com");
        solicitud.setIdEstado(BigInteger.valueOf(2));
        solicitud.setIdTipoPrestamo(2L);

        Usuario usuario = Usuario.builder()
                .idusuario(1L)
                .nombre("Ana")
                .apellido("García")
                .salarioBase(BigInteger.valueOf(2500))
                .documentoIdentidad("99999")
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(2L)
                .nombre("Hipotecario")
                .tasaInteres(8)
                .montoMaximo(500000.0)
                .montoMinimo(10000.0)
                .validacionAutomatica(true)
                .build();

        when(solicitudRepository.findbyIdEstado("1"))
                .thenReturn(Flux.just(solicitud));

        when(usuarioClient.existeUsuario(solicitud.getIdentificacion()))
                .thenReturn(Mono.just(usuario));

        when(tipoPrestamoRepository.findByIdTipoPrestamo(solicitud.getIdTipoPrestamo()))
                .thenReturn(Mono.just(tipoPrestamo));

        when(estadoRepository.findByIdEstado(solicitud.getIdEstado()))
                .thenReturn(Mono.just(new Estado(BigInteger.valueOf(1), "PENDIENTE")));

        Flux<ListaSolicitudes> resultado = useCase.listarSolicitudes("1", 0, 10, "otro@ejemplo.com", null);

        StepVerifier.create(resultado)
                .expectNextMatches(lista -> lista.getCorreoElectronico().equals("otro@ejemplo.com"))
                .verifyComplete();
    }

    @Test
    void listarSolicitudes_filtradoPorIdentificacion() {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdentificacion(BigInteger.valueOf(99999));
        solicitud.setMonto(2000.0);
        solicitud.setPlazo(24);
        solicitud.setCorreoElectronico("otro@ejemplo.com");
        solicitud.setIdEstado(BigInteger.valueOf(2));
        solicitud.setIdTipoPrestamo(2L);

        Usuario usuario = Usuario.builder()
                .idusuario(1L)
                .nombre("Ana")
                .apellido("García")
                .salarioBase(BigInteger.valueOf(2500))
                .documentoIdentidad("99999")
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(2L)
                .nombre("Hipotecario")
                .tasaInteres(8)
                .montoMaximo(500000.0)
                .montoMinimo(10000.0)
                .validacionAutomatica(true)
                .build();

        Estado estado = new Estado(BigInteger.valueOf(2), "APROBADO");

        when(solicitudRepository.findbyIdEstado("2"))
                .thenReturn(Flux.just(solicitud));

        when(usuarioClient.existeUsuario(solicitud.getIdentificacion()))
                .thenReturn(Mono.just(usuario));

        when(tipoPrestamoRepository.findByIdTipoPrestamo(solicitud.getIdTipoPrestamo()))
                .thenReturn(Mono.just(tipoPrestamo));

        // Aquí el mock debe usar el mismo ID que tiene la solicitud
        when(estadoRepository.findByIdEstado(BigInteger.valueOf(2)))
                .thenReturn(Mono.just(estado));

        Flux<ListaSolicitudes> resultado =
                useCase.listarSolicitudes("2", 0, 10, null, BigInteger.valueOf(99999));

        StepVerifier.create(resultado)
                .expectNextMatches(lista ->
                        lista.getIdentificacion().equals(BigInteger.valueOf(99999)) &&
                                lista.getNombre().equals("Ana") &&
                                lista.getEstado().equals("APROBADO") &&
                                lista.getTipoPrestamo().equals("Hipotecario") &&
                                lista.getDeudaTotal() != null &&
                                lista.getSalarioBase().equals(BigInteger.valueOf(2500))
                )
                .verifyComplete();
    }


    @Test
    void listarSolicitudes_paginacion() {
        Solicitud s1 = new Solicitud();
        s1.setIdentificacion(BigInteger.valueOf(1));
        s1.setMonto(1000.0);
        s1.setPlazo(12);
        s1.setCorreoElectronico("uno@ejemplo.com");
        s1.setIdEstado(BigInteger.valueOf(2));
        s1.setIdTipoPrestamo(2L);

        Solicitud s2 = new Solicitud();
        s2.setIdentificacion(BigInteger.valueOf(2));
        s2.setMonto(2000.0);
        s2.setPlazo(12);
        s2.setCorreoElectronico("dos@ejemplo.com");
        s2.setIdEstado(BigInteger.valueOf(2));
        s2.setIdTipoPrestamo(2L);

        Usuario usuario2 = Usuario.builder()
                .idusuario(2L)
                .nombre("Luis")
                .apellido("Pérez")
                .salarioBase(BigInteger.valueOf(3000))
                .documentoIdentidad("88888")
                .build();

        TipoPrestamo tipoPrestamo = TipoPrestamo.builder()
                .idTipoPrestamo(2L)
                .nombre("Hipotecario")
                .tasaInteres(8)
                .montoMaximo(500000.0)
                .montoMinimo(10000.0)
                .validacionAutomatica(true)
                .build();

        when(solicitudRepository.findbyIdEstado("2"))
                .thenReturn(Flux.just(s1, s2));

        when(usuarioClient.existeUsuario(BigInteger.valueOf(2)))
                .thenReturn(Mono.just(usuario2));

        when(tipoPrestamoRepository.findByIdTipoPrestamo(2L))
                .thenReturn(Mono.just(tipoPrestamo));

        when(estadoRepository.findByIdEstado(BigInteger.valueOf(2)))
                .thenReturn(Mono.just(new Estado(BigInteger.valueOf(2), "PENDIENTE")));

        Flux<ListaSolicitudes> resultado =
                useCase.listarSolicitudes("2", 1, 1, null, null);

        StepVerifier.create(resultado)
                .expectNextMatches(lista ->
                        lista.getIdentificacion().equals(BigInteger.valueOf(2)) &&
                                lista.getCorreoElectronico().equals("dos@ejemplo.com") &&
                                lista.getEstado().equals("PENDIENTE")
                )
                .verifyComplete();
    }


}

