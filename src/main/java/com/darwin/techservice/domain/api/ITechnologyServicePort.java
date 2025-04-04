package com.darwin.techservice.domain.api;

import com.darwin.techservice.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyServicePort {
    Mono<Technology> create(Technology technology);
    Flux<Technology> findAllOrderedByName(int page, int size, boolean ascending);
    Mono<Technology> findById(Long id);
    Mono<Boolean> existsById(Long id);
}
