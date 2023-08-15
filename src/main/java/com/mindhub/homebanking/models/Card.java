package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private CardType type;
    private String cardHolder;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private CardColor color;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Card() {
    }

    public Card(CardType type,String cardHolder, String number, int cvv, LocalDate fromDate, LocalDate thruDate, CardColor color) {
        this.type = type;
        this.cardHolder = cardHolder;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.color = color;
    }

    public Long getId() {return id;}

    public CardType getType() {return type;}

    public String getCardHolder() {return cardHolder;}

    public String getNumber() {return number;}

    public int getCvv() {return cvv;}

    public LocalDate getFromDate() {return fromDate;}

    public LocalDate getThruDate() {return thruDate;}

    public CardColor getColor() {return color;}

    public Client getClient() {return client;}

    public void setType(CardType type) {this.type = type;}

    public void setCardHolder(String cardHolder) {this.cardHolder = cardHolder;}

    public void setNumber(String number) {this.number = number;}

    public void setCvv(int cvv) {this.cvv = cvv;}

    public void setFromDate(LocalDate fromDate) {this.fromDate = fromDate;}

    public void setThruDate(LocalDate thruDate) {this.thruDate = thruDate;}

    public void setColor(CardColor color) {this.color = color;}

    public void setClient(Client cardHolder) {this.client = cardHolder;}
}
