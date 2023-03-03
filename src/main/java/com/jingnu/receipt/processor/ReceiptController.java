package com.jingnu.receipt.processor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    @GetMapping(value="/{id}/points")
    public ResponseEntity<String> getPoints(@PathVariable("id") String id) {
        String response = "dummy";
        return ResponseEntity.ok(response);
    }

    @PostMapping(value="/process", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> submitReceipt(@RequestBody String data) {

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", "succeed!");

        String formattedJson = responseJson.toString(4);

        return ResponseEntity.ok(formattedJson);
    }


}
