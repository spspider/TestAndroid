package fr.frazew.virtualgyroscope.hooks;

import android.content.BroadcastReceiver;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.SparseArray;

import de.robv.android.xposed.XposedBridge;
import fr.frazew.virtualgyroscope.UDP.ReceiveSocket;
import fr.frazew.virtualgyroscope.Util;
import fr.frazew.virtualgyroscope.VirtualSensorListener;

public class SensorChange {
    private static final float[] GRAVITY = new float[]{0F, 0F, 9.81F};
    private static final float NS2S = 1.0f / 1000000000.0f;
    private static final float LOWPASS_ALPHA = 0.5F;

    // Filter stuff @TODO
    private float lastFilterValues[][] = new float[3][10];
    private float prevValues[] = new float[3];

    //Sensor values
    private float[] magneticValues = {1F, 0.1F, 0.2F, 0.4F};
    private float[] accelerometerValues = new float[3];

    //Keeping track of the previous rotation matrix and timestamp
    private float[] prevRotationMatrix = new float[9];
    private long prevTimestamp = 0;
    private SparseArray<float[]> filterValues = new SparseArray<>();
    //private float[] returntedFloatVal = new float[3];
    public static float[] new_values={0.1F,0.2F,0.3F};
    public float[] test_static_values={0.2F,0.2F,0.2F};
    private ReceiveSocket receiveSocket;

    public float[] handleListener(Sensor s, VirtualSensorListener listener, float[] values, int accuracy, long timestamp, float accResolution, float magResolution) {
        if (listener.getSensor() != null || listener.isDummyGyroListener) {
            //return returnFloat();
            return this.getSensorValues_wifi(listener, timestamp);
        } else if (s.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // if (Util.checkSensorResolution(this.magneticValues, values, magResolution)) {
            //this.magneticValues = values;
            //}
        }

        return null;
    }

    private float[] getSensorValues_wifi(VirtualSensorListener listener, long timestamp) {
        float[] values = test_static_values;
        listener.sensorRef = null; // Avoid sending completely wrong values to the wrong sensor
        if (listener.isDummyGyroListener || listener.getSensor().getType() == Sensor.TYPE_GYROSCOPE) {
            //float timeDifference = Math.abs((float) (timestamp - this.prevTimestamp) * NS2S);
            //if (timeDifference != 0.0F) {

            int min = 0;
            int max = 10;
/*
                Random r = new Random();
                float i1 = (r.nextInt(max - min + 1) + min) / 10.0F;
                this.Retvalues = new float[]{i1, i1, i1,};
                values = Retvalues;
*/

            //values= UdpClientHandler.returnFloat;
            //values= try_recieveAsynckTak();
            //values=Singleton.getInstance().getValues();

            if (Float.isNaN(values[0]) || Float.isInfinite(values[0]))
                XposedBridge.log("VirtualSensor: Value #" + 0 + " is NaN or Infinity, this should not happen");

            if (Float.isNaN(values[1]) || Float.isInfinite(values[1]))
                XposedBridge.log("VirtualSensor: Value #" + 1 + " is NaN or Infinity, this should not happen");

            if (Float.isNaN(values[2]) || Float.isInfinite(values[2]))
                XposedBridge.log("VirtualSensor: Value #" + 2 + " is NaN or Infinity, this should not happen");
            ///this.prevTimestamp = timestamp;
            // }
        }
        return values;
    }

    private float[] try_recieveAsynckTak() {

        return new float[3];
    }


