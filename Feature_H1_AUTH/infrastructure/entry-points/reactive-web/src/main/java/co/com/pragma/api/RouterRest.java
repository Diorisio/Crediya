package co.com.pragma.api;

import co.com.pragma.api.dto.RequestGuardarUsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {


    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenPOSTGuardarCase",
                    operation = @Operation(
                            operationId = "saveUsuario",
                            summary = "Registrar un nuevo usuario",
                            description = "Crear un usuario con la informacion suministrada",
                            tags = {"Usuario"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Peticion del registro de usuario",
                                    content = @Content(schema = @Schema(implementation = RequestGuardarUsuarioDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                                            content = @Content(schema = @Schema(implementation = RequestGuardarUsuarioDto.class)))
                            }
                    )
            )
    })


    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::listenPOSTGuardarCase);
    }


}
