package com.jingnu.receipt.processor.exception;

public class ReceiptAlreadyExistException extends RuntimeException{
    public ReceiptAlreadyExistException(String message){
        super(message);
    }
}
