package com.darwin.techservice.application.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface ITechnologyHandler {
    Mono<ServerResponse> create(ServerRequest request);

    Mono<ServerResponse> findAllOrderedByName(ServerRequest request);
    Mono<ServerResponse> findById(ServerRequest request);
    Mono<ServerResponse> existsById(ServerRequest request);
}
