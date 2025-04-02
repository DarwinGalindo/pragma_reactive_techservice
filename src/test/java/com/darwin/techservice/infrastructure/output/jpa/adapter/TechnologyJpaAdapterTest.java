package com.darwin.techservice.infrastructure.output.jpa.adapter;

import com.darwin.techservice.domain.model.Technology;
import com.darwin.techservice.infrastructure.output.jpa.entity.TechnologyEntity;
import com.darwin.techservice.infrastructure.output.jpa.mapper.TechnologyEntityMapper;
import com.darwin.techservice.infrastructure.output.jpa.repository.ITechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TechnologyJpaAdapterTest {

    private ITechnologyRepository technologyRepository;
    private TechnologyEntityMapper technologyEntityMapper;
    private TechnologyJpaAdapter technologyJpaAdapter;

    @BeforeEach
    void setUp() {
        technologyRepository = mock(ITechnologyRepository.class);
        technologyEntityMapper = mock(TechnologyEntityMapper.class);
        technologyJpaAdapter = new TechnologyJpaAdapter(technologyRepository, technologyEntityMapper);
    }

    @Test
    void createTechnology_ShouldSaveAndReturnTechnology() {
        Technology technology = new Technology(1L, "Java", "Programming Language");
        TechnologyEntity technologyEntity = new TechnologyEntity();
        technologyEntity.setId(1L);
        technologyEntity.setName("Java");

        when(technologyEntityMapper.toEntity(technology)).thenReturn(technologyEntity);
        when(technologyRepository.save(technologyEntity)).thenReturn(Mono.just(technologyEntity));
        when(technologyEntityMapper.toModel(technologyEntity)).thenReturn(technology);

        StepVerifier.create(technologyJpaAdapter.createTechnology(technology))
                .expectNext(technology)
                .verifyComplete();

        verify(technologyEntityMapper).toEntity(technology);
        verify(technologyRepository).save(technologyEntity);
        verify(technologyEntityMapper).toModel(technologyEntity);
    }

    @Test
    void existsTechnology_ShouldReturnTrueWhenNameExists() {
        String techName = "Java";

        when(technologyRepository.existsByName(techName)).thenReturn(Mono.just(true));

        StepVerifier.create(technologyJpaAdapter.existsTechnology(techName))
                .expectNext(true)
                .verifyComplete();

        verify(technologyRepository).existsByName(techName);
    }

    @Test
    void existsTechnology_ShouldReturnFalseWhenNameDoesNotExist() {
        String techName = "Java";

        when(technologyRepository.existsByName(techName)).thenReturn(Mono.just(false));

        StepVerifier.create(technologyJpaAdapter.existsTechnology(techName))
                .expectNext(false)
                .verifyComplete();

        verify(technologyRepository).existsByName(techName);
    }
}