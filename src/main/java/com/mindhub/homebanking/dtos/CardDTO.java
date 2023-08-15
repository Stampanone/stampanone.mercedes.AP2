package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private CardType type;
    private String cardHolder;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private CardColor color;

    public CardDTO (Card card){
        this.id= card.getId();
        this.type = card.getType();
        this.cardHolder = card.getCardHolder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.color = card.getColor();
    }

    public Long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public String getCardHolder() {return cardHolder;}

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public CardColor getColor() {
        return color;
    }
}
