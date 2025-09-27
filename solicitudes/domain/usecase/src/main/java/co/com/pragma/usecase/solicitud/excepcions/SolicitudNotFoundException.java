package co.com.pragma.usecase.solicitud.excepcions;



public class SolicitudNotFoundException extends RuntimeException {
    public SolicitudNotFoundException(Long idSolicitud) {
        super("Solicituda con id " + idSolicitud + " no existe");
    }
}
