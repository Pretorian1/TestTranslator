package com.example.max.testtranslator.REST;




import com.example.max.testtranslator.Utils.MessageEvent;
import com.example.max.testtranslator.Utils.Messages;

import org.greenrobot.eventbus.EventBus;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiMethods {

    public static YandexTranslateAPI createYandexTranlateAPI() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YandexTranslateAPI.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(YandexTranslateAPI.class);
    }

    public static void makeRequest(Call call,  final Messages message) {// дуже цікаво чого final?!
        call.enqueue(new Callback<Object>() {
                         @Override
                         public void onResponse(Call<Object> call, Response<Object> response) {
                             if (response.isSuccessful()) {
                                 //getting response from server
                                 Object serverResponse = response.body();
                                 EventBus.getDefault().post(new MessageEvent(message, serverResponse));
                             } else {
                                 EventBus.getDefault().post(new MessageEvent(Messages.RESPONSE_SERVER_ERROR, null));
                             }
                         }

                         @Override
                         public void onFailure(Call<Object> call, Throwable t) {

                             EventBus.getDefault().post(new MessageEvent(Messages.RESPONSE_SERVER_ERROR, null));
                         }
                     }
        );
    }


}
