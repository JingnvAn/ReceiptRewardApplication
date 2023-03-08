package com.jingnu.receipt.processor.responseModels;

import com.jingnu.receipt.processor.responseModels.SubmitReceiptResponse;
import org.springframework.stereotype.Component;

@Component
public class SubmitReceiptSuccessResponse extends SubmitReceiptResponse {
    private String id;
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
