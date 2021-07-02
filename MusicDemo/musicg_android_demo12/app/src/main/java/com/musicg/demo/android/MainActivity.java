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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class MainActivity extends Activity{

	public static final String LOG_TAG = "my_logs";

	static MainActivity mainApp;
	
	public static final int DETECT_NONE = 0;
	public static final int DETECT_WHISTLE = 1;
	public static int selectedDetection = DETECT_NONE;
	public static String FILE_TO_COMPARE_NAME="Record";
	// detection parameters
	private DetectorThread detectorThread;
	private RecorderThread recorderThread;
	private int numWhistleDetected = 0;
	static int ProgressValue=0;
	// views
	private View mainView, listeningView;
	private Button whistleButton;
	private Button recordsound;
	private Button stopbutton;
	private Button makeRecorder;
	ProgressBar progressbar;
	WavAudioActivity wavAudioActivity;
	GraphView graph;
	public final static String BROADCAST_ACTION = "WavAudioThread";
	public final static String SIMILIARITY = "similiar";
	public static final String POSITION ="position" ;
	public static final String SIMILIAR = "similiar";

	TextView similiarTextView;
	static ArrayList<Integer> datapoints = new ArrayList<>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("musicg WhistleAPI Demo");

		mainApp = this;
		
		// set views
		LayoutInflater inflater = LayoutInflater.from(this);
		mainView = inflater.inflate(R.layout.main, null);
		listeningView = inflater.inflate(R.layout.listening, null);
		setContentView(mainView);

		whistleButton = (Button) this.findViewById(R.id.whistleButton);
		recordsound = (Button) this.findViewById(R.id.RecordSound);
		stopbutton = (Button)this.findViewById(R.id.stopButton);
		progressbar = (ProgressBar)this.findViewById(R.id.progressBar3);
		makeRecorder = (Button)this.findViewById(R.id.StartRecordForFingerprint);

//		progressbar.setMax(100);
//		progressbar.setProgress(0);
		whistleButton.setOnClickListener(new ClickEvent());
		recordsound.setOnClickListener(new ClickEvent());
		stopbutton.setOnClickListener(new ClickEvent());
		makeRecorder.setOnClickListener(new ClickEvent());

		graph = (GraphView)findViewById(R.id.graph);
		for (int i=0;i<16;i++){
			datapoints.add(i);
		}
		addToGraph(0);
		IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
		// регистрируем (включаем) BroadcastReceiver
		registerReceiver(new PlayerReceiver(), intFilt);
	}
	public class PlayerReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			float similiarity = intent.getFloatExtra(SIMILIARITY, 0);
			float position = intent.getFloatExtra(POSITION, 0);
			float similiar = intent.getFloatExtra(SIMILIAR, 0);
			similiarTextView=(TextView)MainActivity.mainApp.findViewById(R.id.Similiar);
			if (similiarTextView!=null){similiarTextView.setText("Similiar:"+String.valueOf(similiarity)+"position:"+String.valueOf(position)
			+"similiar"+similiar);}
			//int status = intent.getIntExtra(PARAM_STATUS, 0);

		}

	}
public void addToGraph(int point){
	//if (datapoints.size()>15){datapoints.set(0,point);}
	for (int i=0;i<14;i++){
		datapoints.set(i,datapoints.get(i+1));
	}
	datapoints.set(15,point);
	LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
			new DataPoint(0, datapoints.get(0)),
			new DataPoint(1, datapoints.get(1)),
			new DataPoint(2, datapoints.get(2)),
			new DataPoint(3, datapoints.get(3)),
			new DataPoint(4, datapoints.get(4)),
			new DataPoint(5, datapoints.get(5)),
			new DataPoint(6, datapoints.get(6)),
			new DataPoint(7, datapoints.get(7)),
			new DataPoint(8, datapoints.get(8)),
			new DataPoint(9, datapoints.get(9)),
			new DataPoint(10, datapoints.get(10)),
			new DataPoint(11, datapoints.get(11)),
			new DataPoint(12, datapoints.get(12)),
			new DataPoint(13, datapoints.get(13)),
			new DataPoint(14, datapoints.get(14)),
			new DataPoint(15, datapoints.get(15))
	});
	if (graph!=null){
	graph.addSeries(series);}
}
	private void goHomeView() {
		setContentView(mainView);
		if (recorderThread != null) {
			//recorderThread.stopRecording();
			recorderThread = null;
		}
		if (detectorThread != null) {
			//detectorThread.stopDetection();
			detectorThread = null;
		}
		selectedDetection = DETECT_NONE;
	}
	
	private void goListeningView(){
		setContentView(listeningView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Quit demo");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			finish();
			break;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			goHomeView();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class ClickEvent implements OnClickListener {
		public void onClick(View view) {
			if (view == whistleButton) {
				//selectedDetection = DETECT_WHISTLE;
				//stopAllThreads();
				wavAudioActivity = new WavAudioActivity();
				wavAudioActivity.startRecording();
				//wavAudioActivity.Recorde(true);
				/*

				if (recorderThread==null){
					recorderThread = new RecorderThread();
					recorderThread.start();
					 }

				if (detectorThread==null) {
					detectorThread = new DetectorThread(recorderThread);
					detectorThread.start();
				}
				*/
				//goListeningView();
			}
			if (view==recordsound){
				//AudioRecording audioRecording = new AudioRecording();
				//audioRecording.arm();
				//goListeningView();
			}
			if(view ==stopbutton){
				stopAllThreads();
			}
			if (view==makeRecorder){
				if (wavAudioActivity==null){wavAudioActivity=new WavAudioActivity(FILE_TO_COMPARE_NAME);}
				if (!wavAudioActivity.isRecording()){
					wavAudioActivity.startRecording();
					Log.d(LOG_TAG,"need new Audio Record");
					}
				else{
					wavAudioActivity.StopThread();
					//wavAudioActivity.stopRecording();
				}

			}
		}
	}

	private void stopAllThreads() {
		Log.d(LOG_TAG,"Stoptherads-------------------------");
		if (wavAudioActivity!=null){wavAudioActivity.stopRecording();wavAudioActivity=null;}
		if (recorderThread!=null){Log.d(LOG_TAG,"stopThread:recoderThread");recorderThread.StopWaveAudioActivity();recorderThread.stopRecording();recorderThread=null;}else{
			Log.d(LOG_TAG,"recorderThread:isNULL");
		}
		if(detectorThread!=null){Log.d(LOG_TAG,"stopThread:detectorThread");detectorThread.stopDetection();detectorThread=null;}else{
			Log.d(LOG_TAG,"detectorThread:isNULL");
		};

	}

	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
		recorderThread.StopWaveAudioActivity();
		recorderThread.stopRecording();
	}
/*
	@Override
	public void onWhistleDetected() {
		runOnUiThread(new Runnable() {
			public void run() {
				TextView textView = (TextView) MainActivity.mainApp.findViewById(R.id.detectedNumberText);
				textView.setText(String.valueOf(numWhistleDetected++));
			}
		});
	}
*/

}
