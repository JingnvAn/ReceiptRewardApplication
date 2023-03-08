package com.jingnu.receipt.processor.services;

import com.jingnu.receipt.processor.models.Item;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class ItemService {
    public Item createItem(String shortDes, String price) {
        Item item = new Item();
        item.setShortDescription(shortDes);
        item.setPrice(price);
        return item;
    }
}
