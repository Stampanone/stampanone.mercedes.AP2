package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam String fromAccountNumber,
                                                    @RequestParam String toAccountNumber,
                                                    @RequestParam Double amount,
                                                    @RequestParam String description){
        Client client = clientService.findByEmail(authentication.getName());

        if (fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("403, no puede ingresar misma cuenta", HttpStatus.FORBIDDEN);
        }
        if (!accountService.existsByNumber(fromAccountNumber)){
            return new ResponseEntity<>("403, no exite cuenta de origen", HttpStatus.FORBIDDEN);
        }
        if (!accountService.existsByNumber(toAccountNumber)){
            return new ResponseEntity<>("403, no existe la cuenta de destino",HttpStatus.FORBIDDEN);
        }
        if (description == null){
            return new ResponseEntity<>("403, debe colocar una descripcion", HttpStatus.FORBIDDEN);
        }

        if (amount<= 0){
            return new ResponseEntity<>("403, monto inconsistente",HttpStatus.FORBIDDEN);
        }

        Account accountClient = accountService.findByNumber(fromAccountNumber);
        Account accountDestiny = accountService.findByNumber(toAccountNumber);


        if (!client.getAccounts().contains(accountClient)){
            return new ResponseEntity<>("403, esta cuenta no le pertenece", HttpStatus.FORBIDDEN);
        }

        if (!(accountClient.getBalance() >= amount)){
            return new ResponseEntity<>("403, saldo insuficiente",HttpStatus.FORBIDDEN);
        }
        Transaction transactionOrigen= new Transaction(TransactionType.DEBIT,-amount,description, LocalDate.now());
        Transaction transactionDestiny=new Transaction(TransactionType.CREDIT,amount,description,LocalDate.now());

        accountClient.addTransaction(transactionOrigen);
        accountDestiny.addTransaction(transactionDestiny);

        transactionService.save(transactionOrigen);
        transactionService.save(transactionDestiny);


        accountClient.setBalance((accountClient.getBalance()) - amount);
        accountDestiny.setBalance((accountDestiny.getBalance()) + amount);

        accountService.save(accountClient);
        accountService.save(accountDestiny);
        return new ResponseEntity<>("200, transaccion creada",HttpStatus.CREATED);
    }
}
