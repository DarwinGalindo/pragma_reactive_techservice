package com.darwin.techservice.application.handler;

import com.darwin.techservice.application.dto.TechnologyRequest;
import com.darwin.techservice.application.dto.TechnologyResponse;
import com.darwin.techservice.application.mapper.TechnologyDtoMapper;
import com.darwin.techservice.application.util.LocalValidator;
import com.darwin.techservice.domain.api.ITechnologyServicePort;
import com.darwin.techservice.domain.model.Technology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TechnologyHandlerTest {
    private ITechnologyServicePort technologyServicePort;
    private TechnologyDtoMapper technologyDtoMapper;
    private LocalValidator validator;
    private ITechnologyHandler technologyHandler;
    private ServerRequest request;

    @BeforeEach
    void setUp() {
        technologyServicePort = mock(ITechnologyServicePort.class);
        technologyDtoMapper = mock(TechnologyDtoMapper.class);
        validator = mock(LocalValidator.class);
        request = mock(ServerRequest.class);
        technologyHandler = new TechnologyHandler(technologyServicePort, technologyDtoMapper, validator);
    }

    @Test
    void create() {
        TechnologyRequest technologyRequest = new TechnologyRequest("Tech Name", "Description");
        Technology technology = new Technology();
        TechnologyResponse technologyResponse = new TechnologyResponse(1L, "Tech Name", "Description");

        when(request.bodyToMono(TechnologyRequest.class)).thenReturn(Mono.just(technologyRequest));
        doNothing().when(validator).validate(technologyRequest);
        when(technologyDtoMapper.toModel(technologyRequest)).thenReturn(technology);
        when(technologyServicePort.create(technology)).thenReturn(Mono.just(technology));
        when(technologyDtoMapper.toResponse(technology)).thenReturn(technologyResponse);

        StepVerifier.create(technologyHandler.create(request))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.CREATED, response.statusCode());
                    return true;
                })
                .expectComplete()
                .verify();

        verify(request).bodyToMono(TechnologyRequest.class);
        verify(validator).validate(technologyRequest);
        verify(technologyDtoMapper).toModel(technologyRequest);
        verify(technologyServicePort).create(technology);
        verify(technologyDtoMapper).toResponse(technology);
    }

    @Test
    void findAllOrderedByName() {
        when(request.queryParam(anyString())).thenReturn(Optional.of("0"));
        when(technologyServicePort.findAllOrderedByName(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(Flux.just(new Technology(), new Technology()));

        StepVerifier.create(technologyHandler.findAllOrderedByName(request))
                .expectNextMatches(response -> {
                    assertEquals(HttpStatus.OK, response.statusCode());
                    return true;
                })
                .expectComplete()
                .verify();

        verify(technologyServicePort).findAllOrderedByName(anyInt(), anyInt(), anyBoolean());
        verify(request).queryParam("page");
        verify(request).queryParam("size");
        verify(request).queryParam("sortAscending");
        verify(technologyDtoMapper).toResponseList(any());
    }

    @Test
    void findById() {
        long id = 1L;
        var tech = new Technology(id, "Tech 1", "Description");
        var techResponse = new TechnologyResponse(id, tech.getName(), tech.getDescription());

        when(request.pathVariable("id")).thenReturn(String.valueOf(id));
        when(technologyServicePort.findById(id)).thenReturn(Mono.just(tech));
        when(technologyDtoMapper.toResponse(tech)).thenReturn(techResponse);

        StepVerifier.create(technologyHandler.findById(request))
                .expectNextMatches(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                    return true;
                })
                .expectComplete()
                .verify();

        verify(technologyServicePort).findById(id);
        verify(technologyDtoMapper).toResponse(tech);
        verify(request).pathVariable("id");
    }

    @Test
    void existsById() {
        long id = 1L;

        when(request.pathVariable("id")).thenReturn(String.valueOf(id));
        when(technologyServicePort.existsById(id)).thenReturn(Mono.just(true));

        StepVerifier.create(technologyHandler.existsById(request))
                .expectNextMatches(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                    return true;
                })
                .expectComplete()
                .verify();

        verify(technologyServicePort).existsById(id);
        verify(request).pathVariable("id");
    }
}