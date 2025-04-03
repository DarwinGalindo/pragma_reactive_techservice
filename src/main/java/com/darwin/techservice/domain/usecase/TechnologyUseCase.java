package com.darwin.techservice.domain.usecase;

import com.darwin.techservice.domain.api.ITechnologyServicePort;
import com.darwin.techservice.domain.exception.TechnologyNameAlreadyExistsException;
import com.darwin.techservice.domain.exception.TechnologyNotFoundException;
import com.darwin.techservice.domain.model.Technology;
import com.darwin.techservice.domain.spi.ITechnologyPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TechnologyUseCase implements ITechnologyServicePort {
    private final ITechnologyPersistencePort technologyPersistencePort;

    public TechnologyUseCase(ITechnologyPersistencePort technologyPersistencePort) {
        this.technologyPersistencePort = technologyPersistencePort;
    }

    @Override
    public Mono<Technology> createTechnology(Technology technology) {
        return technologyPersistencePort
                .existsTechnology(technology.getName())
                .flatMap(exists -> exists.equals(Boolean.TRUE) ? Mono.error(new TechnologyNameAlreadyExistsException())
                        : technologyPersistencePort.createTechnology(technology));
    }

    @Override
    public Flux<Technology> findAllOrderedByName(int page, int size, boolean ascending) {
        return technologyPersistencePort
                .findAllOrderedByName(page, size, ascending);
    }

    @Override
    public Mono<Technology> findById(Long id) {
        return technologyPersistencePort.findById(id)
                .switchIfEmpty(Mono.error(new TechnologyNotFoundException()));
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return technologyPersistencePort.existsById(id);
    }
}
