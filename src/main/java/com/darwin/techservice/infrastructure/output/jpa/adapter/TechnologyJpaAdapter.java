package com.darwin.techservice.infrastructure.output.jpa.adapter;

import com.darwin.techservice.domain.model.Technology;
import com.darwin.techservice.domain.spi.ITechnologyPersistencePort;
import com.darwin.techservice.infrastructure.output.jpa.mapper.TechnologyEntityMapper;
import com.darwin.techservice.infrastructure.output.jpa.repository.ITechnologyRepository;
import reactor.core.publisher.Mono;

public class TechnologyJpaAdapter implements ITechnologyPersistencePort {
    private final ITechnologyRepository technologyRepository;
    private final TechnologyEntityMapper technologyEntityMapper;

    public TechnologyJpaAdapter(ITechnologyRepository technologyRepository,
                                TechnologyEntityMapper technologyEntityMapper) {
        this.technologyRepository = technologyRepository;
        this.technologyEntityMapper = technologyEntityMapper;
    }

    @Override
    public Mono<Technology> createTechnology(Technology technology) {
        return technologyRepository.save(technologyEntityMapper.toEntity(technology))
                .map(technologyEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsTechnology(String name) {
        return technologyRepository.existsByName(name);
    }
}
