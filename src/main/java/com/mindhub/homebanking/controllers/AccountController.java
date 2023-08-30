package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccount(){
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    /*@GetMapping("/accounts/{id}")
    public AccountDTO getAccountById(@PathVariable Long id){
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }*/

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccountById(Authentication authentication, @PathVariable Long id) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByIdAndOwner(id,client);
        if (account == null){
            return new ResponseEntity<>("No autorizado",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new AccountDTO(account),HttpStatus.ACCEPTED);
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> creatAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        String numberRamdom ="VIN-" + getRandomNumber(0,99999999);
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("403 Prohibido, max 3 accounts", HttpStatus.FORBIDDEN);
        }
        if (!accountRepository.existsByNumber(numberRamdom)) {
            client.addAccounts(accountRepository.save(new Account( numberRamdom, LocalDate.now(), 0.0)));
            clientRepository.save(client);
        }

        return new ResponseEntity<>("201 Creada", HttpStatus.CREATED);
    }
}
