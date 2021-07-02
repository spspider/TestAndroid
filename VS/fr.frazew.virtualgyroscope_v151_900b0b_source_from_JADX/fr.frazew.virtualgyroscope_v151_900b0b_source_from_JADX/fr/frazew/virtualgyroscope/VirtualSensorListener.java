package fr.frazew.virtualgyroscope;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class VirtualSensorListener implements SensorEventListener {
    public boolean isDummyGyroListener = false;
    private SensorEventListener realListener = null;
    private Sensor registeredSensor = null;
    public Sensor sensorRef = null;

    public VirtualSensorListener(SensorEventListener realListener, Sensor registeredSensor) {
        this.realListener = realListener;
        this.registeredSensor = registeredSensor;
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == this.registeredSensor.getType()) {
            event.accuracy = 3;
            this.realListener.onSensorChanged(event);
        }
    }

    public Sensor getSensor() {
        return this.registeredSensor;
    }

    public SensorEventListener getRealListener() {
        return this.realListener;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
