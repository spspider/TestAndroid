package fr.frazew.virtualgyroscope.hooks;

import android.hardware.Sensor;
import android.os.Build.VERSION;
import android.util.SparseArray;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import fr.frazew.virtualgyroscope.BuildConfig;
import fr.frazew.virtualgyroscope.SensorModel;
import fr.frazew.virtualgyroscope.XposedMod;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SystemSensorManagerHook {
    public static Class SYSTEM_SENSOR_MANAGER;

    public SystemSensorManagerHook(LoadPackageParam lpparam) {
        SYSTEM_SENSOR_MANAGER = XposedHelpers.findClass("android.hardware.SystemSensorManager", lpparam.classLoader);
    }

    public void fillSensorLists(ArrayList<Sensor> fullSensorList, Object handleToSensor) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Iterator<Sensor> iterator = fullSensorList.iterator();
        int minDelayAccelerometer = 0;
        List<SensorModel> sensorsNotToAdd = new ArrayList();
        while (iterator.hasNext()) {
            Sensor sensor = (Sensor) iterator.next();
            if (XposedMod.sensorsToEmulate.indexOfKey(sensor.getType()) >= 0) {
                sensorsNotToAdd.add(XposedMod.sensorsToEmulate.get(sensor.getType()));
                if (!sensor.getVendor().equals("Xposed")) {
                    ((SensorModel) XposedMod.sensorsToEmulate.get(sensor.getType())).isAlreadyNative = true;
                }
            }
            if (sensor.getType() == 1) {
                minDelayAccelerometer = sensor.getMinDelay();
                XposedMod.ACCELEROMETER_RESOLUTION = sensor.getResolution();
            }
            if (sensor.getType() == 2) {
                XposedMod.MAGNETIC_RESOLUTION = sensor.getResolution();
            }
        }
        XposedHelpers.findConstructorBestMatch(Sensor.class, new Class[0]).setAccessible(true);
        XposedHelpers.findField(Sensor.class, "mName").setAccessible(true);
        XposedHelpers.findField(Sensor.class, "mType").setAccessible(true);
        XposedHelpers.findField(Sensor.class, "mVendor").setAccessible(true);
        XposedHelpers.findField(Sensor.class, "mVersion").setAccessible(true);
        XposedHelpers.findField(Sensor.class, "mHandle").setAccessible(true);
        XposedHelpers.findField(Sensor.class, "mResolution").setAccessible(true);
        XposedHelpers.findField(Sensor.class, "mMinDelay").setAccessible(true);
        XposedHelpers.findField(Sensor.class, "mMaxRange").setAccessible(true);
        if (VERSION.SDK_INT > 19) {
            XposedHelpers.findField(Sensor.class, "mStringType").setAccessible(true);
        }
        if (VERSION.SDK_INT > 19) {
            XposedHelpers.findField(Sensor.class, "mRequiredPermission").setAccessible(true);
        }
        for (int i = 0; i < XposedMod.sensorsToEmulate.size(); i++) {
            SensorModel model = (SensorModel) XposedMod.sensorsToEmulate.valueAt(i);
            if (!sensorsNotToAdd.contains(model)) {
                Sensor s = (Sensor) XposedHelpers.findConstructorBestMatch(Sensor.class, new Class[0]).newInstance(new Object[0]);
                XposedHelpers.setObjectField(s, "mType", Integer.valueOf(XposedMod.sensorsToEmulate.keyAt(i)));
                XposedHelpers.setObjectField(s, "mName", model.name);
                XposedHelpers.setObjectField(s, "mVendor", "Xposed");
                XposedHelpers.setObjectField(s, "mVersion", Integer.valueOf(BuildConfig.VERSION_CODE));
                XposedHelpers.setObjectField(s, "mHandle", Integer.valueOf(model.handle));
                XposedHelpers.setObjectField(s, "mMinDelay", Integer.valueOf(model.minDelay == -1 ? minDelayAccelerometer : model.minDelay));
                if (VERSION.SDK_INT > 19) {
                    XposedHelpers.setObjectField(s, "mStringType", model.stringType);
                }
                XposedHelpers.setObjectField(s, "mResolution", Float.valueOf(model.resolution == -1.0f ? 0.01f : model.resolution));
                if (model.maxRange != -1.0f) {
                    XposedHelpers.setObjectField(s, "mMaxRange", Float.valueOf(model.maxRange));
                }
                if (!model.permission.equals("none") && VERSION.SDK_INT > 19) {
                    XposedHelpers.setObjectField(s, "mRequiredPermission", model.permission);
                }
                fullSensorList.add(s);
                if (handleToSensor.getClass() == SparseArray.class) {
                    ((SparseArray) handleToSensor).put(model.handle, s);
                } else if (handleToSensor.getClass() == HashMap.class) {
                    ((HashMap) handleToSensor).put(Integer.valueOf(model.handle), s);
                }
            }
        }
    }
}
