package com.example.sidarta.myapplication.domain;

import com.example.sidarta.myapplication.domain.model.AccessToken;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by sidarta on 14/08/2017.
 */

public interface GitHubOAuthAPI {

    String BASE_URL = "https://github.com/login/oauth/";

    Retrofit RETROFIT = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build();


    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("access_token")
    Call<AccessToken> accessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code);
}

