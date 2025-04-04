package com.darwin.techservice.infrastructure.output.jpa.repository;

import com.darwin.techservice.infrastructure.output.jpa.entity.TechnologyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {
    Mono<Boolean> existsByName(String name);

    Flux<TechnologyEntity> findBy(Pageable pageable);
}
