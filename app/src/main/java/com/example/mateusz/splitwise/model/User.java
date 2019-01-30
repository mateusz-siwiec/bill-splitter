package com.example.mateusz.splitwise.model;

import java.util.List;

public class User {
    private String username;
    private String email;
    private double balance;
    private List<Bill> bills;

    public User() {
    }

    public User(String username, String email, double balance, List<Bill> bills) {
        this.username = username;
        this.email = email;
        this.balance = balance;
        this.bills = bills;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }
}
