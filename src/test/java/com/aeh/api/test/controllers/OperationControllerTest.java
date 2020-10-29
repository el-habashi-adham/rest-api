package com.aeh.api.test.controllers;

import com.aeh.api.controllers.OperationsController;
import com.aeh.api.models.Entry;
import com.aeh.api.payloads.ApiResponse;
import com.aeh.api.services.OperationService;
import org.apiguardian.api.API;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class OperationControllerTest {
    private OperationsController operationsController;

    private final ApiResponse apiResponseAdd = new ApiResponse(true, "The operation completed successfully");
    private final ApiResponse apiResponseWithdraw = new ApiResponse(true, "{100=1}");

    @Mock
    private OperationService operationService;

    @Before
    public void setUp() {
        this.operationsController = new OperationsController(operationService);
    }

    @Test
    public void shouldReturnAllEntriesSuccess() {
        BDDMockito.given(this.operationService.getAllEntries()).willReturn(ResponseEntity.ok(List.of()));
        Assert.assertThat(operationsController.getAll(), Is.is(ResponseEntity.ok(List.of())));
    }

    @Test
    public void shouldReturnAddSuccess() {
        HashMap<String, Integer> requestBody = new HashMap<>();
        requestBody.put("100", 1);

        BDDMockito.given(this.operationService.add(requestBody)).willReturn(ResponseEntity.ok(this.apiResponseAdd));
        Assert.assertThat(operationsController.addCash(requestBody), Is.is(ResponseEntity.ok(apiResponseAdd)));
    }

    @Test
    public void shouldReturnWithdrawSuccess() {
        Integer amount = 100;

        BDDMockito.given(this.operationService.withdraw(amount)).willReturn(ResponseEntity.ok(this.apiResponseWithdraw));
        Assert.assertThat(operationsController.withdrawCash(amount), Is.is(ResponseEntity.ok(apiResponseWithdraw)));
    }
}
