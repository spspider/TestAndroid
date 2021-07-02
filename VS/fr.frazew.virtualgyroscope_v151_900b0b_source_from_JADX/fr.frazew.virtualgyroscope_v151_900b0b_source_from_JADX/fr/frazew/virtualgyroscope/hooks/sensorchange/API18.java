package fr.frazew.virtualgyroscope.hooks.sensorchange;

import android.hardware.Sensor;
import android.util.SparseArray;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedHelpers;
import fr.frazew.virtualgyroscope.VirtualSensorListener;
import fr.frazew.virtualgyroscope.XposedMod;
import fr.frazew.virtualgyroscope.hooks.SensorChange;

public class API18 extends XC_MethodHook {
    private SensorChange mSensorChange = new SensorChange();

    public API18() {
        super(10000);
    }

    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        Object listener = XposedHelpers.getObjectField(param.thisObject, "mListener");
        Sensor s = (Sensor) ((SparseArray) XposedHelpers.getStaticObjectField(XposedHelpers.getObjectField(param.thisObject, "mManager").getClass(), "sHandleToSensor")).get(((Integer) param.args[0]).intValue());
        if (listener instanceof VirtualSensorListener) {
            float[] values = this.mSensorChange.handleListener(s, (VirtualSensorListener) listener, (float[]) ((float[]) param.args[1]).clone(), ((Integer) param.args[2]).intValue(), ((Long) param.args[3]).longValue(), XposedMod.ACCELEROMETER_RESOLUTION, XposedMod.MAGNETIC_RESOLUTION);
            if (values != null) {
                System.arraycopy(values, 0, param.args[1], 0, values.length);
                param.args[0] = Integer.valueOf(XposedMod.sensorTypetoHandle.get(((VirtualSensorListener) listener).getSensor().getType()));
            }
        }
    }
}
