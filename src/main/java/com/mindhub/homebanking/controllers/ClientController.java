package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public List<ClientDTO> getClient(){
       /* List<Client> allClients = clientRepository.findAll();

        List<ClientDTO> convertedList = allClients.stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());

        return convertedList;
        forma larga
        */
        //return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList()); forma optimizada
        return clientService.getClientsDTO();
    }



    @GetMapping("/clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id){
       /* Optional<Client>client = clientRepository.findById(id);
        return new ClientDTO(client.get());
        */

        return clientService.getClientDTO(id);

    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName,@RequestParam String email, @RequestParam String password) {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        client.addAccounts(accountService.saveAccount(new Account("VIN-"+ getRandomNumber(0,99999999) , LocalDate.now(),0.0)));
        clientService.saveClient(client);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getAll(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }


}
