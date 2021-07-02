package com.example.ttester_paukov;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

/**
 * Created by sp_1 on 06.04.2017.
 */
public class Utils {
    public static void WriteToDeveloper(Activity activity) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "spspider@inbox.ru"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "problem");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Quiz");
//emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

        activity.startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
    }
}
