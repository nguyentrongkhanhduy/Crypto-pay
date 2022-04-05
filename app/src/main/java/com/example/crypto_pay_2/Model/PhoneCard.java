package com.example.crypto_pay_2.Model;

import java.util.Comparator;

public class PhoneCard {
    private String id;
    private String typeCard;
    private String seriNumber;
    private String codeNumber;
    private String dateTime;
    private String singlePrice;
    private String isUsed;

    public PhoneCard(String id, String typeCard,  String codeNumber, String seriNumber, String dateTime, String singlePrice, String isUsed) {
        this.id = id;
        this.typeCard = typeCard;
        this.seriNumber = seriNumber;
        this.codeNumber = codeNumber;
        this.dateTime = dateTime;
        this.singlePrice = singlePrice;
        this.isUsed = isUsed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public static Comparator<PhoneCard> sortDate = new Comparator<PhoneCard>() {
        @Override
        public int compare(PhoneCard t1, PhoneCard t2) {
            String dt1 = t1.getDateTime();
            String date1 = dt1.substring(11,13);
            String month1 = dt1.substring(14,16);
            String year1 = dt1.substring(17,21);
            String time1 = dt1.substring(0,8);
            String dt2 = t2.getDateTime();
            String date2 = dt2.substring(11,13);
            String month2 = dt2.substring(14,16);
            String year2 = dt2.substring(17,21);
            String time2 = dt2.substring(0,8);
            if(year2.compareTo(year1) != 0){
                return year2.compareTo(year1);
            }
            else if(month2.compareTo(month1) != 0){
                return month2.compareTo(month1);
            }
            else if(date2.compareTo(date1) != 0){
                return date2.compareTo(date1);
            }
            else return time2.compareTo(time1);
        }
    };
}
