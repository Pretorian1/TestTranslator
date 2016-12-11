package com.example.max.testtranslator.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.max.testtranslator.R;
import com.example.max.testtranslator.REST.ApiMethods;
import com.example.max.testtranslator.REST.YandexTranslateAPI;
import com.example.max.testtranslator.RequestMethods.TranslateRequest;
import com.example.max.testtranslator.ResponseModels.TranslateData;
import com.example.max.testtranslator.Utils.MessageEvent;
import com.example.max.testtranslator.Utils.Messages;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.max.testtranslator.REST.ApiMethods.createYandexTranlateAPI;
import static com.example.max.testtranslator.REST.ApiMethods.makeRequest;

public class MainActivity extends AppCompatActivity {

    private EditText mInputText;
    private EditText mOutputText;
    private Button mButton;
    private Spinner mSpinnerFrom;
    private Spinner mSpinnerTo;
    private Button mButtonParser;
    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInputText = (EditText) findViewById(R.id.input_text);
        mInputText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mOutputText = (EditText) findViewById(R.id.output_text);
        mOutputText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mButton =(Button)findViewById(R.id.button);
        mSpinnerFrom =(Spinner) findViewById(R.id.spinnerFrom);
        mSpinnerTo = (Spinner) findViewById(R.id.spinnerTo);
        mButtonParser = (Button) findViewById(R.id.buttonParser);
        EventBus.getDefault().register(this);
        mButton.setOnClickListener(new View.OnClickListener() {//
            @Override
            public void onClick(View v) {
              translateText();
            }
        });
        mButtonParser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmp = "";

                try {
                    XmlPullParser xpp = prepareXpp();

                    while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                        switch (xpp.getEventType()) {
                            // начало документа
                            case XmlPullParser.START_DOCUMENT:
                                Log.d(LOG_TAG, "START_DOCUMENT");
                                break;
                            // начало тэга
                            case XmlPullParser.START_TAG:
                                Log.d(LOG_TAG, "START_TAG: name = " + xpp.getName()
                                        + ", depth = " + xpp.getDepth() + ", attrCount = "
                                        + xpp.getAttributeCount());
                                tmp = "";
                                for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                    tmp = tmp + xpp.getAttributeName(i) + " = "
                                            + xpp.getAttributeValue(i) + ", ";
                                }
                                if (!TextUtils.isEmpty(tmp))
                                    Log.d(LOG_TAG, "Attributes: " + tmp);
                                break;
                            // конец тэга
                            case XmlPullParser.END_TAG:
                                Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
                                break;
                            // содержимое тэга
                            case XmlPullParser.TEXT:
                                Log.d(LOG_TAG, "text = " + xpp.getText());
                                break;

                            default:
                                break;
                        }
                        // следующий элемент
                        xpp.next();
                    }
                    Log.d(LOG_TAG, "END_DOCUMENT");

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            XmlPullParser prepareXpp() {
                return getResources().getXml(R.xml.english_russian);
            }
        });
    }

    public void translateText(){
        if(!mInputText.getText().toString().matches("")){
          HashMap mapJson = new HashMap<>();
            String from = mSpinnerFrom.getSelectedItem().toString();
            String to = mSpinnerTo.getSelectedItem().toString();
            mapJson.put("key", YandexTranslateAPI.KEY);
            mapJson.put("text", mInputText.getText().toString());
            mapJson.put("lang", from+"-"+to);
            TranslateRequest.requestTranslate(mapJson);
           /* YandexTranslateAPI service = createYandexTranlateAPI();
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
            });*/


        }

    }

    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        switch (event.message) {
            case RESPONSE_SERVER_ERROR:
                Toast.makeText(this, event.message.name(), Toast.LENGTH_SHORT).show();
                break;
            case RESPONSE_SERVER_TRANSLATE:
                mOutputText.setText("");
                ArrayList<String> translatedList = ((TranslateData)event.link).getText();
                for(String s:translatedList){
                    mOutputText.setText(mOutputText.getText().toString()+s);
                }
              //  mOutputText.setText(((TranslateData)event.link).getText().get(0));// може більше треба показувати!!!
                break;
        }
    }

}
