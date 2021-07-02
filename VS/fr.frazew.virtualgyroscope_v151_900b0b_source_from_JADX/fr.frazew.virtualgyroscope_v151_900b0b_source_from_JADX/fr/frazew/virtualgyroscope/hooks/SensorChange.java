package fr.frazew.virtualgyroscope.hooks;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.util.SparseArray;
import de.robv.android.xposed.XposedBridge;
import fr.frazew.virtualgyroscope.Util;
import fr.frazew.virtualgyroscope.VirtualSensorListener;
import java.lang.reflect.Array;

public class SensorChange {
    private static final float[] GRAVITY = new float[]{0.0f, 0.0f, 9.81f};
    private static final float LOWPASS_ALPHA = 0.5f;
    private static final float NS2S = 1.0E-9f;
    private float[] accelerometerValues = new float[3];
    private SparseArray<float[]> filterValues = new SparseArray();
    private float[][] lastFilterValues = ((float[][]) Array.newInstance(Float.TYPE, new int[]{3, 10}));
    private float[] magneticValues = new float[3];
    private float[] prevRotationMatrix = new float[9];
    private long prevTimestamp = 0;
    private float[] prevValues = new float[3];

    public float[] handleListener(Sensor s, VirtualSensorListener listener, float[] values, int accuracy, long timestamp, float accResolution, float magResolution) {
        if (s.getType() == 1) {
            if (Util.checkSensorResolution(this.accelerometerValues, values, accResolution)) {
                this.accelerometerValues = values;
            }
            if (listener.getSensor() != null || listener.isDummyGyroListener) {
                return getSensorValues(listener, timestamp);
            }
        } else if (s.getType() == 2 && Util.checkSensorResolution(this.magneticValues, values, magResolution)) {
            this.magneticValues = values;
        }
        return null;
    }

    private float[] getSensorValues(VirtualSensorListener listener, long timestamp) {
        int i;
        float[] values = new float[3];
        listener.sensorRef = null;
        if (listener.isDummyGyroListener || listener.getSensor().getType() == 4) {
            float timeDifference = Math.abs(((float) (timestamp - this.prevTimestamp)) * NS2S);
            if (timeDifference != 0.0f) {
                values = getGyroscopeValues(timeDifference);
                if (Float.isNaN(values[0]) || Float.isInfinite(values[0])) {
                    XposedBridge.log("VirtualSensor: Value #0 is NaN or Infinity, this should not happen");
                }
                if (Float.isNaN(values[1]) || Float.isInfinite(values[1])) {
                    XposedBridge.log("VirtualSensor: Value #1 is NaN or Infinity, this should not happen");
                }
                if (Float.isNaN(values[2]) || Float.isInfinite(values[2])) {
                    XposedBridge.log("VirtualSensor: Value #2 is NaN or Infinity, this should not happen");
                }
                this.prevTimestamp = timestamp;
            }
        } else if ((VERSION.SDK_INT >= 18 && listener.getSensor().getType() == 15) || ((VERSION.SDK_INT >= 19 && listener.getSensor().getType() == 20) || listener.getSensor().getType() == 11)) {
            rotationMatrix = new float[9];
            SensorManager.getRotationMatrix(rotationMatrix, null, this.accelerometerValues, this.magneticValues);
            float[] quaternion = Util.rotationMatrixToQuaternion(rotationMatrix);
            values[0] = quaternion[1];
            values[1] = quaternion[2];
            values[2] = quaternion[3];
        } else if (listener.getSensor().getType() == 9) {
            rotationMatrix = new float[9];
            SensorManager.getRotationMatrix(rotationMatrix, null, this.accelerometerValues, this.magneticValues);
            values[0] = ((GRAVITY[0] * rotationMatrix[0]) + (GRAVITY[1] * rotationMatrix[3])) + (GRAVITY[2] * rotationMatrix[6]);
            values[1] = ((GRAVITY[0] * rotationMatrix[1]) + (GRAVITY[1] * rotationMatrix[4])) + (GRAVITY[2] * rotationMatrix[7]);
            values[2] = ((GRAVITY[0] * rotationMatrix[2]) + (GRAVITY[1] * rotationMatrix[5])) + (GRAVITY[2] * rotationMatrix[8]);
        } else if (listener.getSensor().getType() == 10) {
            rotationMatrix = new float[9];
            SensorManager.getRotationMatrix(rotationMatrix, null, this.accelerometerValues, this.magneticValues);
            values[0] = this.accelerometerValues[0] - (((GRAVITY[0] * rotationMatrix[0]) + (GRAVITY[1] * rotationMatrix[3])) + (GRAVITY[2] * rotationMatrix[6]));
            values[1] = this.accelerometerValues[1] - (((GRAVITY[0] * rotationMatrix[1]) + (GRAVITY[1] * rotationMatrix[4])) + (GRAVITY[2] * rotationMatrix[7]));
            values[2] = this.accelerometerValues[2] - (((GRAVITY[0] * rotationMatrix[2]) + (GRAVITY[1] * rotationMatrix[5])) + (GRAVITY[2] * rotationMatrix[8]));
        }
        if (listener.isDummyGyroListener) {
            i = 42;
        } else {
            i = listener.getSensor().getType();
        }
        return sensorLowPass(values, i);
    }

