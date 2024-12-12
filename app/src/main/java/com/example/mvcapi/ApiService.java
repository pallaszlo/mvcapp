package com.example.mvcapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register")
    Call<Void> register(@Body RegisterRequest request);

    @POST("login")
    Call<Void> login(@Body LoginRequest request);

    @GET("users/")
    Call<UserResponse> getUsers();
}
