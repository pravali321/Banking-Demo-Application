package com.Banking.demo.mapper;

import com.Banking.demo.entity.Account;
import com.Banking.demo.entity.Transaction;
import com.Banking.demo.entity.TransactionType;
import com.Banking.demo.model.transaction.TransactionResponseModel;

public interface TransactionMapper {
    Transaction toEntity(double amount, Account account, TransactionType type);
    TransactionResponseModel toResponseModel(Long id, double amount, double balance);
}
