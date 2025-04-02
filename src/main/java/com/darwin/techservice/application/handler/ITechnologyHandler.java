package com.darwin.techservice.application.handler;

import com.darwin.techservice.application.dto.TechnologyRequest;
import com.darwin.techservice.application.dto.TechnologyResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ITechnologyHandler {
    Mono<ServerResponse> createTechnology(ServerRequest request);
}
