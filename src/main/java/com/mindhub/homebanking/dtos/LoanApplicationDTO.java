package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    //debe tener id del préstamo, monto, cuotas y número de cuenta de destino.
    private Long loanId;
    private Double amount;
    private int payments;
    private String toAccountNumber;

    public LoanApplicationDTO(Long loanId, Double amount, int payments, String toAccountNumber){
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber =toAccountNumber;
    }

    public Long getLoanId() {return loanId;}

    public Double getAmount() {return amount;}

    public int getPayments() {return payments;}

    public String getToAccountNumber() {return toAccountNumber;}
}
