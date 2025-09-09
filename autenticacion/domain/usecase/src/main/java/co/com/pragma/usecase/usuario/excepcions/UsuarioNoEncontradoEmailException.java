package co.com.pragma.usecase.usuario.excepcions;

public class UsuarioNoEncontradoEmailException extends RuntimeException {
    public UsuarioNoEncontradoEmailException(String email) {
        super("Usuario con email " + email + " no encontrado");
    }
}
