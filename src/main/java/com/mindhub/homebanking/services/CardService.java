package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

public interface CardService {
    void save (Card card);
    Card saveCard (Card card);
    void delete (Long id);
}
