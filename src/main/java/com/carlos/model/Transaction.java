package com.carlos;

import java.time.LocalDate;

public class Transaction {

    private final String name;
    private final double amount;
    private final LocalDate date;
    private final String source;
    private final TransactionType type;
    private final String category;

    public Transaction(String name,
                       double amount,
                       LocalDate date,
                       String source,
                       TransactionType type,
                       String category) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.source = source;
        this.type = type;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

    public TransactionType getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return date + " | " + name + " | $" + amount + " | " + source;
    }
}