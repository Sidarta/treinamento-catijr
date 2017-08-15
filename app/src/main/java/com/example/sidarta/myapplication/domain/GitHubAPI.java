package com.example.sidarta.myapplication.domain;

import com.example.sidarta.myapplication.domain.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by sidarta on 03/08/17.
 */

public interface GitHubAPI {

    Retrofit RETROFIT = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.github.com/")
            .build();

    @GET("user")
    Call<User> login(@Header("Authorization") String credential);

    @GET("/user/followers")
    Call<List<User>> listFollowers(@Header("Authorization") String credential);
}



