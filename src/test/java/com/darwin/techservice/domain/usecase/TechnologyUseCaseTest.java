package com.darwin.techservice.domain.usecase;

import com.darwin.techservice.domain.exception.TechnologyNameAlreadyExistsException;
import com.darwin.techservice.domain.exception.TechnologyNotFoundException;
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
        when(technologyPersistencePort.create(technology)).thenReturn(Mono.just(technology));

        StepVerifier.create(technologyUseCase.create(technology))
                .expectNext(technology)
                .verifyComplete();

        verify(technologyPersistencePort).existsTechnology(technology.getName());
        verify(technologyPersistencePort).create(technology);
    }

    @Test
    void createTechnology_whenNameAlreadyExists_ShouldThrowException() {
        when(technologyPersistencePort.existsTechnology(technology.getName())).thenReturn(Mono.just(true));

        StepVerifier.create(technologyUseCase.create(technology))
                .expectError(TechnologyNameAlreadyExistsException.class)
                .verify();

        verify(technologyPersistencePort).existsTechnology(technology.getName());
        verify(technologyPersistencePort, never()).create(technology);
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

    @Test
    void findById_shouldReturn() {
        Long id = technology.getId();

        when(technologyPersistencePort.findById(id)).thenReturn(Mono.just(technology));

        StepVerifier.create(technologyUseCase.findById(id))
                .expectNext(technology)
                .verifyComplete();

        verify(technologyPersistencePort).findById(id);
    }

    @Test
    void findById_shouldThrowNotFoundException() {
        Long id = 11111L;

        when(technologyPersistencePort.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(technologyUseCase.findById(id))
                .expectError(TechnologyNotFoundException.class)
                .verify();

        verify(technologyPersistencePort).findById(id);
    }

    @Test
    void existsById_shouldReturnTrue() {
        Long id = 1L;

        when(technologyPersistencePort.existsById(id)).thenReturn(Mono.just(true));

        StepVerifier.create(technologyUseCase.existsById(id))
                .expectNext(true)
                .verifyComplete();

        verify(technologyPersistencePort).existsById(id);
    }

    @Test
    void existsById_shouldReturnFalse() {
        Long id = 1111L;

        when(technologyPersistencePort.existsById(id)).thenReturn(Mono.just(false));

        StepVerifier.create(technologyUseCase.existsById(id))
                .expectNext(false)
                .verifyComplete();

        verify(technologyPersistencePort).existsById(id);
    }
}