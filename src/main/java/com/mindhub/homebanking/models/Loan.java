package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private Integer maxAmoun;
    @ElementCollection
    @Column(name="payment")
    private List<Integer> payments;
    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan() {
    }

    public Loan(String name, Integer maxAmoun, List<Integer> payments) {
        this.name = name;
        this.maxAmoun = maxAmoun;
        this.payments = payments;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMaxAmoun() {
        return maxAmoun;
    }

    public List<Integer> getPayments() {
        return payments;
    }
    @JsonIgnore
    public Set<ClientLoan> getClients() {
        return clientLoans;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxAmoun(Integer maxAmoun) {
        this.maxAmoun = maxAmoun;
    }
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }
}
