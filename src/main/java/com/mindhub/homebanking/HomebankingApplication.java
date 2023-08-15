package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;



@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository,CardRepository cardRepository){
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

			Loan hipotecario= new Loan("Hipotecario",500000,List.of(12,24,36,48,60));
			Loan personal = new Loan ("Personal",100000,List.of(6,12,24));
			Loan automotriz = new Loan ("Automotriz", 300000,List.of(6,12,24,36));

			loanRepository. save(hipotecario);
			loanRepository. save(personal);
			loanRepository. save(automotriz);

			ClientLoan clientLoan1 = new ClientLoan(400000.0,60);
			ClientLoan clientLoan2 = new ClientLoan(50000.0,12);
			ClientLoan clientLoan3 = new ClientLoan(100000.0,24);
			ClientLoan clientLoan4 = new ClientLoan(200000.0,36);

			hipotecario.addClientLoan(clientLoan1);
			personal.addClientLoan(clientLoan2);
			personal.addClientLoan(clientLoan3);
			automotriz.addClientLoan(clientLoan4);

			client.addClientLoan(clientLoan1);
			client.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(CardType.DEBIT,client.getFirstName()+ " "+ client.getLastName(),"4556-1598-7887-6009",456,LocalDate.now(),LocalDate.now().plusYears(5),CardColor.GOLD);
			Card card2 = new Card(CardType.CREDIT,client.getFirstName()+ " "+ client.getLastName(),"4789-1234-5587-7887",789,LocalDate.now(),LocalDate.now().plusYears(5),CardColor.TITANIUM);
			Card card3 = new Card(CardType.CREDIT,client2.getFirstName()+ " "+ client2.getLastName(),"1546-7564-1548-6548",735,LocalDate.now(),LocalDate.now().plusYears(5),CardColor.SILVER);

			client.addCards(card1);
			client.addCards(card2);
			client2.addCards(card3);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
		};
	}

}
