package com.Banking.demo.service.impl;

import com.Banking.demo.entity.Account;
import com.Banking.demo.entity.Transaction;
import com.Banking.demo.entity.TransactionType;
import com.Banking.demo.exception.LowBalanceException;
import com.Banking.demo.mapper.TransactionMapper;
import com.Banking.demo.model.transaction.DepositRequestModel;
import com.Banking.demo.model.transaction.TransactionResponseModel;
import com.Banking.demo.model.transaction.WithdrawRequestModel;
import com.Banking.demo.repository.AccountRepository;
import com.Banking.demo.repository.TransactionRepository;
import com.Banking.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionResponseModel deposit(DepositRequestModel request) {
        Account account = accountRepository
                .findByCardNumber(request.getCard_number())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        Long transactionId = performDeposit(account, request.getAmount());

        return transactionMapper.toResponseModel(transactionId, request.getAmount(), account.getBalance());
    }

    @Override
    public TransactionResponseModel withdraw(WithdrawRequestModel request) {
        Account account = accountRepository
                .findByCardNumberAndCvv(request.getCard_number(), request.getCvv())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        Long transactionId = performWithdrawal(account, request.getAmount());

        return transactionMapper.toResponseModel(transactionId, request.getAmount(), account.getBalance());
    }

    private Long performDeposit(Account account, double amount) {
        updateAccountBalance(account, amount);
        Transaction transaction = transactionRepository.save(transactionMapper.toEntity(amount, account, TransactionType.DEPOSIT));
        return transaction.getId();
    }

    private Long performWithdrawal(Account account, double amount) {
        if (account.getBalance() < amount) {
            throw new LowBalanceException("Your Balance " + account.getBalance() + " is not enough to withdraw " + amount);
        }

        updateAccountBalance(account, -amount);
        Transaction transaction = transactionRepository.save(transactionMapper.toEntity(amount, account, TransactionType.WITHDRAW));
        return transaction.getId();
    }

    private void updateAccountBalance(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }
}