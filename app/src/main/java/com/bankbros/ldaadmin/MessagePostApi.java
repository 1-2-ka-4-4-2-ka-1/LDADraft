package com.bankbros.ldaadmin;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MessagePostApi {

    String baseUrl = "https://fcm.googleapis.com/fcm/";
    Retrofit retrofit = new Retrofit.Builder()
           .baseUrl(baseUrl)
           .addConverterFactory(GsonConverterFactory.create())
           .build();

    @Headers({
            "Content-Type: application/json",
            "Authorization:key=AAAAm9YL944:APA91bF8RSHroL__LHEoGFMYwWlv-1MrpLRc_fr_q92-7pOmmWGpVdrESVmF8H-N80SMJHjfr11PEW-imST5DUdaXwz9wunFM85kqTGzJQ5qWZLXFKjSJG2lbtvyCI5ihbvWaD0PPZGY"
    })
    @POST("send")
    Call<String> postMessage(@Body MessageApi data);

}
