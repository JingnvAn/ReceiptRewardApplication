package com.jingnu.receipt.processor.service;

import com.jingnu.receipt.processor.model.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiptService {
    @Autowired
    Receipt receipt;

}
