package com.darwin.techservice.application.handler;

import com.darwin.techservice.application.dto.TechnologyRequest;
import com.darwin.techservice.application.mapper.TechnologyDtoMapper;
import com.darwin.techservice.application.util.LocalValidator;
import com.darwin.techservice.domain.api.ITechnologyServicePort;
import com.darwin.techservice.shared.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TechnologyHandler implements ITechnologyHandler {
    private final ITechnologyServicePort technologyServicePort;
    private final TechnologyDtoMapper technologyDtoMapper;
    private final LocalValidator validator;

    @Override
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(TechnologyRequest.class)
                .doOnNext(validator::validate)
                .map(technologyDtoMapper::toModel)
                .flatMap(technologyServicePort::create)
                .flatMap(technology -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(technologyDtoMapper.toResponse(technology)));
    }

    @Override
    public Mono<ServerResponse> findAllOrderedByName(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam(Pagination.PAGE_PARAM).orElse(Pagination.DEFAULT_PAGE));
        int size = Integer.parseInt(request.queryParam(Pagination.SIZE_PARAM).orElse(Pagination.DEFAULT_SIZE));
        boolean sortAscending = request.queryParam(Pagination.SORT_ASCENDING).orElse(Pagination.DEFAULT_ASCENDING)
                .equals(Pagination.ASCENDING_TRUE);

        return technologyServicePort.findAllOrderedByName(page, size, sortAscending)
                .collectList()
                .flatMap(response -> ServerResponse.ok().bodyValue(technologyDtoMapper.toResponseList(response)));

    }

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        return technologyServicePort.findById(Long.valueOf(request.pathVariable("id")))
                .flatMap(technology -> ServerResponse.ok()
                        .bodyValue(technologyDtoMapper.toResponse(technology)));
    }

    @Override
    public Mono<ServerResponse> existsById(ServerRequest request) {
        return technologyServicePort.existsById(Long.parseLong(request.pathVariable("id")))
                .flatMap(exist -> ServerResponse.ok().bodyValue(exist));
    }
}
