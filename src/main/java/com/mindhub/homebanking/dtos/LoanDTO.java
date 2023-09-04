package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private Long id;
    private String name;
    private Integer maxAmoun;
    private List<Integer> payments;

    public LoanDTO (Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmoun = loan.getMaxAmoun();
        this.payments = loan.getPayments();
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
}
