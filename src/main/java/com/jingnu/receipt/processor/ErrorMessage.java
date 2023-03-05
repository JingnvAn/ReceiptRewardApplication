package com.jingnu.receipt.processor;

import org.springframework.stereotype.Component;
public enum ErrorMessage {
    EMPTY_PAYLOAD("Empty payload not allowed."),
    INVALID_INPUT("Invalid input provided."),
    RESOURCE_NOT_FOUND("The requested receipt was not found."),
    INTERNAL_SERVER_ERROR("An internal server error occurred."),
    JSON_EXCEPTION_MSG("Invalid request due to bad JSON format: %s");

    private final String message;

    private ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}