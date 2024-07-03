package com.Banking.demo.service;

import com.Banking.demo.model.transaction.DepositRequestModel;
import com.Banking.demo.model.transaction.TransactionResponseModel;
import com.Banking.demo.model.transaction.WithdrawRequestModel;

public interface TransactionService {
    TransactionResponseModel deposit(DepositRequestModel request);
    TransactionResponseModel withdraw(WithdrawRequestModel request);
}