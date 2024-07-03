package com.Banking.demo.mapper;

import com.Banking.demo.entity.Account;
import com.Banking.demo.model.account.AccountResponseModel;

public interface AccountMapper {
    AccountResponseModel toResponseModel(Account account);
}
