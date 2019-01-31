package com.example.mateusz.splitwise.model;

public class Bill {
    private String email;
    private Double amount;

    public Bill() {
    }

    public Bill(String email, Double amount) {
        this.email = email;
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
