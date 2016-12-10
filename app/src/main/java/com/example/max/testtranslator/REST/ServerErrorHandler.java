package com.example.max.testtranslator.REST;

import android.util.Log;

/**
 * Class that keeps errors having place during server request
 */
public class ServerErrorHandler {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private final int UNAUTHORIZED_ERROR = 401;
    private String errorMessage;

    /**
     * Constructor - keeps server error responses: like 4xx, 5xx ...
     */
    public ServerErrorHandler(int responseCode, String responseMessage) {
        //todo: test this
        if(responseCode == UNAUTHORIZED_ERROR){
         //   EventBus.getDefault().post(new MessageEvent(Messages.GO_TO_LOGIN_ACTIVITY, null));
        }

        errorMessage = "" + responseCode + " " + responseMessage;
    }

    /**
     * Constructor - keeps other errors: like no internet connection etc
     */
    public ServerErrorHandler(Throwable t) {
        //todo: remake
//        errorMessage = "some error happend";
        errorMessage = t.getMessage() + " " + t.toString();
        Log.d(LOG_TAG, errorMessage);
    }

    /**
     * @return error string message
     */
    public String getErrorMessage(){
        return errorMessage;
    }
}
