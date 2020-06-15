package com.company;

import org.sqlite.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static ArrayList<Account> accounts = new ArrayList<>();
    public static void main(String[] args) throws SQLException {
        if (args.length == 0){ System.out.println("You must run with command line argument '0'");
        System.exit(0);}
        if (args[0].contains("-fileName")){

            SQLiteJDBC.create(args[1]);

            }

        EntryMenu();

    }

    private static void EntryMenu() {
        Scanner s= new Scanner(System.in);
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");

        switch (s.nextInt()) {

            case 1:
                createAccount();
                break;

            case 2:
                LogIntoAccount();
                break;
            case 0:
                System.out.println();
                System.out.println("Bye!");
                System.exit(0);
                break;
            default:
                EntryMenu();
        }
    }

    private static void LogIntoAccount() {
        Scanner s= new Scanner(System.in);
        System.out.println("Enter your card number:");
        long cardNumber = s.nextLong();
        System.out.println("Enter your PIN:");
        String pin = s.next();

        Account a = SQLiteJDBC.getAccount(cardNumber,pin);
        if (a != null){
            System.out.println();
            System.out.println("You have successfully logged in!");
                AccountMenu(a);

        }
        else{
            System.out.println();
            System.out.println("Wrong card number or PIN!");
            System.out.println();
            EntryMenu();
        }
    }

    private static void AccountMenu(Account account) {

        System.out.println();
        Scanner s= new Scanner(System.in);
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");

        switch (s.nextInt()) {

            case 1:
                getBalance(account);
                break;

            case 2:
                addIncome(account);
                AccountMenu(SQLiteJDBC.getAccount(account.getCardNumber() ,account.getPin()));
                break;
            case 3:
                doTransfer(account);
                break;

            case 4:
                System.out.println();
                closeAccount(account);
                System.out.println();
                EntryMenu();
                break;
            case 5:
                System.out.println();
                System.out.println("You have successfully logged out!");
                System.out.println();
                EntryMenu();
                break;


            case 0:
                System.out.println("Bye!");
                System.exit(0);
                break;
            default:AccountMenu(account);
        }
    }

    private static void doTransfer(Account account) {
    Scanner s = new Scanner(System.in);
    System.out.println("Transfer");
    System.out.println("Enter card number");
    String cardNumber= s.next();
    if (!checkCardNumber(cardNumber)){
        System.out.println("Probably you made mistake in card number. Please try again!");
        AccountMenu(account);
    }
    Account transferAccount = SQLiteJDBC.getAccount(cardNumber);
    if (transferAccount == null) {
        System.out.println("Such a card does not exist.");
        AccountMenu(account);
    }
    if (transferAccount.getCardNumber() == account.getCardNumber()){
        System.out.println("You can't transfer money to the same account!");
        AccountMenu(account);
    }
    System.out.println("Enter how much money you want to transfer:");
    int amount = s.nextInt();
    if (amount < account.getBalance()) {
    SQLiteJDBC.updateBalance(transferAccount, amount);
    System.out.println("Success");
    AccountMenu(account);
    }

    }





    private static boolean checkCardNumber(String number) {
         return Account.verifyCardNumber(number);
    }

    private static void closeAccount(Account account) {
        SQLiteJDBC.deleteAccount(account);
    }

    private static void addIncome(Account account) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter income:");
        int amount = s.nextInt();
        if(SQLiteJDBC.updateBalance(account,amount)){
        System.out.println("Income was added!");}
    }

    private static void getBalance(Account account) {
        System.out.println();
        System.out.println("Balance: " + account.getBalance());
        AccountMenu(account);
    }

    private static void createAccount() {
        Account account = new Account();
        accounts.add(account);
        SQLiteJDBC.saveAccount(SQLiteJDBC.getLastID()+1,account);
        System.out.println();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(account.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(account.getPin());
        System.out.println();
        EntryMenu();
    }
}
