package com.example.max.testtranslator.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.max.testtranslator.R;
import com.example.max.testtranslator.REST.YandexTranslateAPI;
import com.example.max.testtranslator.ResponseModels.TranslateData;
import com.example.max.testtranslator.Utils.MessageEvent;
import com.example.max.testtranslator.Utils.Messages;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.max.testtranslator.REST.ApiMethods.createYandexTranlateAPI;

public class MainActivity extends AppCompatActivity {

    private EditText mInputText;
    private EditText mOutputText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInputText = (EditText) findViewById(R.id.input_text);
        mOutputText = (EditText) findViewById(R.id.output_text);
        mButton =(Button)findViewById(R.id.button);
        EventBus.getDefault().register(this);
        mButton.setOnClickListener(new View.OnClickListener() {//
            @Override
            public void onClick(View v) {
              translateText();
            }
        });
    }

    public void translateText(){
        if(!mInputText.getText().toString().matches("")){
          HashMap mapJson = new HashMap<>();
            mapJson.put("key", YandexTranslateAPI.KEY);
            mapJson.put("text", mInputText.getText().toString());
            mapJson.put("lang", "en-uk");
            YandexTranslateAPI service = createYandexTranlateAPI();
            Call call = service.translate(mapJson);
            call.enqueue(new Callback<TranslateData>() {
                @Override
                public void onResponse(Call<TranslateData> call, Response<TranslateData> response) {
                    if (response.isSuccessful()) {
                        //getting response from server
                        TranslateData serverResponse = response.body();
                        EventBus.getDefault().post(new MessageEvent(Messages.RESPONSE_SERVER_TRANSLATE, serverResponse));
                    } else {
                        EventBus.getDefault().post(new MessageEvent(Messages.RESPONSE_SERVER_ERROR, null));
                    }
                }

                @Override
                public void onFailure(Call<TranslateData> call, Throwable t) {

                }
            });


        }

    }

    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        switch (event.message) {
            case RESPONSE_SERVER_ERROR:
                Toast.makeText(this, event.message.name(), Toast.LENGTH_SHORT).show();
                break;
            case RESPONSE_SERVER_TRANSLATE:
                mOutputText.setText(((TranslateData)event.link).getText().get(0));
                break;
        }
    }

}
