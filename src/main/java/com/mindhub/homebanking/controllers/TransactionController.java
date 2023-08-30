package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Authentication authentication,
                                                    @RequestParam String fromAccountNumber,
                                                    @RequestParam String toAccountNumber,
                                                    @RequestParam Double amount,
                                                    @RequestParam String description){
        Client client = clientRepository.findByEmail(authentication.getName());

        if (fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("403, no puede ingresar misma cuenta", HttpStatus.FORBIDDEN);
        }
        if (!accountRepository.existsByNumber(fromAccountNumber)){
            return new ResponseEntity<>("403, no exite cuenta de origen", HttpStatus.FORBIDDEN);
        }
        if (!accountRepository.existsByNumber(toAccountNumber)){
            return new ResponseEntity<>("403, no existe la cuenta de destino",HttpStatus.FORBIDDEN);
        }
        if (description == null){
            return new ResponseEntity<>("403, debe colocar una descripcion", HttpStatus.FORBIDDEN);
        }

        if (amount<= 0){
            return new ResponseEntity<>("403, monto inconsistente",HttpStatus.FORBIDDEN);
        }

        Account accountClient = accountRepository.findByNumber(fromAccountNumber);
        Account accountDestiny = accountRepository.findByNumber(toAccountNumber);


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

        transactionRepository.save(transactionOrigen);
        transactionRepository.save(transactionDestiny);

        Double balanceAccountsClient = accountClient.getBalance();
        Double balanceAccountsDestiny = accountDestiny.getBalance();

        accountClient.setBalance(balanceAccountsClient - amount);
        accountDestiny.setBalance(balanceAccountsDestiny + amount);

        accountRepository.save(accountClient);
        accountRepository.save(accountDestiny);
        return new ResponseEntity<>("200, transaccion creada",HttpStatus.CREATED);
    }
}
