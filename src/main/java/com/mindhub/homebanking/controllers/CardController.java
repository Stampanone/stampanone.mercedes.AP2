package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
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
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCards (Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        return client.getCards().stream().map(CardDTO ::new).collect(Collectors.toList());
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication,
                                             @RequestParam CardType cardType,
                                             @RequestParam CardColor cardColor) {
        Client client = clientService.findByEmail(authentication.getName());
        Set<Card> cards = client.getCards();
        if (cardType == null || cardColor == null){
            return new ResponseEntity<>("403, Algun campo vacio", HttpStatus.FORBIDDEN);
        }

        if(!cards.stream().filter(card -> card.getType().equals(cardType))
                         .filter(card -> card.getColor().equals(cardColor)).collect(Collectors.toList()).isEmpty()){
            return new ResponseEntity<>("403 Prohibido, cannot have more than one card color", HttpStatus.FORBIDDEN);
        }

       /* if (client.getCards().stream().filter(card -> card.getType() == cardType).count()>= 3){
            return new ResponseEntity<>("403 Prohibido, max 3 cards " + cardType, HttpStatus.FORBIDDEN);
        }*/


        client.addCards(cardService.saveCard(new Card(cardType, client.getFirstName()+ " "+ client.getLastName(),getRandomNumber(0,9999)+"-"+getRandomNumber(0,9999)+"-"+getRandomNumber(0,9999)+"-"+getRandomNumber(0,9999),getRandomNumber(100,999),LocalDate.now(),LocalDate.now().plusYears(5), cardColor)));
        clientService.saveClient(client);
        return new ResponseEntity<>("201 Creada", HttpStatus.CREATED);
    }
}
