package co.com.pragma.r2dbc;

import co.com.pragma.api.mapper.UsuarioEntityMapper;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    private MyReactiveRepository myReactiveRepository;
    
    private MyReactiveRepositoryAdapter adapter;

    @Mock
    UsuarioEntityMapper mapper;


    @BeforeEach
    void setUp() {
        myReactiveRepository = mock(MyReactiveRepository.class);
        adapter = new MyReactiveRepositoryAdapter(myReactiveRepository, (ObjectMapper) mapper);
    }

    @Test
    void saveUsuarioShouldReturnMappedUsuario() {
        // Arrange
        Usuario usuario = new Usuario(
                "Carlos", "Pérez", "carlos.perez@example.com",
                LocalDate.of(1990, 5, 20),
                "Calle 123 #45-67",
                "3001234567",
                new BigInteger("2500000")
        );

        UsuarioEntity entity = new UsuarioEntity(
                1L, "Carlos", "Pérez", "carlos.perez@example.com",
                LocalDate.of(1990, 5, 20),
                "Calle 123 #45-67",
                "3001234567",
                new BigInteger("2500000")
        );

        when(myReactiveRepository.save(any(UsuarioEntity.class)))
                .thenReturn(Mono.just(entity));

        // Act
        Usuario result = adapter.saveUsuario(usuario).block();

        // Assert
        assertNotNull(result);
        assertEquals("Carlos", result.getNombre());
        verify(myReactiveRepository).save(any(UsuarioEntity.class));
    }

    @Test
    void buscarEmailShouldReturnNullIfNotExistsTest() {
        when(myReactiveRepository.findAll()).thenReturn(Flux.empty());

        Usuario result = adapter.existsByCorreoElectronico("no@exists.com").block();
        assertNull(result);
    }



}
