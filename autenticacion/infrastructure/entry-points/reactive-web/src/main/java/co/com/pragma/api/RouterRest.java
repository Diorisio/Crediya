package co.com.pragma.api;

import co.com.pragma.api.dto.AuthRequestDto;
import co.com.pragma.api.dto.AuthResponseDto;
import co.com.pragma.api.dto.RequestGuardarUsuarioDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {


    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {"application/json"},
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenPOSTGuardarCase",
                    operation = @Operation(
                            operationId = "saveUsuario",
                            summary = "Registrar un nuevo usuario",
                            description = "Crear un usuario con la informacion suministrada",
                            tags = {"Usuario"},
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                                            content = @Content(schema = @Schema(implementation = RequestGuardarUsuarioDto.class)))
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/usuarios/{documento}",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenGETBuscarUsuario",
                    operation = @Operation(
                            operationId = "existsBydocumentoIdentidad",
                            summary = "Buscar un usuario",
                            description = "Buscar un usuario por documento",
                            tags = {"Usuario"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Peticion del login",
                                    content = @Content(schema = @Schema(implementation = AuthRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "logeado exitosamente",
                                            content = @Content(schema = @Schema(implementation = AuthRequestDto.class)))
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = Handler.class, // tu clase donde está listenPOSTgenerarToken
                    beanMethod = "listenPOSTgenerarToken",
                    operation = @Operation(
                            operationId = "generarToken",
                            summary = "Generar un token JWT",
                            description = "Recibe credenciales del usuario y retorna un token JWT si son válidas",
                            tags = {"Auth"},
                            requestBody = @RequestBody(
                                    required = true,
                                    description = "Credenciales del usuario",
                                    content = @Content(schema = @Schema(implementation = AuthRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Token generado correctamente",
                                            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación en las credenciales"
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Credenciales inválidas"
                                    )
                            }
                    )
            )
    })




    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::listenPOSTGuardarCase)
                .andRoute(GET("/api/v1/usuarios/{documento}"), handler::listenGETBuscarUsuario)
                .andRoute(POST("/api/v1/login"), handler::listenPOSTgenerarToken);

    }


}
