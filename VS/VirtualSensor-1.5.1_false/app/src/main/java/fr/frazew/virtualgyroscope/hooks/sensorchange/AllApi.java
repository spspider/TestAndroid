package fr.frazew.virtualgyroscope.hooks.sensorchange;

import android.hardware.Sensor;
import android.util.SparseArray;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import fr.frazew.virtualgyroscope.UDP.UdpClientHandler;
import fr.frazew.virtualgyroscope.VirtualSensorListener;
import fr.frazew.virtualgyroscope.XposedMod;
import fr.frazew.virtualgyroscope.hooks.SensorChange;

/**
 * Created by sergey on 16.12.2017.
 */

public class AllApi extends XC_MethodHook {
    private SensorChange mSensorChange;
    private UdpClientHandler udpClientHandler;
    public AllApi() {
        super(XC_MethodHook.PRIORITY_HIGHEST);
        this.mSensorChange = new SensorChange();
       // this.udpClientHandler=new UdpClientHandler();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
        Object listener = XposedHelpers.getObjectField(param.thisObject, "mListener");
        int handle = (int) param.args[0];
        Object mgr = XposedHelpers.getObjectField(param.thisObject, "mManager");
        SparseArray<Sensor> sensors = (SparseArray<Sensor>) XposedHelpers.getStaticObjectField(mgr.getClass(), "sHandleToSensor");
        Sensor s = sensors.get(handle);

        if (listener instanceof VirtualSensorListener) {
            float[] values = this.mSensorChange.handleListener(s, (VirtualSensorListener) listener, ((float[]) param.args[1]).clone(), (int) param.args[2], (long) param.args[3], XposedMod.ACCELEROMETER_RESOLUTION, XposedMod.MAGNETIC_RESOLUTION);
            //float[] values=this.udpClientHandler.returnFloat(s, (VirtualSensorListener) listener);
            if (values != null) {
                System.arraycopy(values, 0, param.args[1], 0, values.length);
                param.args[0] = XposedMod.sensorTypetoHandle.get(((VirtualSensorListener) listener).getSensor().getType());
            }// else param.setResult(null);
        }
    }
}

