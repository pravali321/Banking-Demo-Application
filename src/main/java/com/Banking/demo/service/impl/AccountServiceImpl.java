package com.Banking.demo.service.impl;

import com.Banking.demo.entity.Account;
import com.Banking.demo.entity.User;
import com.Banking.demo.mapper.AccountMapper;
import com.Banking.demo.model.account.AccountResponseModel;
import com.Banking.demo.repository.AccountRepository;
import com.Banking.demo.repository.UserRepository;
import com.Banking.demo.service.AccountService;
import com.Banking.demo.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponseModel createNewAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User " + email + " Not Found"));

        Account account = accountRepository.save(
            Account
                .builder()
                .cardNumber(generateUniqueCardNumber())
                .cvv(Utils.generateCVV())
                .balance(0.0)
                .user(user)
                .build()
        );

        return accountMapper.toResponseModel(account);
    }

    @Override
    public List<AccountResponseModel> getMyAccounts() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User " + email + " Not Found"));

        return accountRepository
                .findAllByUser(user)
                .stream()
                .map(accountMapper::toResponseModel)
                .toList();
    }

    public String generateUniqueCardNumber() {
        String cardNumber = Utils.generateCardNumber();

        while (accountRepository.existsByCardNumber(cardNumber)) {
            cardNumber = Utils.generateCardNumber();
        }

        return cardNumber;
    }
}
