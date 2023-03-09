package com.jingnu.receipt.processor.controllers;

import com.jingnu.receipt.processor.constant.ErrorMessage;
import com.jingnu.receipt.processor.exception.ReceiptAlreadyExistException;
import com.jingnu.receipt.processor.exception.ReceiptNotFoundException;
import com.jingnu.receipt.processor.exception.ValidationException;
import com.jingnu.receipt.processor.responseModels.*;
import com.jingnu.receipt.processor.models.*;
import com.jingnu.receipt.processor.services.ReceiptService;
import com.jingnu.receipt.processor.validator.ReceiptValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    @Autowired
    ReceiptService receiptService;
    @Autowired
    ReceiptValidator receiptValidator;
    @Autowired
    SubmitReceiptSuccessResponse submitReceiptSuccessResponse;
    @Autowired
    GetPointsSuccessResponse getPointsSuccessResponse;
    // Logger for debug
    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

    @GetMapping(value="/all")
    public ResponseEntity<String> getAllReceipts() {
        List<Receipt> receiptList = receiptService.getAllReceipts();
        JSONArray resultArray = new JSONArray();
        receiptList.forEach(r -> resultArray.put(new JSONObject(r)));

        return ResponseEntity.status(HttpStatus.OK).body(resultArray.toString(4) + "\n");
    }

    @GetMapping(value="/{id}/points")
    public ResponseEntity<String> getPoints(@PathVariable("id") String id) throws ReceiptNotFoundException {
        try {
            Receipt receipt = receiptService.getReceiptById(id);
            // check if the id exists, if it doesn't, fail the request
            if (receipt == null) {
                throw new ReceiptNotFoundException(String.format(ErrorMessage.RECEIPT_NOT_FOUND.getMessage(), id));
            }
            getPointsSuccessResponse.setPoints(receipt.getPoints());
        } catch (ReceiptNotFoundException receiptNotFoundException) {
            logger.debug("Request failed with resourceNotFoundException " + receiptNotFoundException.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(receiptNotFoundException.getMessage() + "\n");
        } catch (Exception e) {
            logger.debug("Request failed with unexpected exception " + ErrorMessage.INTERNAL_SERVER_ERROR + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + "\n");
        }

        return ResponseEntity.status(HttpStatus.OK).body(new JSONObject(getPointsSuccessResponse).toString(4) + "\n");
    }

    @PostMapping(value="/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> submitReceipt(@RequestBody String requestBody) {
        try {
            receiptValidator.validateReceipt(requestBody);
            // create a receipt object
            Receipt createdReceipt = receiptService.addReceipt(requestBody);
            if (createdReceipt != null) {
                String id = createdReceipt.getId();

                // build success response and ready to return
                submitReceiptSuccessResponse.setId(id);
                logger.debug("Created a receipt with id: " + id+", points: " + createdReceipt.getPoints());
            }
            return ResponseEntity.status(HttpStatus.OK).body(new JSONObject(submitReceiptSuccessResponse).toString(4)+ "\n");
        } catch (JSONException e) {
            String errorMessage = String.format(ErrorMessage.JSON_EXCEPTION_MSG.getMessage(), e.getMessage());
            logger.debug("Request failed with JSONException " + e.getMessage());
            return ResponseEntity.badRequest().body(errorMessage + "\n");
        } catch (ValidationException validationException) {
            logger.debug("Request failed with ValidationException " + validationException.getMessage());
            return ResponseEntity.badRequest().body(validationException.getMessage() + "\n");
        } catch (ReceiptAlreadyExistException receiptAlreadyExistException) {
            logger.debug("Request failed with resourceAlreadyExistException " + ErrorMessage.RESOURCE_ALREADY_EXISTS.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorMessage.RESOURCE_ALREADY_EXISTS.getMessage() + "\n");
        } catch (Exception generalException) {
            logger.debug("Request failed with unexpected exception " + ErrorMessage.INTERNAL_SERVER_ERROR + generalException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(generalException.getMessage() + "\n");
        }
    }
}
