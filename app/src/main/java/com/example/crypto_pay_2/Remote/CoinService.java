package com.example.crypto_pay_2.Remote;

import com.example.crypto_pay_2.Model.Price;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinService {

    @GET("data/price")
    Call<Price> calculate(@Query("fsym") String from, @Query("tsyms") String to);

}
