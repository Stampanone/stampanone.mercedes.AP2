package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClientsDTO();
    Client findById(Long id);
    Client findByEmail(String email);
    ClientDTO getClientDTO (Long id);
    void saveClient (Client client);
}
