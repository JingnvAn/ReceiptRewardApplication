/**
 * This interface provides an abstraction layer between the controller and the datastore.
 * It defines a set of methods for accessing and manipulating data stored in the datastore.
 */
package com.jingnu.receipt.processor.repositories;

import com.jingnu.receipt.processor.models.Receipt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReceiptRepository extends CrudRepository<Receipt, String> {
    /**
     Returns a list of receipts matching the specified retailer, purchase date, purchase time, and total.
     @param retailer The name of the retailer associated with the receipts to be returned.
     @param purchaseDate The date of purchase associated with the receipts to be returned.
     @param purchaseTime The time of purchase associated with the receipts to be returned.
     @param total The total amount associated with the receipts to be returned.
     @return A list of receipts that match the specified criteria, or an empty list if no receipts were found.
     */
    public List<Receipt> findByRetailerAndPurchaseDateAndPurchaseTimeAndTotal(String retailer, String purchaseDate, String purchaseTime, String total);
}
