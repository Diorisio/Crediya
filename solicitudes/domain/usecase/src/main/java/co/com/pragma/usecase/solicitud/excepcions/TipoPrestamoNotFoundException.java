package co.com.pragma.usecase.solicitud.excepcions;

import java.math.BigInteger;

public class TipoPrestamoNotFoundException  extends RuntimeException{
    public TipoPrestamoNotFoundException(Long idTipoPrestamo) {
        super("El tipo de prestamo " + idTipoPrestamo + " no existe");
    }
}
