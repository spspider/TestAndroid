package com.jjoe64.motiondetection.TTS;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

import static android.R.attr.data;
import static android.R.attr.key;

public class TTSService extends Service implements TextToSpeech.OnInitListener {

    private Locale language;
    private String message = null;
    private TextToSpeech ttsObj=null;





    @Override
    public void onInit(int status){
        // Check if Text to speech engine has been successfully instantiated
        if(status ==TextToSpeech.SUCCESS){
            Log.d("Text2Speech","initialisation Succesful");
        }
        else{
            Log.d("TTS","Initialisation failed");
        }

    }
    public TTSService() {
        //super("TTSService");
    }
    @Override
    public void onCreate(){
        super.onCreate();
        ttsObj=new TextToSpeech(this,this);
        Log.d("onCreate","Inside it");
    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        try{
            //Intent myIntent = getIntent();
            if (intent !=null && intent.getExtras()!=null){
                message = intent.getExtras().getString("message");
             }
            //Bundle extras=intent.getExtras();
            //String locale = extras.getString("locale");

            Locale locale = new Locale("ru");
            //Locale lang = (Locale) extras.get("locale");
            //Get the language in which to speak
            //language=(Locale) getLanguage();//extras.get("Language"); // Change here 28.10.14

            //Sets the language
            //int result=ttsObj.setLanguage(locale);
            int result=ttsObj.setLanguage(locale);
            //Checks if language data is missing or  language is not supported
            if(result== TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.d("TTS","Language not supported");
            }
            else{
                Log.d("TTS","Language supported");
            }

            //if(ttsObj!=null && extras!=null){ change here 28.10.14
            if(ttsObj!=null){
                //String from=(String)extras.get("From");// Change here
                if(message!=null)
                {
                    ttsObj.speak(message, TextToSpeech.QUEUE_ADD, null); // Changed from to message
                    Log.d("onStart","Inside it");
                }
                else
                {
                    Log.d("onStart", "Message is null");
                }
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return START_STICKY;


    }
    //Stop and Shutdown Text to speech engine
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(ttsObj!=null){
            ttsObj.stop();
            ttsObj.shutdown();
        }
    }
    public IBinder onBind(Intent intent) {

        return new Binder();
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);

    }

    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }




    // May get removed as present these has to be initialized in the constructor.
    /**
     * @return the language
     */
    public final Locale getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public final void setLanguage(Locale language) {
        this.language = language;
    }

    /**
     * @return the message
     */
    public final String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public final void setMessage(String message) {
        this.message = message;
    }

    public void Say(String s, Locale loc) {
        ttsObj.speak(message, TextToSpeech.QUEUE_ADD, null); // Changed from to message
        Log.d("onStart","Inside it");
    }


}