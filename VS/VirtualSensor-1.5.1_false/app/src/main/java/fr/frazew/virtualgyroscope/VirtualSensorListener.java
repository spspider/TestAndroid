package fr.frazew.virtualgyroscope;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class VirtualSensorListener implements SensorEventListener {
    private SensorEventListener realListener = null;
    private Sensor registeredSensor = null;
    public Sensor sensorRef = null;
    public boolean isDummyGyroListener = false;

    public VirtualSensorListener(SensorEventListener realListener, Sensor registeredSensor) {
        this.realListener = realListener;
        this.registeredSensor = registeredSensor;
        Context context = (Context) AndroidAppHelper.currentApplication();
        if (context!=null){
            Log.d("UDP","CONTEXT!!!access!!!VirtualSensorListener");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == this.registeredSensor.getType()) {
            event.accuracy = SensorManager.SENSOR_STATUS_ACCURACY_HIGH;
            realListener.onSensorChanged(event);
        }
    }

    public Sensor getSensor() {
        return this.registeredSensor;
    }

    public SensorEventListener getRealListener() {
        return this.realListener;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


}
