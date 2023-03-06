package com.jingnu.receipt.processor;

import com.jingnu.receipt.processor.exception.ReceiptNotFoundException;
import com.jingnu.receipt.processor.exception.ValidationException;
import com.jingnu.receipt.processor.model.*;
import com.jingnu.receipt.processor.service.ReceiptService;
import com.jingnu.receipt.processor.validator.ReceiptValidator;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    Map<String, Receipt> receipts = new HashMap<>();

    @GetMapping(value="/{id}/points")
    public ResponseEntity<GetPointsResponse> getPoints(@PathVariable("id") String id) throws ReceiptNotFoundException {
        if(!receipts.containsKey(id)) {
            getPointsFailureResponse.setErrorMessage(ErrorMessage.RESOURCE_NOT_FOUND.getMessage());
            return  ResponseEntity.ok(getPointsFailureResponse);
        }
        Integer points = receiptService.calculatePoints(receipts.get(id));
        getPointsSuccessResponse.setPoints(points);
        return ResponseEntity.ok(getPointsSuccessResponse);
    }

    /**
     * Process a receipt received from a POST request sent to /receipts/process
     * @param requestBody
     * @return
     */
    @PostMapping(value="/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubmitReceiptResponse> submitReceipt(@RequestBody String requestBody) {
        try {
            receiptValidator.validateReceipt(requestBody);
            Receipt result = receiptService.createReceiptFromInput(requestBody);
            String id = receiptService.generateReceiptId();
            submitReceiptSuccessResponse.setId(id);

            receipts.put(id, result);

            return ResponseEntity.ok(submitReceiptSuccessResponse);
        } catch (JSONException e) {
            String errorMessage = String.format(ErrorMessage.JSON_EXCEPTION_MSG.getMessage(), e.getMessage());
            submitReceiptFailureResponse.setErrorMessage(errorMessage);
            return ResponseEntity.badRequest().body(submitReceiptFailureResponse);
        } catch (ValidationException validationException) {
            submitReceiptFailureResponse.setErrorMessage(validationException.getMessage());
            return ResponseEntity.badRequest().body(submitReceiptFailureResponse);
        }

    }

}
