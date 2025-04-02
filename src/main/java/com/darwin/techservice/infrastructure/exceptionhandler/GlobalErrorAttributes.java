package com.darwin.techservice.infrastructure.exceptionhandler;

import com.darwin.techservice.domain.exception.TechnologyNameAlreadyExistsException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = error.getMessage();

        if (error instanceof TechnologyNameAlreadyExistsException) {
            status = HttpStatus.CONFLICT;
            message = ExceptionMessage.TECHNOLOGY_NAME_ALREADY_EXISTS.getMessage();
        } else if (error instanceof ServerWebInputException) {
            status = HttpStatus.BAD_REQUEST;
        }

        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("timestamp", LocalDateTime.now());
        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status.getReasonPhrase());
        errorAttributes.put("message", message);
        errorAttributes.put("path", request.path());

        return errorAttributes;
    }

}