    private float[] sensorLowPass(float[] values, int sensorType) {
        float[] filteredValues = new float[values.length];
        float[] previousValues = (float[]) this.filterValues.get(sensorType);
        if (previousValues == null) {
            this.filterValues.put(sensorType, values);
            return (float[]) values.clone();
        }
        for (int i = 0; i < values.length; i++) {
            filteredValues[i] = lowPass(LOWPASS_ALPHA, values[i], previousValues[i]);
        }
        this.filterValues.put(sensorType, filteredValues);
        return filteredValues;
    }

    private static float lowPass(float alpha, float value, float prev) {
        return ((value - prev) * alpha) + prev;
    }

    private float[] getGyroscopeValues(float timeDifference) {
        float[] angularRates = new float[]{0.0f, 0.0f, 0.0f};
        rotationMatrix = new float[9];
        gravityRot = new float[3];
        angleChange = new float[3];
        SensorManager.getRotationMatrix(rotationMatrix, null, this.accelerometerValues, this.magneticValues);
        gravityRot[0] = ((GRAVITY[0] * rotationMatrix[0]) + (GRAVITY[1] * rotationMatrix[3])) + (GRAVITY[2] * rotationMatrix[6]);
        gravityRot[1] = ((GRAVITY[0] * rotationMatrix[1]) + (GRAVITY[1] * rotationMatrix[4])) + (GRAVITY[2] * rotationMatrix[7]);
        gravityRot[2] = ((GRAVITY[0] * rotationMatrix[2]) + (GRAVITY[1] * rotationMatrix[5])) + (GRAVITY[2] * rotationMatrix[8]);
        SensorManager.getRotationMatrix(rotationMatrix, null, gravityRot, this.magneticValues);
        SensorManager.getAngleChange(angleChange, rotationMatrix, this.prevRotationMatrix);
        angularRates[0] = (-angleChange[1]) / timeDifference;
        angularRates[1] = angleChange[2] / timeDifference;
        angularRates[2] = angleChange[0] / timeDifference;
        this.prevRotationMatrix = rotationMatrix;
        return angularRates;
    }

    private float[] filterValues(float[] values) {
        if (Float.isInfinite(values[0]) || Float.isNaN(values[0])) {
            values[0] = this.prevValues[0];
        }
        if (Float.isInfinite(values[1]) || Float.isNaN(values[1])) {
            values[1] = this.prevValues[1];
        }
        if (Float.isInfinite(values[2]) || Float.isNaN(values[2])) {
            values[2] = this.prevValues[2];
        }
        float[] filteredValues = new float[3];
        float[][] newLastFilterValues = (float[][]) Array.newInstance(Float.TYPE, new int[]{3, 10});
        for (int i = 0; i < 3; i++) {
            int j;
            float newValue = lowPass(LOWPASS_ALPHA, values[i], this.prevValues[i]);
            for (j = 0; j < 10; j++) {
                if (j != 0) {
                    newLastFilterValues[i][j - 1] = this.lastFilterValues[i][j];
                }
            }
            newLastFilterValues[i][9] = newValue;
            float sum = 0.0f;
            for (j = 0; j < 10; j++) {
                sum += this.lastFilterValues[i][j];
            }
            newValue = sum / 10.0f;
            if (newValue != 0.0f && Math.abs(newValue) < 0.01f) {
                newValue = 0.0f;
            }
            filteredValues[i] = newValue;
        }
        this.prevValues = filteredValues;
        this.lastFilterValues = newLastFilterValues;
        return filteredValues;
    }
}
