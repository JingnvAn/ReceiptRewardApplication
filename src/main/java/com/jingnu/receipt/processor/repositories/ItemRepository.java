package com.jingnu.receipt.processor.repositories;

import com.jingnu.receipt.processor.models.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, String> {
}
