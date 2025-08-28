package co.com.pragma.api;

import co.com.pragma.api.dto.RequestGuardarUsuarioDto;
import co.com.pragma.api.mapper.UsuarioEntityMapper;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.usecase.usuario.UsuarioUseCase;
import co.com.pragma.usecase.usuario.excepcions.EmailAlreadyRegisteredException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UsuarioUseCase userUseCase;

    @MockBean
    private UsuarioEntityMapper userDtoMapper;

    @Test
    void GuardarUser_Created() {
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

        when(userDtoMapper.toDomain(ArgumentMatchers.any(RequestGuardarUsuarioDto.class))).thenReturn(domain);
        when(userUseCase.saveUsuario(domain));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Juan")
                .jsonPath("$.correoElectronico").isEqualTo("juan.perez@example.com");
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
        Usuario domain = new Usuario(
                "Carlos",
                "Pérez",
                "carlos.perez@example.com",
                LocalDate.of(1990, 5, 20),
                "Calle 123 #45-67",
                "3001234567",
                new BigInteger("2500000")
        );

        when(userDtoMapper.toDomain(ArgumentMatchers.any(RequestGuardarUsuarioDto.class))).thenReturn(domain);
        when(userUseCase.saveUsuario(domain)).thenReturn(Mono.error(new EmailAlreadyRegisteredException("carlos.perez@example.com")));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.code").isEqualTo("DOM-003")
                .jsonPath("$.email").isEqualTo("carlos.perez@example.com");
    }
}
