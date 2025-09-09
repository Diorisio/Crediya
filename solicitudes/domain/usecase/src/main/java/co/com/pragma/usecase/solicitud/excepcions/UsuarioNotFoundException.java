package co.com.pragma.usecase.solicitud.excepcions;

import java.math.BigInteger;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(BigInteger documento) {
        super("Usuario con documento " + documento + " no existe");
    }
}
