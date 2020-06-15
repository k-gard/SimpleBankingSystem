package com.company;

import java.util.Random;

public class Account {
    private long cardNumber;
    private String pin;
    private long balance;

    public Account(long cardNumber, String pin ,long balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public Account() {
        Random r = new Random();
        String c = "400000";
        for (int i = 0 ; i < 9 ; i++){
            c += Integer.toString(r.nextInt(10));
        }
        cardNumber = applyLuhnAlgorithm(c);
        verifyCardNumber(String.valueOf(cardNumber));
        String p = "";
        for (int i = 0 ; i < 4 ; i++){
            p += Integer.toString(r.nextInt(10));
        }
        pin = p;
        balance = 0;
    }

    public static boolean verifyCardNumber(String cardNumber) {

        int[] numbers = new int[cardNumber.length()];
        int sum = 0;
        String str = "";

        for (int i = 0; i < numbers.length ; i++){
            numbers[i] = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            if ( (i +1) % 2 != 0){
                numbers[i] = 2 * numbers[i];
                numbers[i] = (numbers[i] > 9) ? numbers[i] - 9 : numbers[i] ;
            }

            sum += numbers[i];

        }


        return (sum % 10 == 0);
    }


    public static long applyLuhnAlgorithm(String number) {
        int[] numbers = new int[number.length()];
        int sum = 0;
        String str = "";
        int remainder;
        for (int i = 0; i < number.length() ; i++){
            numbers[i] = Integer.parseInt(String.valueOf(number.charAt(i)));
            if ( (i +1) % 2 != 0){
                numbers[i] = 2 * numbers[i];
                numbers[i] = (numbers[i] > 9) ? numbers[i] - 9 : numbers[i] ;
            }

            sum += numbers[i];

        }

        remainder = 10 - sum % 10;
        number += String.valueOf(remainder);
        return Long.parseLong(number);
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
