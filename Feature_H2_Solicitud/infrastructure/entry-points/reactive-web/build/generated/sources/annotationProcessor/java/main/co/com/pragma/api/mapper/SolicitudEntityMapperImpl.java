package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.RequestSolicitud;
import co.com.pragma.model.solicitud.Solicitud;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-04T18:47:12-0500",
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

        solicitud.identificacion( solicitudEntity.getIdentificacion() );
        solicitud.monto( solicitudEntity.getMonto() );
        solicitud.plazo( solicitudEntity.getPlazo() );
        solicitud.idTipoPrestamo( solicitudEntity.getIdTipoPrestamo() );
        solicitud.correoElectronico( solicitudEntity.getCorreoElectronico() );

        return solicitud.build();
    }

    @Override
    public Solicitud toDomain(RequestSolicitud solicitud) {
        if ( solicitud == null ) {
            return null;
        }

        Solicitud.SolicitudBuilder solicitud1 = Solicitud.builder();

        solicitud1.identificacion( solicitud.identificacion() );
        solicitud1.monto( solicitud.monto() );
        solicitud1.plazo( solicitud.plazo() );
        solicitud1.idTipoPrestamo( solicitud.idTipoPrestamo() );
        solicitud1.correoElectronico( solicitud.correoElectronico() );

        return solicitud1.build();
    }
}
