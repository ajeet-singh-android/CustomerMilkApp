package com.app.veka.Network;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {


    public static ApiServices getClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://milk.imminenttechnology.com/Api/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        ApiServices apiServices = retrofit.create(ApiServices.class);
        return apiServices;
    }

}
