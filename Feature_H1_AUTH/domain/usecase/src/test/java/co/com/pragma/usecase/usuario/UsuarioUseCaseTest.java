package co.com.pragma.usecase.usuario;

import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsuarioUseCaseTest {
    private UsuarioRepository usuarioRepository;


    private UsuarioUseCase useCase;

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
        when(usuarioRepository.existsByCorreoElectronico("carlos.perez@example.com")).thenReturn(Mono.empty());
        when(usuarioRepository.saveUsuario(usuario)).thenReturn(Mono.just(usuario));

        Usuario result = useCase.saveUsuario(usuario).block();
        assertEquals(usuario, result);
        verify(usuarioRepository).saveUsuario(usuario);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExistsTest() {
        Usuario usuario = new Usuario("Jane", "Smith", "jane@smith.com", 2000.0, null, "street", "456");
        when(usuarioRepository.existsByCorreoElectronico("jane@smith.com")).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.saveUsuario(usuario))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El correo ya está registrado"))
                .verify();

        verify(usuarioRepository, never()).saveUsuario(any());
    }

    @Test
    void shouldThrowExceptionOnValidationErrorTest() {
        Usuario usuario = new Usuario("", "Smith", "jane@smith.com", 2000.0, null, "street", "456");

        StepVerifier.create(Mono.defer(() -> useCase.saveUsuario(usuario)))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("El nombre es obligatorio"))
                .verify();
    }
}