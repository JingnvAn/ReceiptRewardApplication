package com.jingnu.receipt.processor.service;

import com.jingnu.receipt.processor.model.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemService {
    public Item createItem(String shortDes, String price) {
        Item item = new Item();
        item.setShortDescription(shortDes);
        item.setPrice(price);
        return item;
    }
}
