package com.darwin.techservice.infrastructure.output.jpa.adapter;

import com.darwin.techservice.domain.model.Technology;
import com.darwin.techservice.infrastructure.output.jpa.entity.TechnologyEntity;
import com.darwin.techservice.infrastructure.output.jpa.mapper.TechnologyEntityMapper;
import com.darwin.techservice.infrastructure.output.jpa.repository.ITechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TechnologyR2DbcAdapterTest {

    private ITechnologyRepository technologyRepository;
    private TechnologyEntityMapper technologyEntityMapper;
    private TechnologyR2dbcAdapter technologyR2DbcAdapter;

    @BeforeEach
    void setUp() {
        technologyRepository = mock(ITechnologyRepository.class);
        technologyEntityMapper = mock(TechnologyEntityMapper.class);
        technologyR2DbcAdapter = new TechnologyR2dbcAdapter(technologyRepository, technologyEntityMapper);
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

        StepVerifier.create(technologyR2DbcAdapter.createTechnology(technology))
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

        StepVerifier.create(technologyR2DbcAdapter.existsTechnology(techName))
                .expectNext(true)
                .verifyComplete();

        verify(technologyRepository).existsByName(techName);
    }

    @Test
    void existsTechnology_ShouldReturnFalseWhenNameDoesNotExist() {
        String techName = "Java";

        when(technologyRepository.existsByName(techName)).thenReturn(Mono.just(false));

        StepVerifier.create(technologyR2DbcAdapter.existsTechnology(techName))
                .expectNext(false)
                .verifyComplete();

        verify(technologyRepository).existsByName(techName);
    }

    @Test
    void findAllOrderedByNameAsc_ShouldReturnAscending() {
        int page = 0;
        int size = 2;
        boolean ascending = true;
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, size, sort);

        TechnologyEntity technologyEntity1 = new TechnologyEntity(1L, "Java", "Programming Language");
        TechnologyEntity technologyEntity2 = new TechnologyEntity(2L, "Python", "Programming Language");

        Technology technology1 = new Technology(technologyEntity1.getId(),
                technologyEntity1.getName(), technologyEntity1.getDescription());

        Technology technology2 = new Technology(technologyEntity2.getId(),
                technologyEntity2.getName(), technologyEntity2.getDescription());

        when(technologyRepository.findBy(pageable)).thenReturn(Flux.just(technologyEntity1, technologyEntity2));
        when(technologyEntityMapper.toModel(technologyEntity1)).thenReturn(technology1);
        when(technologyEntityMapper.toModel(technologyEntity2)).thenReturn(technology2);

        StepVerifier.create(technologyR2DbcAdapter.findAllOrderedByName(page, size, ascending))
                .expectNext(technology1)
                .expectNext(technology2)
                .verifyComplete();

        verify(technologyRepository).findBy(pageable);

    }

    @Test
    void findAllOrderedByNameDesc_ShouldReturnDescending() {
        int page = 0;
        int size = 2;
        boolean ascending = false;
        Sort sort = Sort.by(Sort.Direction.DESC, "name");
        Pageable pageable = PageRequest.of(page, size, sort);

        TechnologyEntity technologyEntity1 = new TechnologyEntity(1L, "Java", "Programming Language");
        TechnologyEntity technologyEntity2 = new TechnologyEntity(2L, "Python", "Programming Language");

        Technology technology1 = new Technology(technologyEntity1.getId(),
                technologyEntity1.getName(), technologyEntity1.getDescription());

        Technology technology2 = new Technology(technologyEntity2.getId(),
                technologyEntity2.getName(), technologyEntity2.getDescription());

        when(technologyRepository.findBy(pageable)).thenReturn(Flux.just(technologyEntity2, technologyEntity1));
        when(technologyEntityMapper.toModel(technologyEntity1)).thenReturn(technology1);
        when(technologyEntityMapper.toModel(technologyEntity2)).thenReturn(technology2);

        StepVerifier.create(technologyR2DbcAdapter.findAllOrderedByName(page, size, ascending))
                .expectNext(technology2)
                .expectNext(technology1)
                .verifyComplete();

        verify(technologyRepository).findBy(pageable);

    }
}