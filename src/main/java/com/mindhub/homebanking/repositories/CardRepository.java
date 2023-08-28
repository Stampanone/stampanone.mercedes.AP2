package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    //se podria usar, pero preferimos no darle toda la responsabilidad a la base de datos
    boolean existsByTypeAndColor(CardType cardType, CardColor cardColor);
}
