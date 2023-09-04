package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccountsDTO();
    void save (Account account);
    Account saveAccount (Account account);
    Account findByIdAndOwner(Long id, Client client);
    boolean existsByNumber(String number);

    Account findByNumber(String number);
}
