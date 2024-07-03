package com.Banking.demo.controller;

import com.Banking.demo.model.ResponseModel;
import com.Banking.demo.model.transaction.DepositRequestModel;
import com.Banking.demo.model.transaction.WithdrawRequestModel;
import com.Banking.demo.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<ResponseModel> deposit(@Valid @RequestBody DepositRequestModel request) {
        return ResponseEntity.ok(
                ResponseModel
                        .builder()
                        .status(HttpStatus.OK)
                        .success(true)
                        .data(transactionService.deposit(request))
                        .build()
        );
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ResponseModel> withdraw(@Valid @RequestBody WithdrawRequestModel request) {
        return ResponseEntity.ok(
                ResponseModel
                        .builder()
                        .status(HttpStatus.OK)
                        .success(true)
                        .data(transactionService.withdraw(request))
                        .build()
        );
    }
}