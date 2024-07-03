package com.Banking.demo.service;

import com.Banking.demo.model.account.AccountResponseModel;

import java.util.List;

public interface AccountService {
    AccountResponseModel createNewAccount();

    List<AccountResponseModel> getMyAccounts();
}
