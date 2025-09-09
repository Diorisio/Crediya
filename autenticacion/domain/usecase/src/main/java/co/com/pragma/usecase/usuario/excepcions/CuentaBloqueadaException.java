package co.com.pragma.usecase.usuario.excepcions;

public class CuentaBloqueadaException extends RuntimeException {
    public CuentaBloqueadaException(String email) {
        super("Cuenta bloqueada para el usuario con email " + email);
    }
}
