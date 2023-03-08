package com.jingnu.receipt.processor.validator;

import com.jingnu.receipt.processor.ErrorMessage;
import com.jingnu.receipt.processor.exception.ValidationException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BaseValidator {
    //Original pattern given in the api.yml is "^\\S+$", but it contradicts to example "Mountain dDew 12PK",
    //so replaced \S to allow space in the string of retailer and shortDescription
    private final String STRING_REGEX = "^.+$";
    private final String PRICE_REGEX = "^\\d+\\.\\d{2}$";

    public void validateString(String inputString, String targetPattern, String propertyName) throws ValidationException {
        if(inputString == null || !inputString.matches(targetPattern))
            throw new ValidationException(String.format(ErrorMessage.INVALID_INPUT.getMessage(),
                    inputString,
                    propertyName,
                    targetPattern
            ));
    }

    public void validateDateOrTime(String inputPattern, String targetPattern, String propertyName) throws ValidationException {
        SimpleDateFormat sdf = new SimpleDateFormat(targetPattern);
        try {
            sdf.parse(inputPattern);
        } catch (ParseException e) {
            throw new ValidationException(String.format(
                    ErrorMessage.INVALID_INPUT.getMessage(),
                    e.getMessage(),
                    propertyName,
                    targetPattern
            ));
        }
    }
    public void validateRequiredPropertiesExist(JSONObject json, String propertyName) throws ValidationException {
        if (!json.has(propertyName)) {
            throw new ValidationException(String.format(
                    ErrorMessage.INVALID_INPUT_MISSING_REQUIRED_PROPERTY.getMessage(),
                    propertyName));
        }
    }

    public String getSTRING_REGEX(){
        return STRING_REGEX;
    }

    public String getPRICE_REGEX(){
        return PRICE_REGEX;
    }
}
