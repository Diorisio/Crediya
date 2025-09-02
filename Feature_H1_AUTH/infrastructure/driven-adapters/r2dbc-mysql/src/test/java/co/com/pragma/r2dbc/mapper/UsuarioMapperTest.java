package co.com.pragma.r2dbc.mapper;

import co.com.pragma.api.mapper.UsuarioEntityMapper;
import co.com.pragma.model.usuario.Usuario;
import co.com.pragma.r2dbc.entities.UsuarioEntity;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UsuarioMapperTest {

    @Test
    void privateConstructorIsInvokedTest() throws Exception {
        Constructor<UsuarioEntityMapper> constructor = UsuarioEntityMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThatCode(constructor::newInstance).doesNotThrowAnyException();
    }

    @Test
    void toDomainMapsAllFieldsTest() {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setNombre("John");
        entity.setApellido("Doe");
        entity.setCorreoElectronico("john@doe.com");
        entity.setSalarioBase(1000.0);
        entity.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        entity.setDireccion("Address");
        entity.setTelefono("123456789");

        Usuario usuario = UsuarioEntityMapper.toDomain(entity);

        assertEquals("John", usuario.nombre());
        assertEquals("Doe", usuario.apellido());
        assertEquals("john@doe.com", usuario.correoElectronico());
        assertEquals(1000.0, usuario.salarioBase());
        assertEquals(LocalDate.of(1990, 1, 1), usuario.fechaNacimiento());
        assertEquals("Address", usuario.direccion());
        assertEquals("123456789", usuario.telefono());
    }

    @Test
    void toEntityMapsAllFieldsTest() {
        Usuario usuario = new Usuario(
                "Jane", "Smith", "jane@smith.com", 2000.0,
                LocalDate.of(1985, 5, 20), "Street", "987654321"
        );
        UsuarioDto usuarioDto = usuarioToDto(usuario, 2L);

        UsuarioEntity entity = UsuarioMapper.toEntity(usuarioDto);

        assertEquals("Jane", entity.getNombre());
        assertEquals("Smith", entity.getApellido());
        assertEquals("jane@smith.com", entity.getEmail());
        assertEquals(2000.0, entity.getSalarioBase());
        assertEquals(LocalDate.of(1985, 5, 20), entity.getFechaNacimiento());
        assertEquals("Street", entity.getDireccion());
        assertEquals("987654321", entity.getTelefono());
    }

    @Test
    void toDomainHandlesNullFields() {
        UsuarioEntity entity = new UsuarioEntity();
        Usuario usuario = UsuarioMapper.toDomain(entity);

        assertNull(usuario.nombre());
        assertNull(usuario.apellido());
        assertNull(usuario.correoElectronico());
        assertNull(usuario.salarioBase());
        assertNull(usuario.fechaNacimiento());
        assertNull(usuario.direccion());
        assertNull(usuario.telefono());
    }

    @Test
    void toEntityHandlesNullFields() {
        Usuario usuario = new Usuario(null, null, null, null, null, null, null);
        UsuarioDto usuarioDto = usuarioToDto(usuario, 2L);

        UsuarioEntity entity = UsuarioMapper.toEntity(usuarioDto);

        assertNull(entity.getNombre());
        assertNull(entity.getApellido());
        assertNull(entity.getEmail());
        assertNull(entity.getSalarioBase());
        assertNull(entity.getFechaNacimiento());
        assertNull(entity.getDireccion());
        assertNull(entity.getTelefono());
    }
}
