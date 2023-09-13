package com.mindhub.homebanking.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CardUtilsTest {

    @Test
    void getCardNumber() {
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    void getCVV() {
        int cvv = CardUtils.getCVV();
        assertThat(cvv,notNullValue());
    }
}