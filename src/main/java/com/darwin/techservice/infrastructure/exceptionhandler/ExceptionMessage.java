package com.darwin.techservice.infrastructure.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    TECHNOLOGY_NAME_ALREADY_EXISTS("The technology name already exists"),
    TECHNOLOGY_NOT_FOUND("Technology not found");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }
}
