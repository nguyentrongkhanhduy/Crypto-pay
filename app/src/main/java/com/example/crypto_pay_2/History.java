package com.example.crypto_pay_2;

public class History {
    private String name;
    private String amount;
    private String date;
    private String time;
    private String from;
    private String to;
    private String id;
    private String message;

    public History(){}

    public History(String name, String amount, String date, String time, String from, String to, String id, String message) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.from = from;
        this.to = to;
        this.id = id;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}