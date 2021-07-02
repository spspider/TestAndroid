/*
 * Copyright (C) 2012 Jacquet Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * musicg api in Google Code: http://code.google.com/p/musicg/
 * Android Application in Google Play: https://play.google.com/store/apps/details?id=com.whistleapp
 * 
 */

package com.musicg.demo.android;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;

import com.musicg.wave.Wave;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static com.musicg.demo.android.MainActivity.FILE_TO_COMPARE_NAME;
import static com.musicg.demo.android.WavAudioActivity.AUDIO_RECORDER_FILE_EXT_WAV;
import static com.musicg.demo.android.WavAudioActivity.AUDIO_RECORDER_FOLDER;

public class RecorderThread extends Thread {

	private static final String LOG_TAG = "my_logs";
	private AudioRecord audioRecord;
	private boolean isRecording;
	public static  int channelConfiguration = AudioFormat.CHANNEL_IN_STEREO;
	public static  int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	public static int sampleRate = 44100;
	public static int frameByteSize = 2048; // for 1024 fft size (16bit sample size)

	public static String AUDIO_RECORDER_NAME_THREAD=null;
	public static final int RECORDER_BPP = 16;

	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	byte[] buffer;
	private WavAudioActivity wavAudioActivity=null;
	Handler timerHandler =new Handler(Looper.getMainLooper());
	private long length;
	boolean canrecodenext=true;
	private volatile Thread _thread;
	RecorderThread recorderThread;
	public void StopWaveAudioActivity(){
		if (wavAudioActivity!=null){
		wavAudioActivity.stopRecording();
			wavAudioActivity=null;}
	}
	public RecorderThread(){

		int recBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfiguration, audioEncoding); // need to be larger than size of a frame
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfiguration, audioEncoding, recBufSize);
		buffer = new byte[frameByteSize];
		recorderThread=this;
		//wavAudioActivity = new WavAudioActivity(recorderThread);
	}
	
	public AudioRecord getAudioRecord(){
		return audioRecord;
	}
	
	public boolean isRecording(){
		return this.isAlive() && isRecording;
	}
	
	public void startRecording(){
		try{
			//getFrameBytes();
			_thread = new Thread(new Runnable() {
				public void run() {
					Thread thisThread = Thread.currentThread();
					while (_thread == thisThread) {
						getFrameBytes();
					}
				}

			}, "thread");

			_thread.start();

			audioRecord.startRecording();

			isRecording = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stopRecording(){
		wavAudioActivity.stopRecording();
		Log.d(LOG_TAG,"RecorderThreadStopped");
		try{
			if (null != audioRecord) {
				isRecording = false;
				Log.d(LOG_TAG,"AudioRecordState:"+audioRecord.getState());
				audioRecord.stop();
				audioRecord.release();
				audioRecord = null;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public byte[] getFrameBytes() {
		audioRecord.read(buffer, 0, frameByteSize);
		int totalAbsValue = 0;
		short sample = 0;
		float averageAbsValue = 0.0f;

		for (int i = 0; i < frameByteSize; i += 2) {
			sample = (short) ((buffer[i]) | buffer[i + 1] << 8);
			totalAbsValue += Math.abs(sample);
		}
		averageAbsValue = totalAbsValue / frameByteSize / 2;

		final int finalAverageAbsValue = Math.round(averageAbsValue);
		ProgressBar progressBar3 = (ProgressBar) MainActivity.mainApp.findViewById(R.id.progressBar3);
		if (progressBar3 != null) {
			progressBar3.setProgress(finalAverageAbsValue);
		}



		if ((averageAbsValue > 40)&&(wavAudioActivity==null)&&(canrecodenext)){

				wavAudioActivity = new WavAudioActivity(recorderThread);
			//wavAudioActivity.startRecording();
				//wavAudioActivity.start();

				Log.d(LOG_TAG, "StartRecoding");
				wavAudioActivity.Recorde(true);
				canrecodenext = false;

				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						if (wavAudioActivity.isRecording()) {
							wavAudioActivity.Recorde(false);
							wavAudioActivity=null;
							Log.d(LOG_TAG, "Stop2sec");
							canrecodenext = true;
						}
						}



					;

				}, 3000L);
			}









			return buffer;
		}

	public void run() {

		//_thread.start();
		startRecording();
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

	public long getLength() {
		length=3100;
		Wave waveA = new Wave(getComparisonFile());
		length = Math.round(waveA.length()*1000L);
		return length;
	}
}