    private float[] getSensorValues(VirtualSensorListener listener, long timestamp) {
        float[] values = new float[3];
        BroadcastReceiver br;
        listener.sensorRef = null; // Avoid sending completely wrong values to the wrong sensor
        if (listener.isDummyGyroListener || listener.getSensor().getType() == Sensor.TYPE_GYROSCOPE) {
            float timeDifference = Math.abs((float) (timestamp - this.prevTimestamp) * NS2S);
            if (timeDifference != 0.0F) {
                values = this.getGyroscopeValues(timeDifference);
                values[0] = 1.F;
                values[1] = 1.F;
                values[2] = 1.F;
                if (Float.isNaN(values[0]) || Float.isInfinite(values[0]))
                    XposedBridge.log("VirtualSensor: Value #" + 0 + " is NaN or Infinity, this should not happen");

                if (Float.isNaN(values[1]) || Float.isInfinite(values[1]))
                    XposedBridge.log("VirtualSensor: Value #" + 1 + " is NaN or Infinity, this should not happen");

                if (Float.isNaN(values[2]) || Float.isInfinite(values[2]))
                    XposedBridge.log("VirtualSensor: Value #" + 2 + " is NaN or Infinity, this should not happen");
                this.prevTimestamp = timestamp;
            }
        } else if ((Build.VERSION.SDK_INT >= 18 && listener.getSensor().getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) || (Build.VERSION.SDK_INT >= 19 && listener.getSensor().getType() == Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) || listener.getSensor().getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float[] rotationMatrix = new float[9];

            SensorManager.getRotationMatrix(rotationMatrix, null, this.accelerometerValues, this.magneticValues);
            float[] quaternion = Util.rotationMatrixToQuaternion(rotationMatrix);

            values[0] = quaternion[1];
            values[1] = quaternion[2];
            values[2] = quaternion[3];
        } else if (listener.getSensor().getType() == Sensor.TYPE_GRAVITY) {
            float[] rotationMatrix = new float[9];

            SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magneticValues);
            values[0] = GRAVITY[0] * rotationMatrix[0] + GRAVITY[1] * rotationMatrix[3] + GRAVITY[2] * rotationMatrix[6];
            values[1] = GRAVITY[0] * rotationMatrix[1] + GRAVITY[1] * rotationMatrix[4] + GRAVITY[2] * rotationMatrix[7];
            values[2] = GRAVITY[0] * rotationMatrix[2] + GRAVITY[1] * rotationMatrix[5] + GRAVITY[2] * rotationMatrix[8];
        } else if (listener.getSensor().getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float[] rotationMatrix = new float[9];

            SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magneticValues);
            values[0] = this.accelerometerValues[0] - (GRAVITY[0] * rotationMatrix[0] + GRAVITY[1] * rotationMatrix[3] + GRAVITY[2] * rotationMatrix[6]);
            values[1] = this.accelerometerValues[1] - (GRAVITY[0] * rotationMatrix[1] + GRAVITY[1] * rotationMatrix[4] + GRAVITY[2] * rotationMatrix[7]);
            values[2] = this.accelerometerValues[2] - (GRAVITY[0] * rotationMatrix[2] + GRAVITY[1] * rotationMatrix[5] + GRAVITY[2] * rotationMatrix[8]);
        }

        return sensorLowPass(values, listener.isDummyGyroListener ? 42 : listener.getSensor().getType());
    }

    private float[] sensorLowPass(float[] values, int sensorType) {
        float[] filteredValues = new float[values.length];
        float[] previousValues = this.filterValues.get(sensorType);

        if (previousValues == null) { // This sensor hasn't been used, add it and return the values
            this.filterValues.put(sensorType, values);
            filteredValues = values.clone();
        } else {
            for (int i = 0; i < values.length; i++) {
                filteredValues[i] = lowPass(LOWPASS_ALPHA, values[i], previousValues[i]);
            }
            this.filterValues.put(sensorType, filteredValues);
        }

        return filteredValues;
    }

    private static float lowPass(float alpha, float value, float prev) {
        return prev + alpha * (value - prev);
    }

    private float[] getGyroscopeValues(float timeDifference) {
        float[] angularRates = new float[]{0.0F, 0.0F, 0.0F};
        float[] rotationMatrix = new float[9];
        float[] gravityRot = new float[3];
        float[] angleChange = new float[3];

        SensorManager.getRotationMatrix(rotationMatrix, null, this.accelerometerValues, this.magneticValues);
        gravityRot[0] = GRAVITY[0] * rotationMatrix[0] + GRAVITY[1] * rotationMatrix[3] + GRAVITY[2] * rotationMatrix[6];
        gravityRot[1] = GRAVITY[0] * rotationMatrix[1] + GRAVITY[1] * rotationMatrix[4] + GRAVITY[2] * rotationMatrix[7];
        gravityRot[2] = GRAVITY[0] * rotationMatrix[2] + GRAVITY[1] * rotationMatrix[5] + GRAVITY[2] * rotationMatrix[8];
        SensorManager.getRotationMatrix(rotationMatrix, null, gravityRot, this.magneticValues);

        SensorManager.getAngleChange(angleChange, rotationMatrix, this.prevRotationMatrix);
        angularRates[0] = -(angleChange[1]) / timeDifference;
        angularRates[1] = (angleChange[2]) / timeDifference;
        angularRates[2] = (angleChange[0]) / timeDifference;

        this.prevRotationMatrix = rotationMatrix;
        return angularRates;
    }

    private float[] filterValues(float[] values) { // @TODO Move to a better filter, such as a Kalman filter
        if (Float.isInfinite(values[0]) || Float.isNaN(values[0])) values[0] = this.prevValues[0];
        if (Float.isInfinite(values[1]) || Float.isNaN(values[1])) values[1] = this.prevValues[1];
        if (Float.isInfinite(values[2]) || Float.isNaN(values[2])) values[2] = this.prevValues[2];

        float[] filteredValues = new float[3];
        float[][] newLastFilterValues = new float[3][10];
        for (int i = 0; i < 3; i++) {
            // Apply lowpass on the value
            float alpha = 0.5F;
            float newValue = lowPass(alpha, values[i], this.prevValues[i]);

            for (int j = 0; j < 10; j++) {
                if (j == 0) continue;
                newLastFilterValues[i][j - 1] = this.lastFilterValues[i][j];
            }
            newLastFilterValues[i][9] = newValue;

            float sum = 0F;
            for (int j = 0; j < 10; j++) {
                sum += this.lastFilterValues[i][j];
            }
            newValue = sum / 10;

            //The gyroscope is moving even after lowpass
            if (newValue != 0.0F) {
                //We are under the declared resolution of the gyroscope, so the value should be 0
                if (Math.abs(newValue) < 0.01F) newValue = 0.0F;
            }

            filteredValues[i] = newValue;
        }

        this.prevValues = filteredValues;
        this.lastFilterValues = newLastFilterValues;
        return filteredValues;
    }


}