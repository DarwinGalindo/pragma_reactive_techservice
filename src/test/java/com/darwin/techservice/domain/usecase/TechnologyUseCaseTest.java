package com.darwin.techservice.domain.usecase;

import com.darwin.techservice.domain.exception.TechnologyNameAlreadyExistsException;
import com.darwin.techservice.domain.model.Technology;
import com.darwin.techservice.domain.spi.ITechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class TechnologyUseCaseTest {

    private TechnologyUseCase technologyUseCase;
    private ITechnologyPersistencePort technologyPersistencePort;

    @BeforeEach
    void setUp() {
        technologyPersistencePort = mock(ITechnologyPersistencePort.class);
        technologyUseCase = new TechnologyUseCase(technologyPersistencePort);
    }

    @Test
    void createTechnology_whenNameDoesNotExist_ShouldCreate() {
        Technology technology = new Technology();
        technology.setId(1L);
        technology.setName("Java");
        technology.setDescription("Java programming language");

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
        Technology technology = new Technology();
        technology.setId(1L);
        technology.setName("Java");
        technology.setDescription("Java programming language");

        when(technologyPersistencePort.existsTechnology(technology.getName())).thenReturn(Mono.just(true));

        StepVerifier.create(technologyUseCase.createTechnology(technology))
                .expectError(TechnologyNameAlreadyExistsException.class)
                .verify();

        verify(technologyPersistencePort).existsTechnology(technology.getName());
        verify(technologyPersistencePort, never()).createTechnology(technology);
    }
}