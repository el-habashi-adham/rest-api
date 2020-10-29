package com.aeh.api.validations;


import com.aeh.api.utils.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InputValidator {

    @Value("${constraints.max-pairs}")
    private int maxInputPairs;

    @Value("${constraints.max-denomination}")
    private int maxDenomination;

    @Value("${constraints.max-capacity}")
    private int maxCapacity;

    public Pair<Boolean, String> validateInput(HashMap<String, Integer> input, Integer currentCount) {

        int requestCount = input.values().stream().mapToInt(Integer::intValue).sum();
        int requestPairs = input.size();
        int requestMaxDenomination = input.keySet().stream().mapToInt(Integer::parseInt).sum();

        if((currentCount + requestCount > maxCapacity) || (currentCount + requestCount < 1)) {
            return new Pair<Boolean, String>(false, "The request exceeds the max capacity of the ATM");
        }

        if(requestPairs > maxInputPairs || requestPairs < 1) {
            return new Pair<Boolean, String>(false, "The request exceeds the max number of operations");
        }

        if(requestMaxDenomination > maxDenomination || requestMaxDenomination < 1) {
            return new Pair<Boolean, String>(false, "The request exceeds the max denomination supported by the ATM");
        }

        // check for negative input numbers
        for(Map.Entry<String, Integer> entry : input.entrySet()) {
            if(Integer.parseInt(entry.getKey()) < 1 || entry.getValue() < 0) {
                return new Pair<Boolean, String>(false, "Input values cannot be negative numbers");
            }
        }

        return new Pair<Boolean, String>(true, "The operation completed successfully");
    }
}
