package co.com.pragma.usecase.usuario.excepcions;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String documento) {
        super("Usuario con documento " + documento + " no existe");
    }
}
