package com.darwin.techservice.domain.usecase;

import com.darwin.techservice.domain.exception.TechnologyNameAlreadyExistsException;
import com.darwin.techservice.domain.model.Technology;
import com.darwin.techservice.domain.spi.ITechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class TechnologyUseCaseTest {

    private TechnologyUseCase technologyUseCase;
    private ITechnologyPersistencePort technologyPersistencePort;
    private Technology technology;

    @BeforeEach
    void setUp() {
        technologyPersistencePort = mock(ITechnologyPersistencePort.class);
        technologyUseCase = new TechnologyUseCase(technologyPersistencePort);

        technology = new Technology();
        technology.setId(1L);
        technology.setName("Java");
        technology.setDescription("Java programming language");
    }

    @Test
    void createTechnology_whenNameDoesNotExist_ShouldCreate() {
        when(technologyPersistencePort.existsTechnology(technology.getName())).thenReturn(Mono.just(false));
        when(technologyPersistencePort.createTechnology(technology)).thenReturn(Mono.just(technology));

        StepVerifier.create(technologyUseCase.createTechnology(technology))
                .expectNext(technology)
                .verifyComplete();

        verify(technologyPersistencePort).existsTechnology(technology.getName());
        verify(technologyPersistencePort).createTechnology(technology);
    }

    @Test
    void createTechnology_whenNameAlreadyExists_ShouldThrowException() {
        when(technologyPersistencePort.existsTechnology(technology.getName())).thenReturn(Mono.just(true));

        StepVerifier.create(technologyUseCase.createTechnology(technology))
                .expectError(TechnologyNameAlreadyExistsException.class)
                .verify();

        verify(technologyPersistencePort).existsTechnology(technology.getName());
        verify(technologyPersistencePort, never()).createTechnology(technology);
    }

    @Test
    void findAllOrderedByNameAsc() {
        int page = 0;
        int size = 1;
        boolean ascending = true;

        Technology technology2 = new Technology(2L, "C#", "Description");

        when(technologyPersistencePort.findAllOrderedByName(page, size, ascending))
                .thenReturn(Flux.just(technology2, technology));

        StepVerifier.create(technologyUseCase.findAllOrderedByName(page, size, ascending))
                .expectNext(technology2)
                .expectNext(technology)
                .verifyComplete();

        verify(technologyPersistencePort).findAllOrderedByName(page, size, ascending);
    }

    @Test
    void findAllOrderedByNameDesc() {
        int page = 0;
        int size = 1;
        boolean ascending = false;

        Technology technology2 = new Technology(2L, "C#", "Description");

        when(technologyPersistencePort.findAllOrderedByName(page, size, ascending))
                .thenReturn(Flux.just(technology, technology2));

        StepVerifier.create(technologyUseCase.findAllOrderedByName(page, size, ascending))
                .expectNext(technology)
                .expectNext(technology2)
                .verifyComplete();

        verify(technologyPersistencePort).findAllOrderedByName(page, size, ascending);
    }
}