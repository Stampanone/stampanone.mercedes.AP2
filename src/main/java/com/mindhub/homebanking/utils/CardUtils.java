package com.mindhub.homebanking.utils;

public class CardUtils {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static String getCardNumber (){
        return getRandomNumber(1000,9999)+"-"+getRandomNumber(1000,9999)+"-"+getRandomNumber(1000,9999)+"-"+getRandomNumber(1000,9999);
    }
    public static int getCVV(){return getRandomNumber(100,999);}
}
