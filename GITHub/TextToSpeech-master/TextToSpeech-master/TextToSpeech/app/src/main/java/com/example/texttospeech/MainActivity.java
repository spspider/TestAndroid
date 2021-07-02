package com.example.texttospeech;

import java.util.ArrayList;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private TextToSpeech textToSpeech;
		private EditText etText;
		private Button bSpeak;
		private ImageButton bVoice;

		private final int REQUESTCODE_RECOGNIZESPEECH = 1;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			getControls();

			textToSpeech = new TextToSpeech(getActivity(),
					new OnInitListener() {

						@Override
						public void onInit(int status) {

							if (status == TextToSpeech.SUCCESS)
								Toast.makeText(getActivity(),
										"Text To Speech initialized !",
										Toast.LENGTH_LONG).show();
							else
								Toast.makeText(
										getActivity(),
										"Error occurred while initializing Text To Speech !",
										Toast.LENGTH_LONG).show();

						}
					});

			bSpeak.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String textToSpeak = etText.getText().toString();

					if (textToSpeak.trim().length() > 0) {

						Toast.makeText(getActivity(),
								"Saying : " + textToSpeak, Toast.LENGTH_LONG)
								.show();

						textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_ADD,
								null);

					}

				}
			});

			bVoice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					Intent intent = new Intent(
							RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

					startActivityForResult(intent, REQUESTCODE_RECOGNIZESPEECH);

				}
			});
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);

			if (requestCode == REQUESTCODE_RECOGNIZESPEECH) {

				if (resultCode == RESULT_OK) {

					ArrayList<String> matches = data
							.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

					if (matches.size() > 0)
						textToSpeech.speak(matches.get(0),
								TextToSpeech.QUEUE_ADD, null);

				}

			}
		}

		private void getControls() {
			etText = (EditText) getView().findViewById(R.id.etText);
			bSpeak = (Button) getView().findViewById(R.id.btnSpeak);
			bVoice = (ImageButton) getView().findViewById(R.id.btnVoice);
		}

	}

}
