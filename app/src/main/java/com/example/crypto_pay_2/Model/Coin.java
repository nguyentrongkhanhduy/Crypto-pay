package com.example.crypto_pay_2.Model;

public class Coin {
    int bitcoin;
    int ethereum;
    int lvcoin;

    int usd;

    public Coin(int Bitcoin, int Ethereum, int lvcoin, int usd) {
        this.bitcoin = Bitcoin;
        this.ethereum = Ethereum;
        this.lvcoin = lvcoin;
        this.usd = usd;
    }

    public int getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(int bitcoin) {
        this.bitcoin = bitcoin;
    }

    public int getEthereum() {
        return ethereum;
    }

    public void setEthereum(int ethereum) {
        this.ethereum = ethereum;
    }

    public int getLvcoin() {
        return lvcoin;
    }

    public void setLvcoin(int lvcoin) {
        this.lvcoin = lvcoin;
    }

    public int getUsd() {
        return usd;
    }

    public void setUsd(int usd) {
        this.usd = usd;
    }
}
