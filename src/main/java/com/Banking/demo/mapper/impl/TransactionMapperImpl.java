package com.Banking.demo.mapper.impl;

import com.Banking.demo.entity.Account;
import com.Banking.demo.entity.Transaction;
import com.Banking.demo.entity.TransactionType;
import com.Banking.demo.mapper.TransactionMapper;
import com.Banking.demo.model.transaction.TransactionResponseModel;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public Transaction toEntity(double amount, Account account, TransactionType type) {
        return Transaction
                .builder()
                .amount(amount)
                .account(account)
                .type(type)
                .timestamp(new Date())
                .notes("Account Balance" + account.getBalance())
                .build();
    }

    @Override
    public TransactionResponseModel toResponseModel(Long id, double amount, double balance) {
        return TransactionResponseModel
                .builder()
                .id(id)
                .amount(amount)
                .balance(balance)
                .build();
    }
}
