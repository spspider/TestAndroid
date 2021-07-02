package fr.frazew.virtualgyroscope.hooks.constructor;

import android.hardware.Sensor;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import fr.frazew.virtualgyroscope.hooks.SystemSensorManagerHook;
import java.util.ArrayList;
import java.util.HashMap;

public class API24 extends XC_MethodHook {
    private SystemSensorManagerHook mSystemSensorManagerHook;

    public API24(LoadPackageParam lpparam) {
        this.mSystemSensorManagerHook = new SystemSensorManagerHook(lpparam);
    }

    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        ArrayList<Sensor> mFullSensorsList = (ArrayList) XposedHelpers.getObjectField(param.thisObject, "mFullSensorsList");
        HashMap<Integer, Sensor> mHandleToSensor = (HashMap) ((HashMap) XposedHelpers.getObjectField(param.thisObject, "mHandleToSensor")).clone();
        XposedHelpers.findField(SystemSensorManagerHook.SYSTEM_SENSOR_MANAGER, "mFullSensorsList").setAccessible(true);
        XposedHelpers.findField(SystemSensorManagerHook.SYSTEM_SENSOR_MANAGER, "mHandleToSensor").setAccessible(true);
        this.mSystemSensorManagerHook.fillSensorLists(mFullSensorsList, mHandleToSensor);
        XposedHelpers.setObjectField(param.thisObject, "mHandleToSensor", mHandleToSensor.clone());
        XposedHelpers.setObjectField(param.thisObject, "mFullSensorsList", mFullSensorsList.clone());
    }
}
