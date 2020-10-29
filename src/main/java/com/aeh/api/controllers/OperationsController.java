package com.aeh.api.controllers;

import com.aeh.api.models.Entry;
import com.aeh.api.payloads.ApiResponse;
import com.aeh.api.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/operations")
public class OperationsController {

    private OperationService operationService;

    @Autowired
    public OperationsController(final OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Entry>> getAll() {
        return operationService.getAllEntries();
    }

    @GetMapping("/withdraw/{amount}")
    public ResponseEntity<ApiResponse> withdrawCash(@PathVariable Integer amount) {
        return operationService.withdraw(amount);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCash(@RequestBody HashMap<String, Integer> requestBody) {
        return operationService.add(requestBody);
    }
 }
