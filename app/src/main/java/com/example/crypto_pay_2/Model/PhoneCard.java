package com.example.crypto_pay_2.Model;

public class PhoneCard {
    private String typeCard;
    private String seriNumber;
    private String codeNumber;
    private String dateTime;
    private String singlePrice;

    public PhoneCard(String typeCard, String seriNumber, String codeNumber, String dateTime, String singlePrice) {
        this.typeCard = typeCard;
        this.seriNumber = seriNumber;
        this.codeNumber = codeNumber;
        this.dateTime = dateTime;
        this.singlePrice = singlePrice;
    }

    public String getTypeCard() {
        return typeCard;
    }

    public void setTypeCard(String typeCard) {
        this.typeCard = typeCard;
    }

    public String getSeriNumber() {
        return seriNumber;
    }

    public void setSeriNumber(String seriNumber) {
        this.seriNumber = seriNumber;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(String singlePrice) {
        this.singlePrice = singlePrice;
    }
}
