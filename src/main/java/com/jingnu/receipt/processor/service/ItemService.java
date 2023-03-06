package com.jingnu.receipt.processor.service;

import com.jingnu.receipt.processor.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemService {
    @Autowired
    Item item;
    public Item createItem(String shortDes, String price) {
        item.setShortDescription(shortDes);
        item.setPrice(price);
        return item;
    }
}
