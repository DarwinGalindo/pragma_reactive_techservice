package com.darwin.techservice.application.handler;

import com.darwin.techservice.application.dto.TechnologyRequest;
import com.darwin.techservice.application.dto.TechnologyResponse;
import com.darwin.techservice.application.mapper.TechnologyRequestMapper;
import com.darwin.techservice.application.util.ValidatorUtil;
import com.darwin.techservice.domain.api.ITechnologyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TechnologyHandler implements ITechnologyHandler {
    private final ITechnologyServicePort technologyServicePort;
    private final TechnologyRequestMapper technologyRequestMapper;
    private final Validator validator;

    @Override
    public Mono<ServerResponse> createTechnology(ServerRequest request) {
        return request.bodyToMono(TechnologyRequest.class)
                .doOnNext(technologyRequest -> ValidatorUtil.validate(validator, technologyRequest))
                .map(technologyRequestMapper::toModel)
                .flatMap(technologyServicePort::createTechnology)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED).bodyValue(response));
    }

    @Override
    public Mono<ServerResponse> findAllOrderedByName(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));

        return technologyServicePort.findAllOrderedByName(page, size, true)
                .collectList()
                .flatMap(response -> ServerResponse.ok().bodyValue(technologyRequestMapper.toResponseList(response)));

    }
}
