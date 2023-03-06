package com.jingnu.receipt.processor.service;

import com.jingnu.receipt.processor.model.Item;
import com.jingnu.receipt.processor.model.Receipt;
import com.jingnu.receipt.processor.validator.ItemValidator;
import com.jingnu.receipt.processor.validator.ReceiptValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReceiptService {
    @Autowired
    Receipt receipt;

    @Autowired
    ItemService itemService;

    public Receipt createReceiptFromInput(String input) {
        JSONObject inputJson = new JSONObject(input);
        receipt.setRetailer(inputJson.getString(ReceiptValidator.Properties.RETAILER.getValue()));
        receipt.setPurchaseDate(inputJson.getString(ReceiptValidator.Properties.PURCHASE_DATE.getValue()));
        receipt.setPurchaseTime(inputJson.getString(ReceiptValidator.Properties.PURCHASE_TIME.getValue()));
        receipt.setTotal(inputJson.getString(ReceiptValidator.Properties.TOTAL.getValue()));
        JSONArray itemsJson = inputJson.getJSONArray(ReceiptValidator.Properties.ITEMS.getValue());

        List<Item> items = new ArrayList<>();
        // Loop through the array and process each item
        for (int i = 0; i < itemsJson.length(); i++) {
            JSONObject currentItem = itemsJson.getJSONObject(i);
            String shortDescription = currentItem.getString(ItemValidator.Properties.SHORT_DESCRIPTION.getValue());
            String price = currentItem.getString(ItemValidator.Properties.PRICE.getValue());

            items.add(itemService.createItem(shortDescription, price));
        }

        receipt.setItems(items);
        return receipt;
    }

    public String generateReceiptId() {
        return UUID.randomUUID().toString();
    }

    public Integer calculatePoints(Receipt inputReceipt) {
        return 5;
    }
}
