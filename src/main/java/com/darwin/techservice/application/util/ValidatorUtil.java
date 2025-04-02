package com.darwin.techservice.application.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;

public final class ValidatorUtil {

    private static final String OBJECT_NAME = "object";

    private ValidatorUtil() {
    }

    public static <T> void validate(Validator validator, T object) {
        Errors errors = new BeanPropertyBindingResult(object, OBJECT_NAME);
        validator.validate(object, errors);

        if (errors.hasErrors()) {
            List<String> errorMessages = errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            String errorMessage = String.join(", ", errorMessages);
            throw new ServerWebInputException(errorMessage);
        }
    }
}
