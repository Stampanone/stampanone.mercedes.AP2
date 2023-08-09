package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return(args) -> {
			Client client = new Client("Melba","Morel", "melba@mindhub.com");
			Client client2 = new Client("Juli","Stampanone", "black@gmail.com");

			clientRepository.save(client);
			clientRepository.save(client2);

			Account account = new Account("VIN001", LocalDate.now(),5000.0);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.0);
			Account account3 = new Account("VIN003", LocalDate.now(),8000.0);

			client2.addAccounts(account3);
			client.addAccounts(account);
			client.addAccounts(account2);

			accountRepository.save(account);
			accountRepository.save(account2);
			accountRepository.save(account3);

			Transaction transaction = new Transaction(TransactionType.CREDIT,2000.0,"Deposito",LocalDate.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT,-200.0,"Pago",LocalDate.now());
			Transaction transaction3 = new Transaction(TransactionType.CREDIT,1000.0,"Deposito",LocalDate.now());

			account.addTransaction(transaction);
			account2.addTransaction(transaction2);
			account3.addTransaction(transaction3);

			transactionRepository.save(transaction);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);

		};
	}

}
