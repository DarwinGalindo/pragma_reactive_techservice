package com.darwin.techservice.infrastructure.exceptionhandler;

import com.darwin.techservice.domain.exception.TechnologyNameAlreadyExistsException;
import com.darwin.techservice.domain.exception.TechnologyNotFoundException;
import com.darwin.techservice.infrastructure.util.Routes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class GlobalErrorAttributesTest {
    private GlobalErrorAttributes globalErrorAttributes;

    @Mock
    private ServerRequest mockRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalErrorAttributes = new GlobalErrorAttributes();
    }

    @Test
    void shouldReturnConflictForTechnologyNameAlreadyExistsException() {
        when(mockRequest.path()).thenReturn(Routes.TECHNOLOGY_RESOURCE);
        TechnologyNameAlreadyExistsException exception = new TechnologyNameAlreadyExistsException();

        Map<String, Object> errorAttributes = getErrorAttributes(exception);

        assertEquals(HttpStatus.CONFLICT.value(), errorAttributes.get("status"));
        assertEquals(HttpStatus.CONFLICT.getReasonPhrase(), errorAttributes.get("error"));
        assertEquals(ExceptionMessage.TECHNOLOGY_NAME_ALREADY_EXISTS.getMessage(), errorAttributes.get("message"));
        assertEquals(Routes.TECHNOLOGY_RESOURCE, errorAttributes.get("path"));
        assertNotNull(errorAttributes.get("timestamp"));
    }

    @Test
    void shouldReturnBadRequestForServerWebInputException() {
        when(mockRequest.path()).thenReturn(Routes.TECHNOLOGY_RESOURCE);
        ServerWebInputException exception = new ServerWebInputException("Invalid input");

        Map<String, Object> errorAttributes = getErrorAttributes(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), errorAttributes.get("status"));
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorAttributes.get("error"));
        assertThat((String) errorAttributes.get("message")).contains("Invalid input");
        assertEquals(Routes.TECHNOLOGY_RESOURCE, errorAttributes.get("path"));
        assertNotNull(errorAttributes.get("timestamp"));
    }

    @Test
    void shouldReturnNotFoundForTechnologyNotFoundException() {
        when(mockRequest.path()).thenReturn(Routes.TECHNOLOGY_RESOURCE_ID);
        TechnologyNotFoundException exception = new TechnologyNotFoundException();

        Map<String, Object> errorAttributes = getErrorAttributes(exception);

        assertEquals(HttpStatus.NOT_FOUND.value(), errorAttributes.get("status"));
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorAttributes.get("error"));
        assertEquals(ExceptionMessage.TECHNOLOGY_NOT_FOUND.getMessage(), errorAttributes.get("message"));
        assertEquals(Routes.TECHNOLOGY_RESOURCE_ID, errorAttributes.get("path"));
        assertNotNull(errorAttributes.get("timestamp"));
    }

    @Test
    void shouldReturnInternalServerErrorForUnknownException() {
        when(mockRequest.path()).thenReturn("/unknown");
        RuntimeException exception = new RuntimeException("Unexpected error");

        Map<String, Object> errorAttributes = getErrorAttributes(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorAttributes.get("status"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorAttributes.get("error"));
        assertEquals("Unexpected error", errorAttributes.get("message"));
        assertEquals("/unknown", errorAttributes.get("path"));
        assertNotNull(errorAttributes.get("timestamp"));
    }

    private Map<String, Object> getErrorAttributes(Throwable exception) {
        when(mockRequest.attribute("org.springframework.boot.web.reactive.error.DefaultErrorAttributes.ERROR"))
                .thenReturn(Optional.of(exception));

        return globalErrorAttributes.getErrorAttributes(mockRequest, ErrorAttributeOptions.defaults());
    }
}