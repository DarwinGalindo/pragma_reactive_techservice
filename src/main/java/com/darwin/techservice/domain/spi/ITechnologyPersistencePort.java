package com.darwin.techservice.domain.spi;

import com.darwin.techservice.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyPersistencePort {
    Mono<Technology> create(Technology technology);

    Mono<Boolean> existsTechnology(String name);
    Flux<Technology> findAllOrderedByName(int page, int size, boolean ascending);
    Mono<Technology> findById(Long id);
    Mono<Boolean> existsById(Long id);
}
