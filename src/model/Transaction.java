package model;

import java.time.LocalDate;

public class Transaction {

    private int id;
    private LocalDate date;
    private String type;
    private String category;
    private double amount;
    private String note;

    public Transaction(int id, LocalDate date, String type, String category, double amount, String note) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }
}