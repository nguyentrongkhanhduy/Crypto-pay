package com.example.crypto_pay_2.Model;

import java.util.Comparator;

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

    public static Comparator<History> sortDate = new Comparator<History>() {
        @Override
        public int compare(History history1, History history2) {
            String dt1 = history1.getDate();
            String date1 = dt1.substring(0,2);
            String month1 = dt1.substring(3,5);
            String year1 = dt1.substring(6);
            String dt2 = history2.getDate();
            String date2 = dt2.substring(0,2);
            String month2 = dt2.substring(3,5);
            String year2 = dt2.substring(6);

            if(year2.compareTo(year1) != 0){
                return year2.compareTo(year1);
            }
            else if(month2.compareTo(month1) != 0){
                return month2.compareTo(month1);
            }
            else if(date2.compareTo(date1) != 0){
                return date2.compareTo(date1);
            }
            else return history2.getTime().compareTo(history1.getTime());
        }
    };
}
