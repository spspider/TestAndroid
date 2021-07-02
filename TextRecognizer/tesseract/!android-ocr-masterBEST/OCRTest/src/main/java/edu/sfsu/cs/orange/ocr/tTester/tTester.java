package edu.sfsu.cs.orange.ocr.tTester;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import edu.sfsu.cs.orange.ocr.CaptureActivity;
import edu.sfsu.cs.orange.ocr.R;
import edu.sfsu.cs.orange.ocr.language.TranslateAsyncTask;

/**
 * Created by sp_1 on 18.04.2017.
 */

public class tTester extends AsyncTask<String,String,Boolean> {
    private static final String TAG = TranslateAsyncTask.class.getSimpleName();

    private CaptureActivity activity;
    private TextView textView;
    private View progressView;
    private TextView targetLanguageTextView;
    private String sourceLanguageCode;
    private String targetLanguageCode;
    private String ocrResultText;
    private String translatedText = "";

    public tTester(CaptureActivity activity, String ocrResultText) {
        this.activity = activity;
        this.ocrResultText= ocrResultText;
        textView = (TextView) activity.findViewById(R.id.translation_text_view);
        progressView = (View) activity.findViewById(R.id.indeterminate_progress_indicator_view);
        targetLanguageTextView = (TextView) activity.findViewById(R.id.translation_language_text_view);
        Log.d("my_logs","text:"+ocrResultText);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return true;
    }
    @Override
    protected synchronized void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result) {
            //Log.i(TAG, "SUCCESS");
            if (targetLanguageTextView != null) {
                targetLanguageTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
            }
            textView.setText(translatedText);
            textView.setVisibility(View.VISIBLE);
            textView.setTextColor(activity.getResources().getColor(R.color.translation_text));

            // Crudely scale betweeen 22 and 32 -- bigger font for shorter text
            int scaledSize = Math.max(22, 32 - translatedText.length() / 4);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

        } else {
            Log.e(TAG, "FAILURE");
            targetLanguageTextView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
            targetLanguageTextView.setText("Unavailable");

        }

        // Turn off the indeterminate progress indicator
        if (progressView != null) {
            progressView.setVisibility(View.GONE);
        }
    }
}
