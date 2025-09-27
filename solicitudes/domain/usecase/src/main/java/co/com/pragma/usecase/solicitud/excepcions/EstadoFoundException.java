package co.com.pragma.usecase.solicitud.excepcions;

public class EstadoFoundException extends RuntimeException {
    public EstadoFoundException(String estado) {
        super("La solicitud ya se encuentra en el estado: "+ estado);
    }
}
