package co.com.pragma.api;

import co.com.pragma.api.dto.ListaSolicitudesResponse;
import co.com.pragma.api.dto.RequestSolicitud;
import co.com.pragma.model.solicitud.Solicitud;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "registrarSolicitud",
                    operation = @Operation(
                            operationId = "save",
                            summary = "Registrar una nueva solicitud",
                            description = "Crear la solicitud con la informacion suministrada",
                            tags = {"Solicitud"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Peticion del registro de la solicitud",
                                    content = @Content(schema = @Schema(implementation = RequestSolicitud.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                                            content = @Content(schema = @Schema(implementation = RequestSolicitud.class)))
                            }
                    )
            ),

                    @RouterOperation(
                            path = "/api/v1/solicitudes",
                            produces = {"application/json"},
                            method = RequestMethod.GET,
                            beanClass = Handler.class,
                            beanMethod = "listenGETListar",
                            operation = @Operation(
                                    operationId = "listarSolicitudes",
                                    summary = "Listar solicitudes",
                                    description = "Obtiene la lista de solicitudes filtradas por estado, email, identificación y paginación",
                                    tags = {"Solicitud"},
                                    parameters = {
                                            @Parameter(name = "estadoId", in = ParameterIn.QUERY, description = "ID del estado", required = false),
                                            @Parameter(name = "page", in = ParameterIn.QUERY, description = "Número de página", required = false),
                                            @Parameter(name = "size", in = ParameterIn.QUERY, description = "Tamaño de página", required = false),
                                            @Parameter(name = "email", in = ParameterIn.QUERY, description = "Correo electrónico del usuario", required = false),
                                            @Parameter(name = "identificacion", in = ParameterIn.QUERY, description = "Identificación del usuario", required = false)
                                    },
                                    responses = {
                                            @ApiResponse(
                                                    responseCode = "200",
                                                    description = "Lista de solicitudes devuelta correctamente",
                                                    content = @Content(schema = @Schema(implementation = ListaSolicitudesResponse.class))
                                            )
                                    }
                            )
                    ),
            @RouterOperation(
                    path = "/api/v1/solicitudes/{id}",
                    produces = {"application/json"},
                    method = RequestMethod.PUT,
                    beanClass = Handler.class,
                    beanMethod = "listenPUTactualizar",
                    operation = @Operation(
                            operationId = "actualizarSolicitud",
                            summary = "Actualizar estado de solicitud",
                            description = "Actualiza el estado de una solicitud específica según el ID proporcionado ",
                            tags = {"Solicitud"},
                            parameters = {
                                    @Parameter(
                                            name = "id",
                                            in = ParameterIn.PATH,
                                            description = "ID de la solicitud a actualizar",
                                            required = true
                                    )
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Datos de actualización de la solicitud",
                                    content = @Content(schema = @Schema(implementation = RequestSolicitud.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud actualizada correctamente",
                                            content = @Content(schema = @Schema(implementation = Solicitud.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Solicitud no encontrada"
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Solicitud inválida"
                                    )
                            }
                    )
            )


    })

    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/solicitud"), handler::registrarSolicitud)
                .andRoute(GET("/api/v1/solicitud"), handler::listenGETListar)
                .andRoute(PUT("/api/v1/solicitud/{id}"), handler::listenPUTactualizar);


    }
}
