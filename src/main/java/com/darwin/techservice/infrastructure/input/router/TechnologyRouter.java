package com.darwin.techservice.infrastructure.input.router;

import com.darwin.techservice.application.handler.ITechnologyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.darwin.techservice.infrastructure.util.Routes.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class TechnologyRouter {

    private final ITechnologyHandler technologyHandler;

    @Bean
    public RouterFunction<ServerResponse> technologyRoutes() {
        return route()
                .POST(TECHNOLOGY_RESOURCE, technologyHandler::createTechnology)
                .GET(TECHNOLOGY_RESOURCE, technologyHandler::findAllOrderedByName)
                .GET(TECHNOLOGY_RESOURCE_ID, technologyHandler::findById)
                .GET(TECHNOLOGY_RESOURCE_ID_EXISTS, technologyHandler::existsById)
                .build();
    }

}
