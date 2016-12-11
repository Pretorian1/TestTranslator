package com.example.max.testtranslator.OfflineTranslate;

import android.text.TextUtils;
import android.util.Log;

import com.example.max.testtranslator.Activity.MainActivity;
import com.example.max.testtranslator.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Max on 11.12.2016.
 */

public class TranslateFromLibrary {

    public static String getTranslate(XmlPullParser xpp, String word){
        final String LOG_TAG = "myLogs";
        boolean flag = false;
        boolean flagLast = false;
        boolean detect = false;
        String tempWord ="";

        try {
            breakpoint:while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_TAG:
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {

                            if(xpp.getAttributeValue(i).equals("L1"))//original
                            { flag = true;}
                            if(xpp.getAttributeValue(i).equals("L2")&&detect)//translate
                            { flagLast = true;}
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if(flag) {
                            String [] bufferArray = getWordFromLibrary(xpp.getText());
                            for(String s : bufferArray){
                            if(s.equals(word)){
                              //  tempWord = s;
                            Log.d(LOG_TAG, "text original = " + s);
                                detect = true;
                            }
                            }
                            flag = false;
                            continue breakpoint;
                       }
                        else if(flagLast){
                            String [] bufferArray = xpp.getText().split(";");
                            detect = false;
                            flagLast = false;
                            tempWord = bufferArray[ (int) (bufferArray.length *Math.random())];
                            Log.d(LOG_TAG, "text translated = " + tempWord);
                            break breakpoint;

                        }
                        break;

                    default:
                        break;
                }
                xpp.next();
            }
            Log.d(LOG_TAG, "END_DOCUMENT");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

   return tempWord;


    }
    public static String [] getWordFromLibrary(String wordSequence){
        wordSequence = removeSpace(wordSequence);
        String [] words =wordSequence.split(";");
        if (words.length != 0)
            return words;
        else
       return null;
    }

    public static String removeSpace(String s) {
        String withoutspaces = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ')
                withoutspaces += s.charAt(i);
        }
        return withoutspaces;
    }


}



