package com.darwin.techservice.infrastructure.input.router;

import com.darwin.techservice.application.dto.TechnologyRequest;
import com.darwin.techservice.application.dto.TechnologyResponse;
import com.darwin.techservice.application.handler.ITechnologyHandler;
import com.darwin.techservice.shared.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.darwin.techservice.infrastructure.util.Routes.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class TechnologyRouter {

    private final ITechnologyHandler technologyHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = TECHNOLOGY_RESOURCE,
                    method = RequestMethod.POST,
                    beanClass = ITechnologyHandler.class,
                    beanMethod = "create",
                    operation = @Operation(
                            summary = "Crea una nueva tecnología",
                            operationId = "create",
                            requestBody = @RequestBody(
                                    description = "Tecnología a crear",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = TechnologyRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Operación exitosa",
                                            content = @Content(schema = @Schema(implementation = TechnologyResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error en la solicitud",
                                            content = @Content
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "El nombre de la tecnología ya está en uso",
                                            content = @Content
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = TECHNOLOGY_RESOURCE,
                    method = RequestMethod.GET,
                    beanClass = ITechnologyHandler.class,
                    beanMethod = "findAllOrderedByName",
                    operation = @Operation(
                            summary = "Lista las tecnologías ordenado por el nombre de forma paginada",
                            operationId = "findAllOrderedByName",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Operación exitosa",
                                            content = @Content(
                                                    array = @ArraySchema(schema = @Schema(implementation = TechnologyResponse.class)
                                                    ))
                                    )
                            },
                            parameters = {
                                    @Parameter(in = ParameterIn.QUERY, name = Pagination.PAGE_PARAM, example = Pagination.DEFAULT_PAGE),
                                    @Parameter(in = ParameterIn.QUERY, name = Pagination.SIZE_PARAM, example = Pagination.DEFAULT_SIZE),
                                    @Parameter(in = ParameterIn.QUERY, name = Pagination.SORT_ASCENDING, example = Pagination.DEFAULT_ASCENDING)
                            }
                    )
            ),
            @RouterOperation(
                    path = TECHNOLOGY_RESOURCE_ID,
                    method = RequestMethod.GET,
                    beanClass = ITechnologyHandler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            summary = "Obtiene una tecnología por su ID",
                            operationId = "findById",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Operación exitosa",
                                            content = @Content(schema = @Schema(implementation = TechnologyResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "ID no encontrado",
                                            content = @Content
                                    ),
                            },
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id", example = "1")
                            }
                    )
            ),
            @RouterOperation(
                    path = TECHNOLOGY_RESOURCE_ID_EXISTS,
                    method = RequestMethod.GET,
                    beanClass = ITechnologyHandler.class,
                    beanMethod = "existsById",
                    operation = @Operation(
                            summary = "Consulta si existe una tecnología por su ID",
                            operationId = "existsById",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Operación exitosa",
                                            content = @Content(schema = @Schema(implementation = Boolean.class))
                                    )
                            },
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id", example = "1")
                            }
                    )
            ),
    })
    public RouterFunction<ServerResponse> technologyRoutes() {
        return route()
                .POST(TECHNOLOGY_RESOURCE, technologyHandler::create)
                .GET(TECHNOLOGY_RESOURCE, technologyHandler::findAllOrderedByName)
                .GET(TECHNOLOGY_RESOURCE_ID, technologyHandler::findById)
                .GET(TECHNOLOGY_RESOURCE_ID_EXISTS, technologyHandler::existsById)
                .build();
    }

}
