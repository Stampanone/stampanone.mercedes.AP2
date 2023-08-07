package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name= "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDate creationDate;
    private Double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client owner;

    public Account() {
    }

    public Account(String number, LocalDate creationDate, Double balance, Client owner) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public Client getOwner() {
        return owner;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }
}
