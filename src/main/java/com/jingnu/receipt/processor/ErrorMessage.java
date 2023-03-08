package com.jingnu.receipt.processor;

public enum ErrorMessage {
    EMPTY_PAYLOAD("Empty payload not allowed."),
    EMPTY_ITEMS_NOT_ALLOW("Empty 'items' not allowed."),
    EMPTY_INPUT_GENERAL("Invalid input provided. One or more properties missing."),
    INVALID_INPUT("Invalid input provided: %s. Property '%s' must be of '%s' pattern."),
    INVALID_INPUT_MISSING_REQUIRED_PROPERTY("Invalid input provided. Property '%s' is required"),
    RECEIPT_NOT_FOUND("No receipt found for id %s."),
    RESOURCE_ALREADY_EXISTS("Request failed idempotency check. Receipt already exist."),
    INTERNAL_SERVER_ERROR("An internal server error occurred. "),
    JSON_EXCEPTION_MSG("Invalid request due to bad JSON format: %s");

    private final String message;

    private ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
