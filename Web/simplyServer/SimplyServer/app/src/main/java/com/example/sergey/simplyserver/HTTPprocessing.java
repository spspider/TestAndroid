package com.example.sergey.simplyserver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Properties;

/**
 * Created by sergey on 12.11.2018.
 */

public class HTTPprocessing {

    public static final String NEW_CAT_DETECTED = "android.intent.action.ACTION";
Context context=null;
    public HTTPprocessing(Context applicationContext) {
        this.context=applicationContext;
    }

    public String HTTPprocessing(Properties parms, String uri, String method, Properties header, Properties files, String IPadress, int PORT) {


        Intent intent = new Intent(NEW_CAT_DETECTED);
// Или так
// Intent intent = new Intent();
// intent.setAction(NEW_CAT_DETECTED);

        intent.putExtra("username", parms.getProperty("username"));
        if (context!=null) {
            context.sendBroadcast(intent);
            Log.d("LOG","sended");
        }


        String html = "<html>\n" +
                "<body>\n" +
                "<form action=\"http://" + IPadress + ":" + PORT + "\" enctype=\"multipart/form-data\" method=\"POST\">\n" +
                "    <input type=\"text\" id=\"input\" name=\"username\" value="+parms.getProperty("username") +
                " />\n" +
                "    <input type=\"submit\" />\n" +
                "</form>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        return html;
    }

}
