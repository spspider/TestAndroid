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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;

import com.musicg.api.ClapApi;
import com.musicg.api.WhistleApi;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;
import com.musicg.wave.WaveHeader;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Environment;
import android.util.Log;

public class DetectorThread extends Thread{
	public String LOG_TAG="my_logs";
	private RecorderThread recorder;
	private WaveHeader waveHeader;
	private WhistleApi whistleApi;
	private ClapApi clapApi;
	private volatile Thread _thread;
	private LinkedList<Boolean> whistleResultList = new LinkedList<Boolean>();
	private int numWhistles;
	private int whistleCheckLength = 3;

	private OnSignalsDetectedListener onSignalsDetectedListener;
	private void Fingepint(Wave w1){
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


	public DetectorThread(RecorderThread recorder){
		this.recorder = recorder;
		AudioRecord audioRecord = recorder.getAudioRecord();

		int channel = 0;
		// whistle detection only supports mono channel
		if (audioRecord.getChannelConfiguration() == AudioFormat.CHANNEL_IN_MONO){
			channel = 1;
		}

		waveHeader = new WaveHeader();
		waveHeader.setChannels(channel);
		waveHeader.setBitsPerSample(WavAudioActivity.RECORDER_BPP);
		waveHeader.setSampleRate(audioRecord.getSampleRate());

		//whistleApi = new WhistleApi(waveHeader);
		//clapApi = new ClapApi(waveHeader);
		//FingerprintSimilarity fingerprintSimilarity =new FingerprintSimilarity();
		//fingerprintSimilarity.
		//whistleApi.
	}

	private void initBuffer() {
		numWhistles = 0;
		whistleResultList.clear();
		
		// init the first frames
		for (int i = 0; i < whistleCheckLength; i++) {
			whistleResultList.add(false);
		}
		// end init the first frames
	}

	public void start() {
		_thread = new Thread(this);
        _thread.start();
    }
	
	public void stopDetection(){
		_thread = null;

	}

	public void run() {
		try {
			byte[] buffer;
			initBuffer();
			
			Thread thisThread = Thread.currentThread();
			while (_thread == thisThread) {
				// detect sound
				buffer = recorder.getFrameBytes();
				if (buffer != null) {
					/*
					boolean isWhistle = whistleApi.isWhistle(buffer);
					boolean isClap = clapApi.isClap(buffer);

					if (isClap){Log.d(LOG_TAG, "Clap"+Boolean.toString(isClap));}
					if (isWhistle){Log.d(LOG_TAG, "Whistle"+Boolean.toString(isWhistle));}
*/
				// end whistle detection
				}
				else{

				}
				// end audio analyst
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onWhistleDetected(){
		if (onSignalsDetectedListener != null){
			onSignalsDetectedListener.onWhistleDetected();
		}
	}
	
	public void setOnSignalsDetectedListener(OnSignalsDetectedListener listener){
		onSignalsDetectedListener = listener;
	}
}