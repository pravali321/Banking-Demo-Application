package com.Banking.demo.mapper.impl;

import com.Banking.demo.entity.Account;
import com.Banking.demo.mapper.AccountMapper;
import com.Banking.demo.model.account.AccountResponseModel;
import org.springframework.stereotype.Component;

@Component
public class AccountMapperImpl implements AccountMapper {
    @Override
    public AccountResponseModel toResponseModel(Account account) {
        return AccountResponseModel
                .builder()
                .card_number(account.getCardNumber())
                .cvv(account.getCvv())
                .balance(account.getBalance())
                .build();
    }
}