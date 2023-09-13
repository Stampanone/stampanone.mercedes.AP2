package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccount(){
        return accountService.getAccountsDTO();
    }

    /*@GetMapping("/accounts/{id}")
    public AccountDTO getAccountById(@PathVariable Long id){
        return new AccountDTO(accountRepository.findById(id).orElse(null));
    }*/

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccountById(Authentication authentication, @PathVariable Long id) {
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByIdAndOwner(id,client);
        if (account == null){
            return new ResponseEntity<>("No autorizado",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new AccountDTO(account),HttpStatus.ACCEPTED);
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> creatAccount(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        String numberRamdom ="VIN-" + getRandomNumber(0,99999999);
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("403 Prohibido, max 3 accounts", HttpStatus.FORBIDDEN);
        }
        if (!accountService.existsByNumber(numberRamdom)) {
            client.addAccounts(accountService.saveAccount(new Account( numberRamdom, LocalDate.now(), 0.0)));
            clientService.saveClient(client);
        }

        return new ResponseEntity<>("201 Creada", HttpStatus.CREATED);
    }
    @DeleteMapping("/clients/current/account/delete/{id}")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @PathVariable Long id) {
        Client client = clientService.findByEmail(authentication.getName());
        Account account = (Account) client.getAccounts().stream().filter(account1 -> account1.getId().equals(id));
        if (!(account == null)){
            accountService.delete(id);
            Set<Transaction> transactions = account.getTransactions();
            //eliminar TODAS las transacciones
            return new ResponseEntity<>("200, Delete Account",HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("403, Not delete account",HttpStatus.FORBIDDEN);
    }
}
