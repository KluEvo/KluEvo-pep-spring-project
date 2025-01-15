package com.example.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import com.example.repository.AccountRepository;
import com.example.entity.Account;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Method for registering a new account
    public Account registerAccount(Account account) {
        // Check if username is empty or password is too short
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username or password");
        }

        // Check if username already exists
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }

        // Save the account and return it
        return accountRepository.save(account);
    }

    // Method for logging in (verifying username and password)
    public Account login(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isEmpty() || !account.get().getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        return account.get();
    }

    // Method for retrieving an Account by its ID
    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }
}