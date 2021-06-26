package com.example.blood;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetroFitApi {
    @FormUrlEncoded
    @POST("login.php")
    Call<User> login(@Field("name") String username, @Field("password") String password);
}
