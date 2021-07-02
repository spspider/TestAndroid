package es.bcn.keepitgoing.activities;

import android.os.Environment;
import android.util.Log;

import com.musicg.api.DetectionApi;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.fingerprint.FingerprintSimilarityComputer;
import com.musicg.wave.Wave;

/**
 * Created by sp_1 on 12.04.2017.
 */

public class WavApi extends DetectionApi {
    Wave waveToMatch;
    public String LOG_TAG="my_logs";

    public WavApi(Wave wave) {
        super(wave.getWaveHeader());
        this.waveToMatch = wave;
    }
    public void Fingepint(Wave w1){
        //somewhere in your code add
        String file1 = Environment.getExternalStorageDirectory().getAbsolutePath();
        file1 += "/test.wav";

        String file2 = Environment.getExternalStorageDirectory().getAbsolutePath();
        file2 += "/test.wav";

        //w1 = new Wave(file1);
        Wave w2 = new Wave(file2);

        //FingerprintSimilarityComputer fpc = new FingerprintSimilarityComputer(w1.getFingerprint(), w2.getFingerprint());
        //FingerprintSimilarity similarity = fpc.getFingerprintsSimilarity();

        //--- TODO: find optimal condition to decide whether {audioBytes} matches {waveToMatch} or not
        //float score = similarity.getScore() * 100f;
        //int sim   = similarity.getMostSimilarFramePosition();


        FingerprintSimilarity fps = w1.getFingerprintSimilarity(w2);
        float score = fps.getScore();
        float sim = fps.getSimilarity();



        Log.d(LOG_TAG,"score"+score+"");
        Log.d(LOG_TAG,"similarities"+sim+"");



    }
    public boolean matches(byte[] audioBytes) {
        Wave wave = new Wave(waveToMatch.getWaveHeader(), audioBytes);

        FingerprintSimilarityComputer fpc = new FingerprintSimilarityComputer(waveToMatch.getFingerprint(), wave.getFingerprint());
        FingerprintSimilarity similarity = fpc.getFingerprintsSimilarity();

        //--- TODO: find optimal condition to decide whether {audioBytes} matches {waveToMatch} or not
        float sim = similarity.getScore() * 100f;
        int pos   = similarity.getMostSimilarFramePosition();

        Log.d(LOG_TAG,"score"+sim+"");
        Log.d(LOG_TAG,"similarities"+pos+"");

            if (pos >= 0 && sim > 0)
            return true;
        //----

        return false;
    }

}
