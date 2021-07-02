package fr.frazew.virtualgyroscope.UDP;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import fr.frazew.virtualgyroscope.gui.MainActivity;

/**
 * Created by sergey on 15.12.2017.
 */

public class UdpClientHandler extends Handler {
    public static final int UPDATE_STATE = 0;
    public static final int UPDATE_MSG = 1;
    public static final int UPDATE_END = 2;
    private static final int CALIBRATION_RESULTS = 10;
    private MainActivity parent;
    public static float[] returnFloat = {0F, 0.1F, 0.2F};
    private static boolean calibrate_this = false;
    private long[][] returnFloatCalibrate = new long[3][100];
    private static long[] returnFloatCalibrateThis = new long[3];
    private static int calibration_counter = 0;

    public UdpClientHandler(MainActivity mainActivity) {
        this.parent = mainActivity;
    }


    public UdpClientHandler() {

    }


    private void updateRxMsg(String rxmsg) {
        String jsonStr = rxmsg;
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (!calibrate_this) {
                    this.returnFloat[0] = (float) (jsonObj.getLong("X") - returnFloatCalibrateThis[0]) / 10000;
                    this.returnFloat[1] = (float) (jsonObj.getLong("Y") - returnFloatCalibrateThis[1]) / 10000;
                    this.returnFloat[2] = (float) (jsonObj.getLong("Z") - returnFloatCalibrateThis[2]) / 10000;
                } else {
                    if (calibration_counter > CALIBRATION_RESULTS) {

                        long calX = 0;
                        long calY = 0;
                        long calZ = 0;
                        for (int i = 0; i < CALIBRATION_RESULTS; i++) {
                            calX += returnFloatCalibrate[0][i];
                            calY += returnFloatCalibrate[1][i];
                            calZ += returnFloatCalibrate[2][i];
                        }
                        returnFloatCalibrateThis[0] = calX / CALIBRATION_RESULTS;
                        returnFloatCalibrateThis[1] = calY / CALIBRATION_RESULTS;
                        returnFloatCalibrateThis[2] = calZ / CALIBRATION_RESULTS;
                        calibrate_this = false;
                        calibration_counter = 0;
                        if (parent != null) {
                           // Button buttonCalibrate = (Button) parent.findViewById(R.id.calibrate);
                           // buttonCalibrate.setEnabled(true);
                        }
                        Log.e("UDP", "calibration Finished " + Arrays.toString(returnFloatCalibrateThis));

                    }
                    this.returnFloatCalibrate[0][calibration_counter] = jsonObj.getLong("X");
                    this.returnFloatCalibrate[1][calibration_counter] = jsonObj.getLong("Y");
                    this.returnFloatCalibrate[2][calibration_counter] = jsonObj.getLong("Z");
                    calibration_counter++;
                }
            } catch (final JSONException e) {

            }
        }
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case UPDATE_STATE:
                // parent.updateState((String)msg.obj);
                break;
            case UPDATE_MSG:
                updateRxMsg((String) msg.obj);

                //returnFloat[0]=
                break;
            case UPDATE_END:
                // parent.clientEnd();
                break;
            default:
                super.handleMessage(msg);
        }

    }

    public void calibrate() {
        //returnFloat[0]
        calibrate_this = true;
    }

    public float[] returnFloat() {
        return this.returnFloat;
    }
}