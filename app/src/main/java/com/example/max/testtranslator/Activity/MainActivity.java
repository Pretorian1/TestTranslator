package com.example.max.testtranslator.Activity;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.max.testtranslator.OfflineTranslate.TranslateFromLibrary;
import com.example.max.testtranslator.R;
import com.example.max.testtranslator.REST.YandexTranslateAPI;
import com.example.max.testtranslator.RequestMethods.TranslateRequest;
import com.example.max.testtranslator.ResponseModels.TranslateData;
import com.example.max.testtranslator.Utils.MessageEvent;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private EditText mInputText;
    private EditText mOutputText;
    private Button mButton;
    private Spinner mSpinnerFrom;
    private Spinner mSpinnerTo;
    private Button mButtonParser;

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
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              translateText();
            }
        });
        mButtonParser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                Runnable r = new Runnable(){
                    public void run() {
                        String from = mSpinnerFrom.getSelectedItem().toString();
                        String to = mSpinnerTo.getSelectedItem().toString();
                        String tempString = mInputText.getText().toString();
                        if ( !tempString.equals("")) {
                            String [] arrayOfWords=tempString.toLowerCase().split(" ");
                            if (from.equals("ru") && to.equals("en")) {
                                mOutputText.setText("");
                                for (int i =0 ; i<arrayOfWords.length;i++){
                                mOutputText.setText(mOutputText.getText().toString()+" "+TranslateFromLibrary.getTranslate(prepareXppRE(), arrayOfWords[i]));
                                }
                            } else if (from.equals("en") && to.equals("ru")) {
                                mOutputText.setText("");
                                for (int i =0 ; i<arrayOfWords.length;i++){
                                mOutputText.setText(mOutputText.getText().toString()+" "+TranslateFromLibrary.getTranslate(prepareXppER(), arrayOfWords[i]));
                                }
                            } else {
                                mOutputText.setText(getResources().getString(R.string.not_ready));
                            }
                        }
                        else
                            mOutputText.setText(getResources().getString(R.string.put_phrase));
                    }
                };
                handler.post(r);
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
                break;
        }
    }
    public  XmlPullParser prepareXppER() {
        return getResources().getXml(R.xml.english_russian);
    }
    public  XmlPullParser prepareXppRE() {
        return getResources().getXml(R.xml.russian_english);
    }

}


