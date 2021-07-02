package fr.frazew.virtualgyroscope.hooks.constructor;

import android.hardware.Sensor;
import android.util.SparseArray;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import fr.frazew.virtualgyroscope.hooks.SystemSensorManagerHook;
import java.util.ArrayList;

public class API16 extends XC_MethodHook {
    private Class SENSOR_EVENT_POOL;
    private SystemSensorManagerHook mSystemSensorManagerHook;

    public API16(LoadPackageParam lpparam) {
        this.mSystemSensorManagerHook = new SystemSensorManagerHook(lpparam);
        this.SENSOR_EVENT_POOL = XposedHelpers.findClass("android.hardware.SensorManager$SensorEventPool", lpparam.classLoader);
    }

    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        synchronized (((ArrayList) XposedHelpers.getStaticObjectField(param.thisObject.getClass(), "sListeners"))) {
            ArrayList<Sensor> sFullSensorsList = (ArrayList) XposedHelpers.getStaticObjectField(param.thisObject.getClass(), "sFullSensorsList");
            SparseArray<Sensor> sHandleToSensor = (SparseArray) XposedHelpers.getStaticObjectField(param.thisObject.getClass(), "sHandleToSensor");
            XposedHelpers.findField(SystemSensorManagerHook.SYSTEM_SENSOR_MANAGER, "sFullSensorsList").setAccessible(true);
            XposedHelpers.findField(SystemSensorManagerHook.SYSTEM_SENSOR_MANAGER, "sHandleToSensor").setAccessible(true);
            this.mSystemSensorManagerHook.fillSensorLists(sFullSensorsList, sHandleToSensor);
            XposedHelpers.setStaticObjectField(param.thisObject.getClass(), "sHandleToSensor", sHandleToSensor.clone());
            XposedHelpers.setStaticObjectField(param.thisObject.getClass(), "sFullSensorsList", sFullSensorsList.clone());
            XposedHelpers.setStaticObjectField(param.thisObject.getClass(), "sPool", XposedHelpers.newInstance(this.SENSOR_EVENT_POOL, new Object[]{Integer.TYPE, Integer.valueOf(sHandleToSensor.size() * 2)}));
        }
    }
}
