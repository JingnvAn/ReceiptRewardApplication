package com.jingnu.receipt.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.jingnu.receipt.processor.exception.ValidationException;
import com.jingnu.receipt.processor.model.Item;
import com.jingnu.receipt.processor.model.Receipt;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class ReceiptService {
    @Autowired
    Receipt receipt;

    private final String RETAILER = "retailer";
    private final String PURCHASE_DATE = "purchaseDate";
    private final String PURCHASE_TIME = "purchaseTime";
    private final String ITEMS = "items";
    private final String TOTAL = "total";

    private final String RETAILER_REGEX = "^\\S+$";

    private static final String RECEIPT_SCHEMA = "{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"required\":[\"retailer\",\"purchaseDate\",\"purchaseTime\",\"items\",\"total\"],\"properties\":{\"retailer\":{\"description\":\"The name of the retailer or store the receipt is from.\",\"type\":\"string\",\"pattern\":\"^\\\\S+$\",\"example\":\"Target\"},\"purchaseDate\":{\"description\":\"The date of the purchase printed on the receipt.\",\"type\":\"string\",\"format\":\"date\",\"example\":\"2022-01-01\"},\"purchaseTime\":{\"description\":\"The time of the purchase printed on the receipt. 24-hour time expected.\",\"type\":\"string\",\"format\":\"time\",\"example\":\"13:01\"},\"items\":{\"type\":\"array\",\"minItems\":1,\"items\":{\"$ref\":\"#/components/schemas/Item\"}},\"total\":{\"description\":\"The total amount paid on the receipt.\",\"type\":\"string\",\"pattern\":\"^\\\\d+\\\\.\\\\d{2}$\",\"example\":\"6.49\"}}}";

    public void validateReceipt(String input) throws ValidationException {
        if(input == null || input.isEmpty()){
            throw new ValidationException(ErrorMessage.EMPTY_PAYLOAD.getMessage());
        }
        createReceipt(input);

    }

    public void createReceipt(String input) throws ValidationException {
        JSONObject json = new JSONObject(input);
        String retailer = json.getString(RETAILER);
        String purchaseDate = json.getString(PURCHASE_DATE);
        String purchaseTime = json.getString(PURCHASE_TIME);
        String itemsString = json.getString(ITEMS);
        String total = json.getString(TOTAL);

        validateRetailer(retailer);
        validatePurchaseDate(purchaseDate);

    }

    public void validateRetailer(String retailer) throws ValidationException {
        if(retailer == null || !retailer.matches(RETAILER_REGEX))
            throw new ValidationException(ErrorMessage.INVALID_INPUT.getMessage());
    }

    public void validatePurchaseDate(String purchaseDate) {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(true);

        try {
            Date date = sdf.parse(purchaseDate);
            System.out.println("String is of date format: " + true);
        } catch (ParseException e) {
            System.out.println("String is of date format: " + false);
        }

    }

}
