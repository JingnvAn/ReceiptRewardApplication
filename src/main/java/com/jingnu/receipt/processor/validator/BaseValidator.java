/**
 The BaseValidator class provides methods to validate input values for a variety of patterns,
 such as string, price, date, and time.
 */
package com.jingnu.receipt.processor.validator;

import com.jingnu.receipt.processor.constant.ErrorMessage;
import com.jingnu.receipt.processor.exception.ValidationException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BaseValidator {
    //Original pattern given in the api.yml is "^\\S+$", but it contradicts to example "Mountain Dew 12PK",
    //so replaced \S to allow space in the string of retailer and shortDescription
    private final String STRING_REGEX = "^.+$";
    private final String PRICE_REGEX = "^\\d+\\.\\d{2}$";

    /**
     Validates the input string against the target pattern and throws a ValidationException
     if the input string is null or doesn't match the target pattern.
     @param inputString The input string to validate.
     @param targetPattern The regex pattern to match against.
     @param propertyName The name of the property being validated.
     @throws ValidationException if the input string is null or doesn't match the target pattern.
     */
    public void validateString(String inputString, String targetPattern, String propertyName) throws ValidationException {
        if(inputString == null || !inputString.matches(targetPattern))
            throw new ValidationException(String.format(ErrorMessage.INVALID_INPUT.getMessage(),
                    inputString,
                    propertyName,
                    targetPattern
            ));
    }

    /**
     Validates the input date or time string against the target pattern and
     throws a ValidationException if the input string cannot be parsed as a date or time.
     @param inputPattern The input string to validate.
     @param targetPattern The date or time pattern to match against.
     @param propertyName The name of the property being validated.
     @throws ValidationException if the input string cannot be parsed as a date or time.
     */
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

    /**
     Validates that a required property exists in the given JSONObject and throws
     a ValidationException if the property is missing.
     @param json The JSONObject to check for the required property.
     @param propertyName The name of the required property.
     @throws ValidationException if the required property is missing.
     */
    public void validateRequiredPropertiesExist(JSONObject json, String propertyName) throws ValidationException {
        if (!json.has(propertyName)) {
            throw new ValidationException(String.format(
                    ErrorMessage.INVALID_INPUT_MISSING_REQUIRED_PROPERTY.getMessage(),
                    propertyName));
        }
    }

    /**
     Gets the string regex pattern.
     @return The string regex pattern.
     */
    public String getSTRING_REGEX(){
        return STRING_REGEX;
    }

    /**
     Gets the price regex pattern.
     @return The price regex pattern.
     */
    public String getPRICE_REGEX(){
        return PRICE_REGEX;
    }
}
