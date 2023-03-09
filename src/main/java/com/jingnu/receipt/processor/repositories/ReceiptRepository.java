package com.jingnu.receipt.processor.repositories;

import com.jingnu.receipt.processor.models.Item;
import com.jingnu.receipt.processor.models.Receipt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReceiptRepository extends CrudRepository<Receipt, String> {
    boolean existsByRetailerAndPurchaseDateAndPurchaseTimeAndTotal(String retailer, String purchaseDate, String purchaseTime, String total);

    public List<Receipt> findByRetailerAndPurchaseDateAndPurchaseTimeAndTotal(String retailer, String purchaseDate, String purchaseTime, String total);
}
