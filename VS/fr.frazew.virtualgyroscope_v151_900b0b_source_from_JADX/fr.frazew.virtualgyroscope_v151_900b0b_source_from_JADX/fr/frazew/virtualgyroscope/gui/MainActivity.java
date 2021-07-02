package fr.frazew.virtualgyroscope.gui;

import android.app.ActivityManager.TaskDescription;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import fr.frazew.virtualgyroscope.BuildConfig;
import fr.frazew.virtualgyroscope.C0155R;
import fr.frazew.virtualgyroscope.VirtualSensorListener;
import fr.frazew.virtualgyroscope.hooks.SensorChange;

public class MainActivity extends AppCompatActivity {
    private SensorEventListener accelerometerListener;
    private SensorEventListener gyroListener;
    private SensorEventListener magneticListener;
    private SensorManager sensorManager;
    private VirtualSensorListener virtualListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0155R.layout.activity_main);
        if (VERSION.SDK_INT >= 21) {
            setTaskDescription(new TaskDescription(getString(C0155R.string.app_name), BitmapFactory.decodeResource(getResources(), C0155R.mipmap.ic_launcher), getResources().getColor(C0155R.color.cyan_900)));
        }
        setSupportActionBar((Toolbar) findViewById(C0155R.id.toolbar));
        this.sensorManager = (SensorManager) getSystemService("sensor");
        ((TextView) findViewById(C0155R.id.versionValue)).setText(BuildConfig.VERSION_NAME);
        ((TextView) findViewById(C0155R.id.accelerometerValue)).setText(this.sensorManager.getDefaultSensor(1) != null ? "true" : "false");
        final TextView accelerometerValues = (TextView) findViewById(C0155R.id.accelerometerValuesTextValues);
        if (this.sensorManager.getDefaultSensor(1) != null) {
            this.accelerometerListener = new SensorEventListener() {
                public void onSensorChanged(SensorEvent event) {
                    String text = "";
                    for (int i = 0; i < event.values.length; i++) {
                        text = text + (((float) Math.round(event.values[i] * 100.0f)) / 100.0f);
                        if (i < event.values.length - 1) {
                            text = text + "; ";
                        }
                    }
                    accelerometerValues.setText(text);
                }

                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
            this.sensorManager.registerListener(this.accelerometerListener, this.sensorManager.getDefaultSensor(1), 2);
        }
        ((TextView) findViewById(C0155R.id.magneticSensorValue)).setText(this.sensorManager.getDefaultSensor(2) != null ? "true" : "false");
        final TextView magneticValues = (TextView) findViewById(C0155R.id.magneticValuesTextValues);
        if (this.sensorManager.getDefaultSensor(2) != null) {
            this.magneticListener = new SensorEventListener() {
                public void onSensorChanged(SensorEvent event) {
                    String text = "";
                    for (int i = 0; i < event.values.length; i++) {
                        text = text + (((float) Math.round(event.values[i] * 100.0f)) / 100.0f);
                        if (i < event.values.length - 1) {
                            text = text + "; ";
                        }
                    }
                    magneticValues.setText(text);
                }

                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
            this.sensorManager.registerListener(this.magneticListener, this.sensorManager.getDefaultSensor(2), 2);
        }
        ((TextView) findViewById(C0155R.id.gyroscopeValue)).setText(this.sensorManager.getDefaultSensor(4) != null ? "true" : "false");
        final TextView gyroscopeValues = (TextView) findViewById(C0155R.id.gyroscopeValuesTextValues);
        if (this.sensorManager.getDefaultSensor(4) != null) {
            this.gyroListener = new SensorEventListener() {
                public void onSensorChanged(SensorEvent event) {
                    String text = "";
                    for (int i = 0; i < event.values.length; i++) {
                        text = text + (((float) Math.round(event.values[i] * 100.0f)) / 100.0f);
                        if (i < event.values.length - 1) {
                            text = text + "; ";
                        }
                    }
                    gyroscopeValues.setText(text);
                }

                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
            this.sensorManager.registerListener(this.gyroListener, this.sensorManager.getDefaultSensor(4), 2);
        }
        final TextView theoryGyro = (TextView) findViewById(C0155R.id.gyroscopeTheoryValues);
        if (this.sensorManager.getDefaultSensor(1) != null && this.sensorManager.getDefaultSensor(2) != null) {
            final float accelerometerResolution = this.sensorManager.getDefaultSensor(1).getResolution();
            final float magneticResolution = this.sensorManager.getDefaultSensor(2).getResolution();
            this.virtualListener = new VirtualSensorListener(null, null) {
                private SensorChange mSensorChange = new SensorChange();

                public void onSensorChanged(SensorEvent event) {
                    if (event.sensor != null) {
                        float[] values = this.mSensorChange.handleListener(event.sensor, this, event.values, event.accuracy, event.timestamp, accelerometerResolution, magneticResolution);
                        if (values != null) {
                            String text = "";
                            for (int i = 0; i < values.length; i++) {
                                text = text + (((float) Math.round(values[i] * 100.0f)) / 100.0f);
                                if (i < values.length - 1) {
                                    text = text + "; ";
                                }
                            }
                            theoryGyro.setText(text);
                        }
                    }
                }
            };
            this.virtualListener.isDummyGyroListener = true;
            this.sensorManager.registerListener(this.virtualListener, this.sensorManager.getDefaultSensor(1), 2);
            this.sensorManager.registerListener(this.virtualListener, this.sensorManager.getDefaultSensor(2), 2);
        }
    }

    protected void onDestroy() {
        this.sensorManager.unregisterListener(this.accelerometerListener);
        this.sensorManager.unregisterListener(this.magneticListener);
        this.sensorManager.unregisterListener(this.gyroListener);
        this.sensorManager.unregisterListener(this.virtualListener);
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
