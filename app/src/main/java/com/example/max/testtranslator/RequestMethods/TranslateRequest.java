package com.example.max.testtranslator.RequestMethods;

import com.example.max.testtranslator.REST.YandexTranslateAPI;
import com.example.max.testtranslator.ResponseModels.TranslateData;
import com.example.max.testtranslator.Utils.Messages;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by Max on 10.12.2016.
 */
import static com.example.max.testtranslator.REST.ApiMethods.createYandexTranlateAPI;
import static com.example.max.testtranslator.REST.ApiMethods.makeRequest;
public class TranslateRequest {

    public static void requestTranslate(HashMap<String, String> query) {
        YandexTranslateAPI service = createYandexTranlateAPI();
        Call call = service.translate(query);
        makeRequest(call, Messages.RESPONSE_SERVER_TRANSLATE);
    }

}
