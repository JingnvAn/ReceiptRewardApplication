package com.jingnu.receipt.processor.validator;

import com.jingnu.receipt.processor.ErrorMessage;
import com.jingnu.receipt.processor.exception.ValidationException;
import com.jingnu.receipt.processor.model.Receipt;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReceiptValidator extends BaseValidator{
   public enum Properties {
      RETAILER("retailer"),
      PURCHASE_DATE("purchaseDate"),
      PURCHASE_TIME("purchaseTime"),
      ITEMS("items"),
      TOTAL("total");

      private final String value;

      Properties(String value) {
         this.value = value;
      }

      public String getValue() {
         return value;
      }
   }

   @Autowired
   ItemValidator itemValidator;
   private final String DATE_PATTERN = "yyyy-MM-dd";
   private final String TIME_PATTERN = "HH:mm";

   public void validateReceipt(String input) throws ValidationException {
      if(input == null || input.isEmpty()){
         throw new ValidationException(ErrorMessage.EMPTY_PAYLOAD.getMessage());
      }

      JSONObject inputJson = new JSONObject(input);
      String retailer = inputJson.getString(Properties.RETAILER.getValue());
      String purchaseDate = inputJson.getString(Properties.PURCHASE_DATE.getValue());
      String purchaseTime = inputJson.getString(Properties.PURCHASE_TIME.getValue());
      String total = inputJson.getString(Properties.TOTAL.getValue());

      // Validate fields other than Items
      validateString(retailer.strip(), getSTRING_REGEX(), Properties.RETAILER.getValue());
      validateString(total.strip(), getPRICE_REGEX(), Properties.TOTAL.getValue());
      validateDateOrTime(purchaseDate.strip(), DATE_PATTERN, Properties.PURCHASE_DATE.getValue());
      validateDateOrTime(purchaseTime.strip(), TIME_PATTERN, Properties.PURCHASE_TIME.getValue());

      //Validate Items
      Object items = inputJson.get(Properties.ITEMS.getValue());
      itemValidator.validateItems(input);
   }
}
