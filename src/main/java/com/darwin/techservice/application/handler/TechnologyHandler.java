package com.darwin.techservice.application.handler;

import com.darwin.techservice.application.dto.TechnologyRequest;
import com.darwin.techservice.application.mapper.TechnologyDtoMapper;
import com.darwin.techservice.application.util.ValidatorUtil;
import com.darwin.techservice.domain.api.ITechnologyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TechnologyHandler implements ITechnologyHandler {
    private final ITechnologyServicePort technologyServicePort;
    private final TechnologyDtoMapper technologyDtoMapper;
    private final Validator validator;

    @Override
    public Mono<ServerResponse> createTechnology(ServerRequest request) {
        return request.bodyToMono(TechnologyRequest.class)
                .doOnNext(technologyRequest -> ValidatorUtil.validate(validator, technologyRequest))
                .map(technologyDtoMapper::toModel)
                .flatMap(technologyServicePort::createTechnology)
                .flatMap(technology -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(technologyDtoMapper.toResponse(technology)));
    }

    @Override
    public Mono<ServerResponse> findAllOrderedByName(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));

        return technologyServicePort.findAllOrderedByName(page, size, true)
                .collectList()
                .flatMap(response -> ServerResponse.ok().bodyValue(technologyDtoMapper.toResponseList(response)));

    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        return technologyServicePort.findById(Long.valueOf(request.pathVariable("id")))
                .flatMap(technology -> ServerResponse.ok()
                        .bodyValue(technologyDtoMapper.toResponse(technology)));
    }
}
