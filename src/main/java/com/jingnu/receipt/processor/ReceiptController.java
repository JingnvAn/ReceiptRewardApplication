package com.jingnu.receipt.processor;

import com.jingnu.receipt.processor.exception.ValidationException;
import com.jingnu.receipt.processor.model.*;
import com.jingnu.receipt.processor.service.ReceiptService;
import com.jingnu.receipt.processor.validator.ReceiptValidator;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashMap;
import java.util.Map;

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
    SubmitReceiptFailureResponse submitReceiptFailureResponse;
    @Autowired
    GetPointsSuccessResponse getPointsSuccessResponse;
    @Autowired
    GetPointsFailureResponse getPointsFailureResponse;

    // Logger for debug
    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);
    // key: id, value: receipt
    Map<String, Receipt> receiptsReservoir = new HashMap<>();
    // key: id, value: points
    Map<String, Integer> pointsReservoir = new HashMap<>();

    @GetMapping(value="/{id}/points")
    public ResponseEntity<GetPointsResponse> getPoints(@PathVariable("id") String id) {
        // check if the id exists, if it doesn't, fail the request
        if(!pointsReservoir.containsKey(id)) {
            getPointsFailureResponse.setErrorMessage(String.format(ErrorMessage.RECEIPT_NOT_FOUND.getMessage(), id));
            logger.debug(getPointsFailureResponse.getErrorMessage());
            return  ResponseEntity.badRequest().body(getPointsFailureResponse);
        }

        // the id exist, return success with the points
        int points = pointsReservoir.get(id);
        getPointsSuccessResponse.setPoints(points);
        logger.debug("Receipt with id: "+id+" has points: "+points);
        return ResponseEntity.ok(getPointsSuccessResponse);
    }

    @PostMapping(value="/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubmitReceiptResponse> submitReceipt(@RequestBody String requestBody) {
        try {
            receiptValidator.validateReceipt(requestBody);
            // create a receipt object
            Receipt receipt = receiptService.createReceiptFromInput(requestBody);
            // generate an id for this receipt
            String id = receiptService.generateReceiptId();
            // store (id, Receipt) in a map
            receiptsReservoir.put(id, receipt);

            // calculate points
            int points = receiptService.calculatePoints(receipt);
            // store (id, points) in a map
            pointsReservoir.put(id, points);

            // build success response and ready to return
            submitReceiptSuccessResponse.setId(id);
            logger.debug("Created a receipt with id: " + id);
            return ResponseEntity.ok(submitReceiptSuccessResponse);
        } catch (JSONException e) {
            String errorMessage = String.format(ErrorMessage.JSON_EXCEPTION_MSG.getMessage(), e.getMessage());
            submitReceiptFailureResponse.setErrorMessage(errorMessage);
            logger.debug("Request failed with JSONException " + e.getMessage());
            return ResponseEntity.badRequest().body(submitReceiptFailureResponse);
        } catch (ValidationException validationException) {
            submitReceiptFailureResponse.setErrorMessage(validationException.getMessage());
            logger.debug("Request failed with ValidationException " + validationException.getMessage());
            return ResponseEntity.badRequest().body(submitReceiptFailureResponse);
        } catch (Exception generalException) {
            submitReceiptFailureResponse.setErrorMessage(generalException.getMessage());
            logger.debug("Request failed with Exception " + generalException.getMessage());
            return ResponseEntity.badRequest().body(submitReceiptFailureResponse);
        }
    }
}
