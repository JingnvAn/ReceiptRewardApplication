package com.jingnu.receipt.processor.repositories;

import com.jingnu.receipt.processor.models.Receipt;
import org.springframework.data.repository.CrudRepository;

public interface ReceiptRepository extends CrudRepository<Receipt, String> {
}
