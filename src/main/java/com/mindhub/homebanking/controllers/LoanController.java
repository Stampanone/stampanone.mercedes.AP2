package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private LoanService loanService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans (){
        return loanService.getLoansDTO();
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        Loan loan = loanService.findById(loanApplicationDTO.getLoanId());
        List<Integer> payments = loan.getPayments();

        if(loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayments() == 0){
            return new ResponseEntity<>("403, Campos vacios",HttpStatus.FORBIDDEN);
        }
        if (loan== null){
            return new ResponseEntity<>("403, No existe el prestamo",HttpStatus.FORBIDDEN);
        }
        if (loan.getMaxAmoun() < loanApplicationDTO.getAmount()){
            return new ResponseEntity<>("403, Monto exedido",HttpStatus.FORBIDDEN);
        }
        if (!payments.contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("403, No se encuentra cantidad de cuotas",HttpStatus.FORBIDDEN);
        }
        if (client.getAccounts().contains(loanApplicationDTO.getToAccountNumber())){
            return new ResponseEntity<>("403, La cuenta no existe",HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()+((20.0/100.0)*loanApplicationDTO.getAmount()),loanApplicationDTO.getPayments());
        loan.addClientLoan(clientLoan);
        client.addClientLoan(clientLoan);
        clientService.saveClient(client);

        Account accountDestiny = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());
        Transaction transactionDestiny=new Transaction(TransactionType.CREDIT,loanApplicationDTO.getAmount(),loan.getName()+" loan approved", LocalDate.now());
        accountDestiny.addTransaction(transactionDestiny);
        transactionService.save(transactionDestiny);
        accountDestiny.setBalance((accountDestiny.getBalance()) + loanApplicationDTO.getAmount());
        accountService.save(accountDestiny);

        return new ResponseEntity<>("Aceptado", HttpStatus.ACCEPTED);
    }
}
