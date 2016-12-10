package com.example.max.testtranslator.REST;



import android.util.Log;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiMethods {

    public static YandexTranslateAPI createYandexTranlateAPI() {


      /*  gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(URL)
                .build();

        service = retrofit.create(YandexTranslateService.class);*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YandexTranslateAPI.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(YandexTranslateAPI.class);
    }

    public static void makeRequest(Call call) {
        call.enqueue(new Callback<Object>() {
                         @Override
                         public void onResponse(Call<Object> call, Response<Object> response) {
                             if (response.isSuccessful()) {
                                 //getting response from server
                                 Object serverResponse = response.body();
                               //  EventBus.getDefault().post(new MessageEvent(message, serverResponse));
                             } else {
                                 ServerErrorHandler serverError = new ServerErrorHandler(response.code(), response.message());
                                 //EventBus.getDefault().post(new MessageEvent(Messages.RESPONSE_SERVER_ERROR, serverError));
                             }
                         }

                         @Override
                         public void onFailure(Call<Object> call, Throwable t) {
                             Log.d("onFailure",t.toString());
                             ServerErrorHandler serverError = new ServerErrorHandler(t);
                           //  EventBus.getDefault().post(new MessageEvent(Messages.RESPONSE_SERVER_ERROR, serverError));
                         }
                     }
        );
    }

}
