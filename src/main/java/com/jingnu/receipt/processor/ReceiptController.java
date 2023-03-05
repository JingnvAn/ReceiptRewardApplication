package com.jingnu.receipt.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jingnu.receipt.processor.exception.ValidationException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    @Autowired
    ReceiptService receiptService;


    @GetMapping(value="/{id}/points")
    public ResponseEntity<String> getPoints(@PathVariable("id") String id) {
        String response = "dummy 123";
        return ResponseEntity.ok(response);
    }

    /**
     * Process a receipt received from a POST request sent to /receipts/process
     * @param requestBody
     * @return
     */
    @PostMapping(value="/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> submitReceipt(@RequestBody String requestBody) {
        try {

            receiptService.validateReceipt(requestBody);



            return ResponseEntity.ok("success");
        } catch (JSONException e) {
            return ResponseEntity.badRequest().body(String.format(ErrorMessage.JSON_EXCEPTION_MSG.getMessage(), e.getMessage()));
        } catch (ValidationException validationException) {
            return ResponseEntity.badRequest().body(validationException.getMessage());
        }

    }

}
