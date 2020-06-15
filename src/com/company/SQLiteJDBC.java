package com.company;

import org.sqlite.SQLiteDataSource;


import javax.xml.transform.Result;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SQLiteJDBC {
    private static Connection connection;
    private static String dbname;
    public static void create(String name) throws SQLException {
        dbname = name;
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + name);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //  System.out.println("Opened database successfully");

        stmt = c.createStatement();
     /*   String sql = "CREATE TABLE IF not EXISTS card (" +
                "id INTEGER," +
                "number TEXT," +
                "pin TEXT," +
                "balance INTEGER DEFAULT 0);";*/
        String sql = "CREATE TABLE IF NOT EXISTS card ("
                + "\nid      INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "\nnumber  TEXT,"
                + "\npin     TEXT,"
                + "\nbalance INTEGER DEFAULT 0 "
                + "\n);";
        stmt.executeUpdate(sql);
        stmt.close();
        c.close();
    }

    public static void saveAccount(int id, Account account) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:card.s3db");
            c.setAutoCommit(false);
            //    System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO CARD (ID,NUMBER,PIN,BALANCE) " +
                    "VALUES (" + id + ", " + String.valueOf(account.getCardNumber()) + ", " + account.getPin() + ", " + account.getBalance() + ");";
            stmt.executeUpdate(sql);


            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        // System.out.println("Records created successfully");
    }


    public static int getLastID() {

        Connection c = null;
        Statement stmt = null;
        int id = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:card.s3db");
            c.setAutoCommit(false);
            //  System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CARD WHERE ID = (SELECT MAX(ID) FROM CARD);");
            //    Result = stmt.executeQuery( "SELECT MAX(ID) FROM ACCOUNT ;" );

            while (rs.next()) {
                id = rs.getInt("id");


            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return id;
    }

    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:"+dbname;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    public static void select() {

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:card.s3db");
            c.setAutoCommit(false);
            //  System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CARD;");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("number");
                String pin = rs.getString("pin");
                int balance = rs.getInt("balance");


                System.out.println("ID = " + id);
                System.out.println("NUMBER = " + name);
                System.out.println("PIN = " + pin);
                System.out.println("BALANCE = " + balance);
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        // System.out.println("Operation done successfully");
    }


    public static boolean deleteAccount(Account account) {

        String sql = "DELETE from card WHERE number = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, String.valueOf(account.getCardNumber()));
            // update
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;


    }

    public static boolean updateBalance(Account account, int amount) {
        String sql = "UPDATE card SET balance = ?  "
                + "WHERE number = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, Integer.parseInt(String.valueOf(account.getBalance())) + amount);

            pstmt.setString(2, String.valueOf(account.getCardNumber()));
            // update
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;

    }

    public static Account getAccount(long cardNumber, String pin) {
        String sql = "SELECT * FROM card WHERE number = ? AND pin = ?;";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, String.valueOf(cardNumber));

            pstmt.setString(2, pin);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            Account account = null;

            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("number");
                String cardPin = rs.getString("pin");
                int balance = rs.getInt("balance");
                account = new Account(Long.parseLong(number), pin, balance);


            }

            rs.close();
            return account;
            // update
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static Account getAccount(String cardNumber) {
        String sql = "SELECT * FROM card WHERE number = ? ;";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, cardNumber);


            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            Account account = null;

            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("number");
                String cardPin = rs.getString("pin");
                int balance = rs.getInt("balance");
                account = new Account(Long.parseLong(number), cardPin, balance);


            }

            rs.close();
            return account;
            // update
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }






}


