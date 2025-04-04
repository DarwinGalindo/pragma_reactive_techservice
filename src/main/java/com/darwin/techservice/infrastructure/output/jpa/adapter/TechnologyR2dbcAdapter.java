package com.darwin.techservice.infrastructure.output.jpa.adapter;

import com.darwin.techservice.domain.model.Technology;
import com.darwin.techservice.domain.spi.ITechnologyPersistencePort;
import com.darwin.techservice.infrastructure.output.jpa.mapper.TechnologyEntityMapper;
import com.darwin.techservice.infrastructure.output.jpa.repository.ITechnologyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TechnologyR2dbcAdapter implements ITechnologyPersistencePort {
    private static final String NAME_PROPERTY = "name";

    private final ITechnologyRepository technologyRepository;
    private final TechnologyEntityMapper technologyEntityMapper;

    public TechnologyR2dbcAdapter(ITechnologyRepository technologyRepository,
                                  TechnologyEntityMapper technologyEntityMapper) {
        this.technologyRepository = technologyRepository;
        this.technologyEntityMapper = technologyEntityMapper;
    }

    @Override
    public Mono<Technology> create(Technology technology) {
        return technologyRepository
                .save(technologyEntityMapper.toEntity(technology))
                .map(technologyEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsTechnology(String name) {
        return technologyRepository.existsByName(name);
    }

    @Override
    public Flux<Technology> findAllOrderedByName(int page, int size, boolean ascending) {
        Sort sort = Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, NAME_PROPERTY);
        Pageable pageable = PageRequest.of(page, size, sort);

        return technologyRepository.findBy(pageable)
                .map(technologyEntityMapper::toModel);
    }

    @Override
    public Mono<Technology> findById(Long id) {
        return technologyRepository.findById(id)
                .map(technologyEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return technologyRepository.existsById(id);
    }
}
