package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType cardType,@RequestParam CardColor cardColor) {
        Client client = clientRepository.findByEmail(authentication.getName());
        if (client.getCards().size() >= 6 ) {

            return new ResponseEntity<>("403 Prohibido, max 6 cards", HttpStatus.FORBIDDEN);

        }

        client.addCards(cardRepository.save(new Card(cardType, client.getFirstName()+ " "+ client.getLastName(),getRandomNumber(0,9999)+"-"+getRandomNumber(0,9999)+"-"+getRandomNumber(0,9999)+"-"+getRandomNumber(0,9999),getRandomNumber(100,999),LocalDate.now(),LocalDate.now().plusYears(5), cardColor)));
        clientRepository.save(client);
        return new ResponseEntity<>("201 Creada", HttpStatus.CREATED);
    }
}
