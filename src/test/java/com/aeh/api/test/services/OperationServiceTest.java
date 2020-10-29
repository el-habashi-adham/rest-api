package com.aeh.api.test.services;

import com.aeh.api.models.Entry;
import com.aeh.api.payloads.ApiResponse;
import com.aeh.api.repositories.EntryRepository;
import com.aeh.api.services.OperationService;
import com.aeh.api.utils.Pair;
import com.aeh.api.validations.InputValidator;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
//@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OperationServiceTest {

    private OperationService operationService;

    @Mock
    private EntryRepository entryRepository;

    @Mock
    private InputValidator inputValidator;

    @Before
    public void setUp() {
        operationService = new OperationService(entryRepository, inputValidator);
    }

    @Test
    public void getAllEntriesReturnsEmptyList() {
        BDDMockito.given(this.entryRepository.findAll()).willReturn(List.of());
        Assert.assertThat(operationService.getAllEntries(), Is.is(ResponseEntity.ok(List.of())));
    }

    @Test
    public void addReturnsSuccess() {
        HashMap<String, Integer> requestBody = new HashMap<>();
        requestBody.put("100", 1);

        Entry entry = new Entry(100, 1);
        Optional<Integer> count = Optional.of(3);
        Pair<Boolean, String> result = new Pair<Boolean, String>(true, "The operation completed successfully");
        ApiResponse apiResponse = new ApiResponse(result.getFirst(), result.getSecond());

        BDDMockito.given(this.entryRepository.getCount()).willReturn(count);
        BDDMockito.given(this.entryRepository.findByDenomination(entry.getDenomination())).willReturn(entry);
        BDDMockito.given(this.entryRepository.save(entry)).willReturn(entry);
        BDDMockito.given(this.inputValidator.validateInput(requestBody, count.get())).willReturn(result);

        Assert.assertThat(operationService.add(requestBody), Is.is(ResponseEntity.ok(apiResponse)));
    }

    @Test
    public void addReturnsErrorNegativeInput() {
        HashMap<String, Integer> requestBody = new HashMap<>();
        requestBody.put("100", 1);

        Entry entry = new Entry(100, -1);
        Optional<Integer> count = Optional.of(3);
        Pair<Boolean, String> result = new Pair<Boolean, String>(false, "Input values cannot be negative numbers");
        ApiResponse apiResponse = new ApiResponse(result.getFirst(), result.getSecond());

        BDDMockito.given(this.entryRepository.getCount()).willReturn(count);
        BDDMockito.given(this.entryRepository.findByDenomination(entry.getDenomination())).willReturn(entry);
        BDDMockito.given(this.entryRepository.save(entry)).willReturn(entry);
        BDDMockito.given(this.inputValidator.validateInput(requestBody, count.get())).willReturn(result);

        Assert.assertThat(operationService.add(requestBody), Is.is(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)));
    }

    @Test
    public void withdrawReturnsSuccess() {

        Entry entry = new Entry(100, 1);
        Optional<Integer> count = Optional.of(3);
        Pair<Boolean, String> result = new Pair<Boolean, String>(true, "{100=1}");
        ApiResponse apiResponse = new ApiResponse(result.getFirst(), result.getSecond());

        BDDMockito.given(this.entryRepository.findAll()).willReturn(List.of(entry));
        BDDMockito.given(this.entryRepository.findByDenomination(entry.getDenomination())).willReturn(entry);
        BDDMockito.given(this.entryRepository.save(entry)).willReturn(entry);

        Assert.assertThat(operationService.withdraw(100), Is.is(ResponseEntity.ok(apiResponse)));
    }

    @Test
    public void withdrawReturnsErrorInsufficientFunds() {
        Entry entry = new Entry(100, 3);
        Optional<Integer> count = Optional.of(3);
        Pair<Boolean, String> result = new Pair<Boolean, String>(false, "The ATM has insufficient funds");
        ApiResponse apiResponse = new ApiResponse(result.getFirst(), result.getSecond());

        BDDMockito.given(this.entryRepository.findAll()).willReturn(List.of(entry));
        BDDMockito.given(this.entryRepository.findByDenomination(entry.getDenomination())).willReturn(entry);
        BDDMockito.given(this.entryRepository.save(entry)).willReturn(entry);

        Assert.assertThat(operationService.withdraw(400), Is.is(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)));
    }

    @Test
    public void withdrawReturnsErrorNegativeInput() {
        Entry entry = new Entry(100, 3);
        ApiResponse apiResponse = new ApiResponse(false, "Input values cannot be negative numbers");

        BDDMockito.given(this.entryRepository.findAll()).willReturn(List.of(entry));
        BDDMockito.given(this.entryRepository.findByDenomination(entry.getDenomination())).willReturn(entry);
        BDDMockito.given(this.entryRepository.save(entry)).willReturn(entry);

        Assert.assertThat(operationService.withdraw(-100), Is.is(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)));
    }

    @Test
    public void withdrawReturnsIncorrectDenomination() {
        Entry entry = new Entry(100, 3);
        ApiResponse apiResponse = new ApiResponse(false, "The ATM does not have the correct denomination to complete the request");

        BDDMockito.given(this.entryRepository.findAll()).willReturn(List.of(entry));
        BDDMockito.given(this.entryRepository.findByDenomination(entry.getDenomination())).willReturn(entry);
        BDDMockito.given(this.entryRepository.save(entry)).willReturn(entry);

        Assert.assertThat(operationService.withdraw(60), Is.is(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse)));
    }
}
