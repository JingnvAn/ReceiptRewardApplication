package com.jingnu.receipt.processor;

import java.util.UUID;

public class Receipt {
    private String receipt_id;
    public Receipt(String name) {
        this.receipt_id = String.valueOf(UUID.randomUUID());
    }
}
