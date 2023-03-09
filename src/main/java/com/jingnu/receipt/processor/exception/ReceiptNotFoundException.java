package com.jingnu.receipt.processor.exception;

public class ReceiptNotFoundException extends RuntimeException{
    public ReceiptNotFoundException(String message){
        super(message);
    }
}
