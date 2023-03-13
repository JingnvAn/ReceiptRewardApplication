/**
 * This class encapsulates business logic for processing a receip object
 * and acts as a mediator between controller and the ReceiptRepositories
 */
package com.jingnu.receipt.processor.services;

import com.jingnu.receipt.processor.constant.ErrorMessage;
import com.jingnu.receipt.processor.exception.ReceiptAlreadyExistException;
import com.jingnu.receipt.processor.models.Item;
import com.jingnu.receipt.processor.models.Receipt;
import com.jingnu.receipt.processor.repositories.ReceiptRepository;
import com.jingnu.receipt.processor.validator.ItemValidator;
import com.jingnu.receipt.processor.validator.ReceiptValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiptService {
    @Autowired
    ReceiptRepository receiptRepository;
    @Autowired
    ItemService itemService;
    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

    /**
     * Retrieve all receipts stored.
     * @return A list of all receipts stored
     */
    public List<Receipt> getAllReceipts() {
        List<Receipt> receipts = new ArrayList<>();
        for (Receipt receipt : receiptRepository.findAll()) {
            receipts.add(receipt);
        }
        return receipts;
    }

    /**
     * Get a receipt by its id.
     * @param id The id of the receipt to retrieve.
     * @return The receipt with the specified id, or null if it does not exist.
     */
    public Receipt getReceiptById(String id) {
        return receiptRepository.findById(id).orElse(null);
    }

    /**
     * Add a new receipt to the database.
     * @param input The JSON representation of the receipt to add.
     * @return The newly created receipt.
     * @throws ReceiptAlreadyExistException If a receipt with the same retailer, date, time and total already exists.
     */

    @Transactional
    public Receipt addReceipt(String input) throws ReceiptAlreadyExistException {
        Receipt receipt = createReceiptFromInput(input);
        // Get all receipts with the same retailer, date, time and total
        List<Receipt> receiptsStored =  receiptRepository.findByRetailerAndPurchaseDateAndPurchaseTimeAndTotal(receipt.getRetailer(),
                receipt.getPurchaseDate(), receipt.getPurchaseTime(), receipt.getTotal());
        // Do idempotency check
        for (Receipt r : receiptsStored){
            if(receipt.getItems().containsAll(r.getItems()) && r.getItems().containsAll(receipt.getItems())){
                throw new ReceiptAlreadyExistException(ErrorMessage.RESOURCE_ALREADY_EXISTS.getMessage());
            }
        }
        receipt.setPoints(calculatePoints(receipt));
        return receiptRepository.save(receipt);
    }

    /**
     * Create a new receipt from the JSON input.
     * @param input The JSON representation of the receipt to create.
     * @return The newly created receipt.
     */
    private Receipt createReceiptFromInput(String input) {
        Receipt receipt = new Receipt();
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

    /**
     * Calculate points for a given receipt
     * @param inputReceipt The receipt to be processed for points
     * @return The points awarded for this receipt
     */
    private Integer calculatePoints(Receipt inputReceipt) {
        int points = 0;
        //One point for every alphanumeric character in the retailer name.
        for (char c : inputReceipt.getRetailer().toCharArray()) {
            if(Character.isLetterOrDigit(c)) {
                points++;
            }
        }
        logger.debug(points + " points - retailer name has " + inputReceipt.getRetailer().length() + " characters");

        //50 points if the total is a round dollar amount with no cents.
        String roundDollarRegex = "^\\d+\\.00$";
        if(inputReceipt.getTotal().matches(roundDollarRegex)) {
            points += 50;
            logger.debug(50 + " points - total is a round dollar");
        }

        //25 points if the total is a multiple of 0.25.
        if (Double.parseDouble(inputReceipt.getTotal()) % 0.25 == 0) {
            points += 25;
            logger.debug(25 + " points - total is a multiple of 0.25");
        }

        // 5 points for every two items on the receipt.
        points += Math.floor(inputReceipt.getItems().size() / 2.0) * 5;
        logger.debug(Math.floor(inputReceipt.getItems().size() / 2.0)*5 + " points - every 2 pairs of items");

        //If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer.
        //The result is the number of points earned.
        for(Item item : inputReceipt.getItems()) {
            if (item.getShortDescription().strip().length() % 3 == 0) {
                points += Math.ceil(Double.parseDouble(item.getPrice()) * 0.2);
                logger.debug(Math.ceil(Double.parseDouble(item.getPrice()) * 0.2) + " points for item " + item.getShortDescription());
            }
        }

        //6 points if the day in the purchase date is odd.
        int dateLength = inputReceipt.getPurchaseDate().length();
        String day = inputReceipt.getPurchaseDate().substring(dateLength-2, dateLength); //sub string the last two char to find the day
        if (Integer.parseInt(day) % 2 != 0) {
            points += 6;
            logger.debug("6  points - day in the purchase date is odd");
        }

        //10 points if the time of purchase is after 2:00pm and before 4:00pm.
        LocalTime purchaseTime = LocalTime.parse(inputReceipt.getPurchaseTime());
        LocalTime start = LocalTime.of(14, 0); // 2:00pm
        LocalTime end = LocalTime.of(16, 0); // 4:00pm
        if (!purchaseTime.isBefore(start) && purchaseTime.isBefore(end)) {
            points += 10;
            logger.debug("10 points - the time of purchase is after 2:00pm and before 4:00pm");
        }

        return points;
    }
}
