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
            "Authorization:key=AAAAMsag9_g:APA91bG7cTIlOfsNKDwgmv0Z_cghq5Z1t-N5Ti_ECY9Y67SMD-0ARG2Z0dGakV6WvUV33i3TkwfBd5UoLQlEP_H6ux5EkXmSv9wNB0Fc4IAVurll-oIbKwOAwxKP0zuy4D9c_9lOOw61"
    })
    @POST("send")
    Call<String> postMessage(@Body MessageApi data);

}
