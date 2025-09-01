package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.RequestSolicitud;
import co.com.pragma.model.solicitud.Solicitud;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-31T23:12:18-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class SolicitudEntityMapperImpl implements SolicitudEntityMapper {

    @Override
    public Solicitud toSolicitudEntity(Solicitud solicitudEntity) {
        if ( solicitudEntity == null ) {
            return null;
        }

        Solicitud.SolicitudBuilder solicitud = Solicitud.builder();

        solicitud.documentoIdentificacion( solicitudEntity.getDocumentoIdentificacion() );
        solicitud.monto( solicitudEntity.getMonto() );
        solicitud.plazo( solicitudEntity.getPlazo() );
        solicitud.tipoPrestamo( solicitudEntity.getTipoPrestamo() );
        solicitud.correoElectronico( solicitudEntity.getCorreoElectronico() );

        return solicitud.build();
    }

    @Override
    public Solicitud toDomain(RequestSolicitud solicitud) {
        if ( solicitud == null ) {
            return null;
        }

        Solicitud.SolicitudBuilder solicitud1 = Solicitud.builder();

        if ( solicitud.monto() != null ) {
            solicitud1.monto( String.valueOf( solicitud.monto() ) );
        }
        solicitud1.plazo( solicitud.plazo() );
        solicitud1.correoElectronico( solicitud.correoElectronico() );

        return solicitud1.build();
    }
}
