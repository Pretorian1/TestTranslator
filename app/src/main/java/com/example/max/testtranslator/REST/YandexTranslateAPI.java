package com.example.max.testtranslator.REST;

/**
 * Created by Max on 10.12.2016.
 */

import com.example.max.testtranslator.ResponseModels.TranslateData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface YandexTranslateAPI {
    String MAIN_URL="https://translate.yandex.net";
    String KEY ="trnsl.1.1.20160324T093729Z.b14b7c54accb1f8e.7d811d65e5f9b19b5541568a35af1e6c8019b10e";

    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Call<TranslateData> translate(@FieldMap Map<String, String> map);
}
