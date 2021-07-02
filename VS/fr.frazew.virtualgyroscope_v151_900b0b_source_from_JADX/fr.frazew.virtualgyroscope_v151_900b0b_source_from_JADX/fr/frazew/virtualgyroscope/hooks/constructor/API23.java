package fr.frazew.virtualgyroscope.hooks.constructor;

import android.hardware.Sensor;
import android.util.SparseArray;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import fr.frazew.virtualgyroscope.hooks.SystemSensorManagerHook;
import java.util.ArrayList;

public class API23 extends XC_MethodHook {
    private SystemSensorManagerHook mSystemSensorManagerHook;

    public API23(LoadPackageParam lpparam) {
        this.mSystemSensorManagerHook = new SystemSensorManagerHook(lpparam);
    }

    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        ArrayList<Sensor> mFullSensorsList = (ArrayList) XposedHelpers.getObjectField(param.thisObject, "mFullSensorsList");
        SparseArray<Sensor> mHandleToSensor = ((SparseArray) XposedHelpers.getObjectField(param.thisObject, "mHandleToSensor")).clone();
        XposedHelpers.findField(SystemSensorManagerHook.SYSTEM_SENSOR_MANAGER, "mFullSensorsList").setAccessible(true);
        XposedHelpers.findField(SystemSensorManagerHook.SYSTEM_SENSOR_MANAGER, "mHandleToSensor").setAccessible(true);
        this.mSystemSensorManagerHook.fillSensorLists(mFullSensorsList, mHandleToSensor);
        XposedHelpers.setObjectField(param.thisObject, "mHandleToSensor", mHandleToSensor.clone());
        XposedHelpers.setObjectField(param.thisObject, "mFullSensorsList", mFullSensorsList.clone());
    }
}
