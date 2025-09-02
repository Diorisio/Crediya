package co.com.pragma.usecase.usuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.usuario.excepcions.EmailAlreadyRegisteredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

class UsuarioUseCaseTest {

    private UsuarioRepository usuarioRepository;


    private UsuarioUseCase useCase;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        useCase = new UsuarioUseCase(usuarioRepository);
    }

    @Test
    void shouldSaveUserWhenEmailNotExistsTest() {
        Usuario usuario = new Usuario(
                "Carlos",
                "Pérez",
                "carlos.perez@example.com",
                LocalDate.of(1990, 5, 20),
                "Calle 123 #45-67",
                "3001234567",
                new BigInteger("2500000")
        );

        when(usuarioRepository.existsByCorreoElectronico("carlos.perez@example.com"))
                .thenReturn(Mono.just(false));

        when(usuarioRepository.saveUsuario(usuario))
                .thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.saveUsuario(usuario))
                .expectNext(usuario)
                .verifyComplete();
        verify(usuarioRepository).saveUsuario(usuario);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExistsTest() {
        Usuario usuario = new Usuario(
                "Carlos",
                "Pérez",
                "carlos.perez@example.com",
                LocalDate.of(1990, 5, 20),
                "Calle 123 #45-67",
                "3001234567",
                new BigInteger("2500000")
        );        when(usuarioRepository.existsByCorreoElectronico("carlos.perez@example.com")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.saveUsuario(usuario))
                .expectError(EmailAlreadyRegisteredException.class)
                .verify();

        verify(usuarioRepository, never()).saveUsuario(any());
    }

}