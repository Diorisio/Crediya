package co.com.pragma.usecase.usuario;

import co.com.pragma.model.jwt.TokenCredenciales;
import co.com.pragma.model.jwt.gateways.PasswordEncoderRepository;
import co.com.pragma.model.jwt.gateways.TokenRepository;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.model.usuario.gateways.UsuarioRepository;
import co.com.pragma.usecase.usuario.excepcions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseTest {

    @InjectMocks
    private UsuarioUseCase useCase;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoderRepository passwordEncoderRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private JwtUseCase jwtUseCase;

    private Usuario usuario;




    @Test
    void shouldSaveUserWhenEmailNotExistsTest() {
        Usuario usuario = Usuario.builder()
                .nombre("Carlos")
                .apellido("Pérez")
                .correoElectronico("carlos.perez@example.com")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .direccion("Calle 123 #45-67")
                .telefono("3001234567")
                .salarioBase(new BigInteger("2500000"))
                .documentoIdentidad("123456789")
                .password("password123")
                .idRol("USER")
                .intentos(0)
                .build();

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
        Usuario usuario = Usuario.builder()
                .nombre("Carlos")
                .apellido("Pérez")
                .correoElectronico("carlos.perez@example.com")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .direccion("Calle 123 #45-67")
                .telefono("3001234567")
                .salarioBase(new BigInteger("2500000"))
                .documentoIdentidad("123456789")
                .password("password123")
                .idRol("USER")
                .intentos(0)
                .build();
        when(usuarioRepository.existsByCorreoElectronico("carlos.perez@example.com")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.saveUsuario(usuario))
                .expectError(EmailAlreadyRegisteredException.class)
                .verify();

        verify(usuarioRepository, never()).saveUsuario(any());
    }

    @Test
    void loginExitosoDebeGenerarToken() {

        TokenCredenciales credenciales = new TokenCredenciales("carlos.perez@example.com", "password123");

        Usuario usuario = Usuario.builder()
                .correoElectronico("carlos.perez@example.com")
                .password("encodedPassword")
                .idRol("1")
                .intentos(0)
                .build();

        when(usuarioRepository.findByCorreoElectronico(credenciales.getEmail()))
                .thenReturn(Mono.just(usuario));
        when(passwordEncoderRepository.matches(credenciales.getPassword(), usuario.getPassword()))
                .thenReturn(true);
        when(tokenRepository.generarToken(anyString(), anyString()))
                .thenReturn(Mono.just("fakeToken"));

        StepVerifier.create(jwtUseCase.login(credenciales))
                .expectNext("fakeToken")
                .verifyComplete();

        verify(usuarioRepository).findByCorreoElectronico(credenciales.getEmail());
        verify(tokenRepository).generarToken(usuario.getCorreoElectronico(), "ADMIN");
        verify(usuarioRepository, never()).saveUsuario(any());
    }


    @Test
    void loginUsuarioNoEncontradoDebeLanzarExcepcion() {
        usuario = Usuario.builder()
                .idusuario(1L)
                .nombre("Carlos")
                .apellido("Pérez")
                .correoElectronico("carlos.perez@example.com")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .direccion("Calle 123 #45-67")
                .telefono("3001234567")
                .salarioBase(new BigInteger("2500000"))
                .documentoIdentidad("123456789")
                .password("encodedPassword")
                .idRol("1") // ADMIN
                .intentos(0)
                .build();
        when(usuarioRepository.findByCorreoElectronico("noExiste@example.com")).thenReturn(Mono.empty());

        StepVerifier.create(jwtUseCase.login(new TokenCredenciales("noExiste@example.com", "password123")))
                .expectError(UsuarioNoEncontradoEmailException.class)
                .verify();
    }

    @Test
    void loginPasswordInvalidaDebeIncrementarIntentos() {
        usuario = Usuario.builder()
                .idusuario(1L)
                .nombre("Carlos")
                .apellido("Pérez")
                .correoElectronico("carlos.perez@example.com")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .direccion("Calle 123 #45-67")
                .telefono("3001234567")
                .salarioBase(new BigInteger("2500000"))
                .documentoIdentidad("123456789")
                .password("encodedPassword")
                .idRol("1") // ADMIN
                .intentos(0)
                .build();
        usuario.setIntentos(1);
        when(usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico())).thenReturn(Mono.just(usuario));
        when(passwordEncoderRepository.matches("wrongPassword", "encodedPassword")).thenReturn(false);
        when(usuarioRepository.saveUsuario(any(Usuario.class))).thenReturn(Mono.just(usuario));

        StepVerifier.create(jwtUseCase.login(new TokenCredenciales("carlos.perez@example.com", "wrongPassword")))
                .expectError(CredencialesInvalidasException.class)
                .verify();

        verify(usuarioRepository).saveUsuario(any(Usuario.class)); // guarda intentos incrementados
    }

    @Test
    void loginCuentaBloqueadaDebeLanzarExcepcion() {
        usuario = Usuario.builder()
                .idusuario(1L)
                .nombre("Carlos")
                .apellido("Pérez")
                .correoElectronico("carlos.perez@example.com")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .direccion("Calle 123 #45-67")
                .telefono("3001234567")
                .salarioBase(new BigInteger("2500000"))
                .documentoIdentidad("123456789")
                .password("encodedPassword")
                .idRol("1") // ADMIN
                .intentos(0)
                .build();
        usuario.setIntentos(3);
        when(usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico())).thenReturn(Mono.just(usuario));

        StepVerifier.create(jwtUseCase.login(new TokenCredenciales("carlos.perez@example.com", "password123")))
                .expectError(CuentaBloqueadaException.class)
                .verify();

        verify(usuarioRepository, never()).saveUsuario(any());
    }

    @Test
    void loginExitosoDebeGenerarTokenParaUsuarioConRolUser() {
        TokenCredenciales credenciales = new TokenCredenciales("maria@example.com", "password123");

        Usuario usuario = Usuario.builder()
                .correoElectronico("maria@example.com")
                .password("encodedPassword")
                .idRol("2")
                .intentos(0)
                .build();

        when(usuarioRepository.findByCorreoElectronico(credenciales.getEmail()))
                .thenReturn(Mono.just(usuario));
        when(passwordEncoderRepository.matches(credenciales.getPassword(), usuario.getPassword()))
                .thenReturn(true);
        when(tokenRepository.generarToken(anyString(), anyString()))
                .thenReturn(Mono.just("fakeTokenUser"));

        StepVerifier.create(jwtUseCase.login(credenciales))
                .expectNext("fakeTokenUser")
                .verifyComplete();

        verify(tokenRepository).generarToken(usuario.getCorreoElectronico(), "USER");
    }

    @Test
    void debeConstruirTokenInvalidoException() {
        TokenInvalidoException ex = new TokenInvalidoException("Token inválido");
        assertEquals("Token inválido", ex.getMessage());
    }

    @Test
    void debeConstruirUsuarioNotFoundException() {
        UsuarioNotFoundException ex = new UsuarioNotFoundException("123");
        assertTrue(ex.getMessage().contains("123"));
    }

}