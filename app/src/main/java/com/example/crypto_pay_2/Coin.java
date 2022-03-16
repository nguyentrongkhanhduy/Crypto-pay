package com.example.crypto_pay_2;

public class Coin {
    int bitcoin;
    int ethereum;
    int lvcoin;

    public Coin(int Bitcoin, int Ethereum, int lvcoin) {
        this.bitcoin = Bitcoin;
        this.ethereum = Ethereum;
        this.lvcoin = lvcoin;
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
}
