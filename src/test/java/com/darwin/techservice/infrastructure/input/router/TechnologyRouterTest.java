package com.darwin.techservice.infrastructure.input.router;

import com.darwin.techservice.application.dto.TechnologyRequest;
import com.darwin.techservice.application.dto.TechnologyResponse;
import com.darwin.techservice.application.handler.ITechnologyHandler;
import com.darwin.techservice.infrastructure.util.Routes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(TechnologyRouter.class)
@Import(TestSecurityConfig.class)
class TechnologyRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ITechnologyHandler technologyHandler;

    @Test
    void testFindAllOrderedByName() {
        var techResponse1 = new TechnologyResponse(1L, "Tech 1", "Desc");
        var techResponse2 = new TechnologyResponse(2L, "Tech 2", "Desc");

        when(technologyHandler.findAllOrderedByName(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().bodyValue(List.of(techResponse1, techResponse2)));

        webTestClient.get()
                .uri(Routes.TECHNOLOGY_RESOURCE)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TechnologyResponse.class)
                .hasSize(2)
                .contains(techResponse1, techResponse2);
    }

    @Test
    void testCreate() {
        var techRequest = new TechnologyRequest("Tech 1", "Desc");
        var techResponse = new TechnologyResponse(1L, "Tech 1", "Desc");

        when(technologyHandler.create(any(ServerRequest.class)))
                .thenReturn(ServerResponse.status(HttpStatus.CREATED).bodyValue(techResponse));

        webTestClient.post()
                .uri(Routes.TECHNOLOGY_RESOURCE)
                .bodyValue(techRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TechnologyResponse.class)
                .isEqualTo(techResponse);
    }

    @Test
    void testFindById() {
        var techId = 1L;
        var techResponse = new TechnologyResponse(techId, "Tech 1", "Desc");

        when(technologyHandler.findById(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().bodyValue(techResponse));

        webTestClient.get()
                .uri(Routes.TECHNOLOGY_RESOURCE_ID, techId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TechnologyResponse.class)
                .isEqualTo(techResponse);
    }

    @Test
    void testExistsById() {
        var techId = 1L;
        var exists = true;

        when(technologyHandler.existsById(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().bodyValue(exists));

        webTestClient.get()
                .uri(Routes.TECHNOLOGY_RESOURCE_ID_EXISTS, techId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(exists);
    }
}