package com.jingnu.receipt.processor.responseModels;

import org.springframework.stereotype.Component;

@Component
public class SubmitReceiptFailureResponse extends SubmitReceiptResponse {
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
