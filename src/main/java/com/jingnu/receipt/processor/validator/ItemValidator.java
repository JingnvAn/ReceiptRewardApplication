package com.jingnu.receipt.processor.validator;

import com.jingnu.receipt.processor.constant.ErrorMessage;
import com.jingnu.receipt.processor.exception.ValidationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ItemValidator extends BaseValidator{
    public enum Properties {
        SHORT_DESCRIPTION("shortDescription"),
        PRICE("price");

        private final String value;

        Properties(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public void validateItems(String input) throws ValidationException {
        if (input == null || input.isEmpty()) {
            throw new ValidationException(ErrorMessage.EMPTY_INPUT_GENERAL.getMessage());
        }
        JSONObject inputJson = new JSONObject(input);
        JSONArray items = inputJson.getJSONArray(ReceiptValidator.Properties.ITEMS.getValue());

        if (items.isEmpty()) {
            throw new ValidationException(ErrorMessage.EMPTY_ITEMS_NOT_ALLOW.getMessage());
        }

        // Loop through the array and process each item
        for (int i = 0; i < items.length(); i++) {
            JSONObject currentItem = items.getJSONObject(i);

            // validate all required properties exist
            for (Properties p : Properties.values()) {
                validateRequiredPropertiesExist(currentItem, p.getValue());
            }

            String shortDescription = currentItem.getString(Properties.SHORT_DESCRIPTION.getValue());
            String price = currentItem.getString(Properties.PRICE.getValue());

            // validate pattern
            validateString(shortDescription.strip(),
                    getSTRING_REGEX(),
                    Properties.SHORT_DESCRIPTION.getValue());
            validateString(price.strip(),
                    getPRICE_REGEX(),
                    Properties.PRICE.getValue());
        }
    }
}
