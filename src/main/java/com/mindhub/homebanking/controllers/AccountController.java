package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccount(){
        List<Account> allAccount = accountRepository.findAll();

        List<AccountDTO> convertedList = allAccount.stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());

        return convertedList;
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccountById(@PathVariable Long id){
        Optional<Account> account = accountRepository.findById(id);
        return new AccountDTO(account.get());

    }
}
