package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return(args) -> {
			Client client = new Client("Melba","Morel", "melba@mindhub.com");
			Client client2 = new Client("Juli","Stampanone", "black@gmail.com");

			clientRepository.save(client);
			clientRepository.save(client2);

			Account account = new Account("VIN001", LocalDate.now(),5000.0,client);
			Account account2 = new Account("VIN002", LocalDate.EPOCH.plusDays(1), 7500.0,client);

			client.addAccounts(account);
			client.addAccounts(account2);

			accountRepository.save(account);
			accountRepository.save(account2);
		};
	}

}
