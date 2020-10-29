package com.aeh.api.services;

import com.aeh.api.models.Entry;
import com.aeh.api.payloads.ApiResponse;
import com.aeh.api.repositories.EntryRepository;
import com.aeh.api.utils.Pair;
import com.aeh.api.validations.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Component
public class OperationService {
    private EntryRepository entryRepository;
    private InputValidator inputValidator;

    @Autowired
    public OperationService(final EntryRepository entryRepository, final InputValidator inputValidator) {
        this.entryRepository = entryRepository;
        this.inputValidator = inputValidator;
    }

    public ResponseEntity<List<Entry>> getAllEntries() {
        return ResponseEntity.ok(entryRepository.findAll());
    }

    public ResponseEntity<ApiResponse> withdraw(Integer amount) {
        HashMap<Integer, Integer> responseBody = new HashMap<>();
        // all available denominations in the ATM
        List<Integer> denominationList = entryRepository.findAll().stream().map(Entry::getDenomination).collect(Collectors.toList());
        // current values in the database
        Map<Integer, Integer> currentEntries = entryRepository.findAll().stream().collect(Collectors.toMap(Entry::getDenomination, Entry::getAmount));

        denominationList.sort(Collections.reverseOrder());

        // total value of all the bills in the ATM
        int currentAmount = 0;
        int denominationCount = 0;

        for(Map.Entry<Integer, Integer> e : currentEntries.entrySet()) {
            currentAmount += e.getKey() * e.getValue();
        }

        // check if the ATM has sufficient funds
        if(amount > currentAmount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "The ATM has insufficient funds"));
        }

        if(amount < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Input values cannot be negative numbers"));
        }

        for(int i = 0; i < denominationList.size() && amount != 0; ++i) {
            if(amount >= denominationList.get(i)) {
                denominationCount = amount/denominationList.get(i);

                // check if there are enough bills of a specific denomination in the ATM; if false, check the next denomination
                if(currentEntries.get(denominationList.get(i)) - denominationCount < 0) {
                    continue;
                }

                // update the values in the database
                Entry entryToUpdate = entryRepository.findByDenomination(denominationList.get(i));
                entryToUpdate.setAmount(entryToUpdate.getAmount() - denominationCount);
                entryRepository.save(entryToUpdate);

                // update the response body
                responseBody.put(denominationList.get(i), denominationCount);
            }
            amount = amount % denominationList.get(i);
        }

        // if at the end of the computation the denominationCount equals zero, it means the the ATM does not have the correct bills to complete the request
        if(denominationCount == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "The ATM does not have the correct denomination to complete the request"));
        }

        return ResponseEntity.ok(new ApiResponse(true, responseBody.toString()));
    }

    public ResponseEntity<ApiResponse> add(HashMap<String, Integer> requestBody) {
        Optional<Integer> currentCapacity = entryRepository.getCount();
        Pair<Boolean, String> result;

        if(currentCapacity.isPresent()) {
             result = inputValidator.validateInput(requestBody, currentCapacity.get());
        } else {
            result = inputValidator.validateInput(requestBody, 0);
        }

        if(!result.getFirst()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(result.getFirst(), result.getSecond()));
        }

        for(Map.Entry<String, Integer> row: requestBody.entrySet()) {
            Entry entryToUpdate = entryRepository.findByDenomination(Integer.parseInt(row.getKey()));

            // if the denomination is not found, create entry
            if(entryToUpdate == null) {
                entryToUpdate = new Entry(Integer.parseInt(row.getKey()), row.getValue());
            } else {
                // update entry
                entryToUpdate.setAmount(entryToUpdate.getAmount() + row.getValue());
            }

            entryRepository.save(entryToUpdate);
        }

        return ResponseEntity.ok(new ApiResponse(result.getFirst(), result.getSecond()));
    }

}
