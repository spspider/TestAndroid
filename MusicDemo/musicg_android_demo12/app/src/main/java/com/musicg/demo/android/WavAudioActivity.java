package com.musicg.demo.android;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.data;
import static com.musicg.demo.android.MainActivity.FILE_TO_COMPARE_NAME;
import static com.musicg.demo.android.MainActivity.LOG_TAG;
import static com.musicg.demo.android.R.id.progressBar3;
import static com.musicg.demo.android.RecorderThread.frameByteSize;

public class WavAudioActivity{

    public static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    public static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    public static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    public static String AUDIO_RECORDER_TEMP_FILE_TEMP = null;
    public static final String AUDIO_RECORDER_NAME = "recorded_file";
    public static String AUDIO_RECORDER_NAME_THREAD=null;
    public static final int RECORDER_BPP = 16;

    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private String outerFilename;
    volatile boolean running = true;
    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecordingWav = false;
    private RecorderThread recoderThread;
    private Boolean isAudioOpend;
    FileOutputStream os = null;
    public WavAudioActivity(){
    bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);

}



    public WavAudioActivity(RecorderThread recoderThread) {
        bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
        recorder = recoderThread.getAudioRecord();
        recorder.startRecording();

        isAudioOpend = true;

        recordingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                writeAudioDataToFile_back();
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
    }



    public WavAudioActivity(String filename){
        this.outerFilename = filename;
    }



    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
            return null;
        }
        if (AUDIO_RECORDER_NAME_THREAD==null){
            AUDIO_RECORDER_NAME_THREAD=AUDIO_RECORDER_NAME+String.valueOf(System.currentTimeMillis());
        }
        String Path = (file.getAbsolutePath() + "/" + AUDIO_RECORDER_NAME_THREAD + AUDIO_RECORDER_FILE_EXT_WAV);
        if (!TextUtils.isEmpty(outerFilename)){
            Path =(file.getAbsolutePath() + "/" + outerFilename + AUDIO_RECORDER_FILE_EXT_WAV);
        }
        return Path;
    }
    public boolean isRecording(){
        if (recordingThread!=null) {
            return isRecordingWav;
        }
        else{
            return false;
        }
    }
    private String getComparisonFile() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
            return null;
        }
        String Path = (file.getAbsolutePath() + "/" + FILE_TO_COMPARE_NAME + AUDIO_RECORDER_FILE_EXT_WAV);
        return Path;
    }

    private String getTempFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
            return null;
        }
        if (AUDIO_RECORDER_TEMP_FILE_TEMP==null){
            AUDIO_RECORDER_TEMP_FILE_TEMP=AUDIO_RECORDER_TEMP_FILE+String.valueOf(System.currentTimeMillis());
            //Log.d(LOG_TAG,"TempFile:"+AUDIO_RECORDER_TEMP_FILE_TEMP);
        }

        File tempFile = new File(filepath, AUDIO_RECORDER_TEMP_FILE_TEMP);

        if (tempFile.exists())
            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE_TEMP);
    }
    public void startRecording() {
        bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, bufferSize);
        recorder.startRecording();

        isAudioOpend = true;
        if (outerFilename!=null){isRecordingWav=true;}
        startThread();
    }

    public void startThread(){
        makeNewFile();
        recordingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                writeAudioDataToFile();
                if (!running) return;
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
    }
    public void Recorde(boolean start) {
        if (start) {
            isRecordingWav = start;

        }
        else{
            stopRecording();
        }
    }
    public void run() {
        //startRecording(recoderThread);
    }

    private void writeAudioDataToFile_back() {
        byte data[] = new byte[bufferSize];

        int read = 0;

        if ((null != os)&&(recorder!=null)) {
            while (isRecordingWav) {

                read = recorder.read(data, 0, bufferSize);

                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void makeNewFile(){

        String filename = getTempFilename();
        //FileOutputStream os = null;
        if (os!=null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void writeAudioDataToFile() {
        byte data[] = new byte[bufferSize];

        if ((null != os)&&(recorder!=null)) {
            int read = 0;
            while (isAudioOpend) {
               int totalAbsValue = 0;
                short sample = 0;
                float averageAbsValue = 0.0f;

                read = recorder.read(data, 0, bufferSize);

                for (int i = 0; i < bufferSize; i += 2) {
                    sample = (short) ((data[i]) | data[i + 1] << 8);
                    totalAbsValue += Math.abs(sample);
                }
                averageAbsValue = totalAbsValue / frameByteSize / 2;
                final int finalAverageAbsValue = Math.round(averageAbsValue);
                ProgressBar progressBar4 = (ProgressBar) MainActivity.mainApp.findViewById(R.id.progressBar4);
                if (progressBar4 != null) {
                    progressBar4.setProgress(finalAverageAbsValue);
                }
                if ((averageAbsValue > 40)&&(!isRecordingWav)) {isRecordingWav=true;tmierTask();}

                if (isRecordingWav) {

                    //read = recorder.read(data, 0, bufferSize);

                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        try {
                            os.write(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    private void tmierTask() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                    if (isRecording()) {
                        //isRecordingWav=false;
                        stopRecording();
                        Log.d(LOG_TAG, "Stop2sec");

                    }
                }



            ;

        }, 2000L);
    }
    public void StopThread(){
        stopRecording();
        if (recorder!=null) {
            recorder.stop();
            recorder.release();
        }
        recordingThread.interrupt();
        running=false;
        isAudioOpend=false;
    }
    public void stopRecording() {

        if (isRecordingWav) {
            isRecordingWav = false;
            //recorder.stop();
            //recorder.release();
            Log.d(LOG_TAG, "RecoderStopped");
            //recorder = null;
            //recordingThread.interrupt();
            running=false;
            //startThread();
            //writeAudioDataToFile();
            if (outerFilename!=null){
                recorder.stop();
                running=false;
                recorder.release();
                isAudioOpend=false;
            }
        }


        if ((getTempFilename()!=null)&&(getFilename()!=null)) {

            copyWaveFile(getTempFilename(), getFilename());

            Log.d(LOG_TAG, "Filename: " + getFilename());
            deleteTempFile();

            compareWithStoredAudio(getFilename());
        }
        if (outerFilename==null){
            makeNewFile();
            running=true;

        }





    }


    private void compareWithStoredAudio(String filename) {
        String songA = getComparisonFile();
        // create wave objects
        if (songA==null){
            Log.d(LOG_TAG,"no File");
            return;
        }

        Wave waveA = new Wave(songA);
        //Log.d(LOG_TAG,"Lenght:"+waveA.length()+" size:"+waveA.size()+" timestamp:"+waveA.timestamp()+" bytes:"+ Arrays.toString(waveA.getBytes()));
        String recordedClip = filename;
        Wave waveRec = new Wave(recordedClip);
        if ((waveRec.getWaveHeader()!=null)&&(waveA.getWaveHeader()!=null)) {
            //FingerprintSimilarityComputer fpc = new FingerprintSimilarityComputer(waveRec.getFingerprint(), waveA.getFingerprint());
            //FingerprintSimilarity similarity = fpc.getFingerprintsSimilarity();



            FingerprintSimilarity similarity;

            // song A:
            similarity = waveA.getFingerprintSimilarity(waveRec);

            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            intent.putExtra(MainActivity.SIMILIARITY, similarity.getScore());
            intent.putExtra(MainActivity.POSITION, similarity.getsetMostSimilarTimePosition());
            intent.putExtra(MainActivity.SIMILIAR, similarity.getSimilarity());
            MainActivity.mainApp.sendBroadcast(intent);

            Log.d(LOG_TAG,
                    "similarity score: " + similarity.getScore() + " :pos: " + similarity.getsetMostSimilarTimePosition()+"similiar:"+similarity.getSimilarity());


        //Log.d(LOG_TAG,
       //         "similarity score: " + similarity.getScore() + " :pos: " + similarity.getsetMostSimilarTimePosition());
        }
    }

    private void deleteTempFile() {
        File file = new File(getTempFilename());

        file.delete();
    }

    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 2;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels / 8;

        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(inFilename);
            totalAudioLen = in.getChannel().size();
            if (totalAudioLen>4400) {
                out = new FileOutputStream(outFilename);

                totalDataLen = totalAudioLen + 36;

                Log.d(LOG_TAG, "File size: " + totalDataLen);

                WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);

                while (in.read(data) != -1) {
                    out.write(data);
                }

                in.close();
                out.close();
            }

            //SequenceInputStream sis = new SequenceInputStream(out,fis2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate,
                                     int channels, long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = RECORDER_BPP; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
        Wave w1 = new Wave();

    }


}
