package co.com.pragma.usecase.usuario.excepcions;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(int intentos) {
        super("Credenciales inválidas. Intentos: " + intentos);
    }
}




