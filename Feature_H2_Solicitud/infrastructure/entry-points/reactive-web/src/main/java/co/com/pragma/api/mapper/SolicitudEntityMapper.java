package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.RequestSolicitud;
import co.com.pragma.model.solicitud.Solicitud;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;

@Component
@Mapper(componentModel = "Spring")
public interface SolicitudEntityMapper {

    Solicitud toSolicitudEntity(Solicitud solicitudEntity);

    Solicitud toDomain(RequestSolicitud solicitud);
}
