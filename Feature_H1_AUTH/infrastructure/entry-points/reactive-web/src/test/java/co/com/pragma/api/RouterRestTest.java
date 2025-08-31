package co.com.pragma.api;

import co.com.pragma.api.dto.RequestGuardarUsuarioDto;
import co.com.pragma.api.mapper.UsuarioEntityMapper;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.usuario.UsuarioUseCase;
import co.com.pragma.usecase.usuario.excepcions.EmailAlreadyRegisteredException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UsuarioUseCase userUseCase;

    @MockBean
    private UsuarioEntityMapper userDtoMapper;

    @Test
    void GuardarUser_Created() {
        // Arrange (datos de entrada)
        RequestGuardarUsuarioDto request = new RequestGuardarUsuarioDto(
                "Carlos",
                "Pérez",
                "carlos.perez@example.com",
                "1990-05-20",
                "Calle 123 #45-67",
                "3001234567",
                new BigInteger("2500000")
        );

        Usuario domain = new Usuario(
                "Carlos",
                "Pérez",
                "carlos.perez@example.com",
                LocalDate.of(1990, 5, 20),
                "Calle 123 #45-67",
                "3001234567",
                new BigInteger("2500000")
        );

        // Mock: cuando el mapper recibe cualquier DTO, devuelve el domain
        when(userDtoMapper.toDomain(any(RequestGuardarUsuarioDto.class))).thenReturn(domain);

        // Mock: cuando el caso de uso guarda, devuelve el mismo usuario
        when(userUseCase.saveUsuario(domain)).thenReturn(Mono.just(domain));

        // Act & Assert (ejecución + validaciones)
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Carlos")
                .jsonPath("$.apellido").isEqualTo("Pérez")
                .jsonPath("$.correoElectronico").isEqualTo("carlos.perez@example.com")
                .jsonPath("$.direccion").isEqualTo("Calle 123 #45-67")
                .jsonPath("$.telefono").isEqualTo("3001234567")
                .jsonPath("$.salarioBase").isEqualTo(2500000);
    }

    @Test
    void saveUser_EmailAlreadyRegistered_ReturnsConflict() {
        RequestGuardarUsuarioDto request = new RequestGuardarUsuarioDto(
                "Carlos",
                "Pérez",
                "carlos.perez@example.com",
                "1990-05-20",
                "Calle 123 #45-67",
                "3001234567",
                new BigInteger("2500000")
        );

        when(userDtoMapper.toDomain(any(RequestGuardarUsuarioDto.class)))
                .thenReturn(new Usuario(
                        "Carlos", "Pérez", "carlos.perez@example.com",
                        LocalDate.of(1990, 5, 20),
                        "Calle 123 #45-67", "3001234567", new BigInteger("2500000")
                ));

        when(userUseCase.saveUsuario(any(Usuario.class)))
                .thenReturn(Mono.error(new EmailAlreadyRegisteredException("carlos.perez@example.com")));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(String.class) // <-- en lugar de jsonPath
                .consumeWith(result -> {
                    System.out.println("⚡ Response body: " + result.getResponseBody());
                });

    }
}
