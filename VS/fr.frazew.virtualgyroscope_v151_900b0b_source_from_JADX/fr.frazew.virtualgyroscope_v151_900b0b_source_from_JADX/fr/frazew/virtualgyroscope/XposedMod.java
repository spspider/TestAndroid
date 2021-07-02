package fr.frazew.virtualgyroscope;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.util.SparseIntArray;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import fr.frazew.virtualgyroscope.hooks.sensorchange.API16;
import fr.frazew.virtualgyroscope.hooks.sensorchange.API18;
import fr.frazew.virtualgyroscope.hooks.sensorchange.API23;
import fr.frazew.virtualgyroscope.hooks.sensorchange.API24;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class XposedMod implements IXposedHookLoadPackage {
    public static float ACCELEROMETER_RESOLUTION;
    public static boolean FIRST_LAUNCH_SINCE_BOOT = true;
    public static float MAGNETIC_RESOLUTION;
    public static final SparseIntArray sensorTypetoHandle = new C01571();
    public static final SparseArray<SensorModel> sensorsToEmulate = new C01582();

    static class C01571 extends SparseIntArray {
        C01571() {
            append(11, 4242);
            append(4, 4243);
            if (VERSION.SDK_INT >= 19) {
                put(20, 4244);
            }
            append(9, 4245);
            append(10, 4246);
            if (VERSION.SDK_INT >= 18) {
                append(15, 4247);
            }
        }
    }

    static class C01582 extends SparseArray<SensorModel> {
        C01582() {
            append(11, new SensorModel(11, "VirtualSensor RotationVector", XposedMod.sensorTypetoHandle.get(11), 0.01f, -1, -1.0f, VERSION.SDK_INT > 19 ? "android.sensor.rotation_vector" : "", "none"));
            append(4, new SensorModel(4, "VirtualSensor Gyroscope", XposedMod.sensorTypetoHandle.get(4), 0.01f, -1, 5460.0f, VERSION.SDK_INT > 19 ? "android.sensor.gyroscope" : "", "android.hardware.sensor.gyroscope"));
            if (VERSION.SDK_INT >= 19) {
                append(20, new SensorModel(20, "VirtualSensor GeomagneticRotationVector", XposedMod.sensorTypetoHandle.get(20), 0.01f, -1, -1.0f, VERSION.SDK_INT > 19 ? "android.sensor.geomagnetic_rotation_vector" : "", "none"));
            }
            append(9, new SensorModel(9, "VirtualSensor Gravity", XposedMod.sensorTypetoHandle.get(9), 0.01f, -1, -1.0f, VERSION.SDK_INT > 19 ? "android.sensor.gravity" : "", "none"));
            append(10, new SensorModel(10, "VirtualSensor LinearAcceleration", XposedMod.sensorTypetoHandle.get(10), 0.01f, -1, -1.0f, VERSION.SDK_INT > 19 ? "android.sensor.linear_acceleration" : "", "none"));
            if (VERSION.SDK_INT >= 18) {
                append(15, new SensorModel(15, "VirtualSensor GameRotationVector", XposedMod.sensorTypetoHandle.get(15), 0.01f, -1, -1.0f, VERSION.SDK_INT > 19 ? "android.sensor.game_rotation_vector" : "", "none"));
            }
        }
    }

    class C01593 extends XC_MethodHook {
        C01593() {
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if (XposedMod.sensorsToEmulate.indexOfKey(((Sensor) param.args[0]).getType()) >= 0 && !((SensorModel) XposedMod.sensorsToEmulate.get(((Sensor) param.args[0]).getType())).isAlreadyNative) {
                param.setResult(Integer.valueOf(0));
            }
            super.afterHookedMethod(param);
        }
    }

    class C01604 extends XC_MethodHook {
        C01604() {
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if (XposedMod.sensorsToEmulate.indexOfKey(((Sensor) param.args[0]).getType()) >= 0 && !((SensorModel) XposedMod.sensorsToEmulate.get(((Sensor) param.args[0]).getType())).isAlreadyNative) {
                param.setResult(Integer.valueOf(0));
            }
            super.afterHookedMethod(param);
        }
    }

    class C01615 extends XC_MethodHook {
        C01615() {
        }

        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            if (XposedMod.sensorsToEmulate.indexOfKey(((Sensor) param.args[0]).getType()) >= 0 && !((SensorModel) XposedMod.sensorsToEmulate.get(((Sensor) param.args[0]).getType())).isAlreadyNative) {
                param.setResult(Boolean.valueOf(true));
            }
            super.afterHookedMethod(param);
        }
    }

    class C01626 extends XC_MethodHook {
        C01626() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if (param.args[1] != null) {
                SensorEventListener listener = param.args[0];
                if (XposedMod.sensorsToEmulate.indexOfKey(((Sensor) param.args[1]).getType()) >= 0 && !(listener instanceof VirtualSensorListener) && !((SensorModel) XposedMod.sensorsToEmulate.get(((Sensor) param.args[1]).getType())).isAlreadyNative) {
                    SensorEventListener specialListener = new VirtualSensorListener(listener, (Sensor) param.args[1]);
                    r4 = new Object[4];
                    r4[1] = XposedHelpers.callMethod(param.thisObject, "getDefaultSensor", new Object[]{Integer.valueOf(1)});
                    r4[2] = param.args[2];
                    r4[3] = param.args[3];
                    XposedHelpers.callMethod(param.thisObject, "registerListener", r4);
                    r4 = new Object[4];
                    r4[1] = XposedHelpers.callMethod(param.thisObject, "getDefaultSensor", new Object[]{Integer.valueOf(2)});
                    r4[2] = param.args[2];
                    r4[3] = param.args[3];
                    XposedHelpers.callMethod(param.thisObject, "registerListener", r4);
                    param.args[0] = specialListener;
                }
            }
        }
    }

    class C01637 extends XC_MethodHook {
        C01637() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if (param.args[1] != null) {
                SensorEventListener listener = param.args[0];
                if (XposedMod.sensorsToEmulate.indexOfKey(((Sensor) param.args[1]).getType()) >= 0 && !(listener instanceof VirtualSensorListener) && !((SensorModel) XposedMod.sensorsToEmulate.get(((Sensor) param.args[1]).getType())).isAlreadyNative) {
                    SensorEventListener specialListener = new VirtualSensorListener(listener, (Sensor) param.args[1]);
                    r4 = new Object[6];
                    r4[1] = XposedHelpers.callMethod(param.thisObject, "getDefaultSensor", new Object[]{Integer.valueOf(1)});
                    r4[2] = XposedHelpers.callStaticMethod(SensorManager.class, "getDelay", new Object[]{param.args[2]});
                    r4[3] = null;
                    r4[4] = param.args[3];
                    r4[5] = Integer.valueOf(0);
                    XposedHelpers.callMethod(param.thisObject, "registerListenerImpl", r4);
                    r4 = new Object[6];
                    r4[1] = XposedHelpers.callMethod(param.thisObject, "getDefaultSensor", new Object[]{Integer.valueOf(2)});
                    r4[2] = XposedHelpers.callStaticMethod(SensorManager.class, "getDelay", new Object[]{param.args[2]});
                    r4[3] = null;
                    r4[4] = param.args[3];
                    r4[5] = Integer.valueOf(0);
                    XposedHelpers.callMethod(param.thisObject, "registerListenerImpl", r4);
                    param.args[0] = specialListener;
                }
            }
        }
    }

    class C01648 extends XC_MethodHook {
        C01648() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if (param.args[1] != null) {
                SensorEventListener listener = param.args[0];
                if (XposedMod.sensorsToEmulate.indexOfKey(((Sensor) param.args[1]).getType()) >= 0 && !(listener instanceof VirtualSensorListener) && !((SensorModel) XposedMod.sensorsToEmulate.get(((Sensor) param.args[1]).getType())).isAlreadyNative) {
                    SensorEventListener specialListener = new VirtualSensorListener(listener, (Sensor) param.args[1]);
                    r4 = new Object[6];
                    r4[1] = XposedHelpers.callMethod(param.thisObject, "getDefaultSensor", new Object[]{Integer.valueOf(1)});
                    r4[2] = XposedHelpers.callStaticMethod(SensorManager.class, "getDelay", new Object[]{param.args[2]});
                    r4[3] = param.args[3];
                    r4[4] = Integer.valueOf(0);
                    r4[5] = Integer.valueOf(0);
                    XposedHelpers.callMethod(param.thisObject, "registerListenerImpl", r4);
                    r4 = new Object[6];
                    r4[1] = XposedHelpers.callMethod(param.thisObject, "getDefaultSensor", new Object[]{Integer.valueOf(2)});
                    r4[2] = XposedHelpers.callStaticMethod(SensorManager.class, "getDelay", new Object[]{param.args[2]});
                    r4[3] = param.args[3];
                    r4[4] = Integer.valueOf(0);
                    r4[5] = Integer.valueOf(0);
                    XposedHelpers.callMethod(param.thisObject, "registerListenerImpl", r4);
                    param.args[0] = specialListener;
                }
            }
        }
    }

    class C01659 extends XC_MethodHook {
        C01659() {
        }

        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if (param.args[1] != null) {
                SensorEventListener listener = param.args[0];
                if (XposedMod.sensorsToEmulate.indexOfKey(((Sensor) param.args[1]).getType()) >= 0 && !(listener instanceof VirtualSensorListener) && !((SensorModel) XposedMod.sensorsToEmulate.get(((Sensor) param.args[1]).getType())).isAlreadyNative) {
                    SensorEventListener specialListener = new VirtualSensorListener(listener, (Sensor) param.args[1]);
                    r4 = new Object[6];
                    r4[1] = XposedHelpers.callMethod(param.thisObject, "getDefaultSensor", new Object[]{Integer.valueOf(1)});
                    r4[2] = XposedHelpers.callStaticMethod(SensorManager.class, "getDelay", new Object[]{param.args[2]});
                    r4[3] = param.args[4];
                    r4[4] = param.args[3];
                    r4[5] = Integer.valueOf(0);
                    XposedHelpers.callMethod(param.thisObject, "registerListenerImpl", r4);
                    r4 = new Object[6];
                    r4[1] = XposedHelpers.callMethod(param.thisObject, "getDefaultSensor", new Object[]{Integer.valueOf(2)});
                    r4[2] = XposedHelpers.callStaticMethod(SensorManager.class, "getDelay", new Object[]{param.args[2]});
                    r4[3] = param.args[4];
                    r4[4] = param.args[3];
                    r4[5] = Integer.valueOf(0);
                    XposedHelpers.callMethod(param.thisObject, "registerListenerImpl", r4);
                    param.args[0] = specialListener;
                }
            }
        }
    }

    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("android")) {
            if (FIRST_LAUNCH_SINCE_BOOT) {
                FIRST_LAUNCH_SINCE_BOOT = false;
                XposedBridge.log("VirtualSensor: Using version 1.5.1");
            }
            hookPackageFeatures(lpparam);
        }
        hookSensorValues(lpparam);
        addSensors(lpparam);
        enableSensors(lpparam);
        registerListenerHook(lpparam);
        hookCardboard(lpparam);
    }

    private void hookSensorValues(LoadPackageParam lpparam) {
        if (VERSION.SDK_INT >= 24) {
            XposedHelpers.findAndHookMethod("android.hardware.SystemSensorManager$SensorEventQueue", lpparam.classLoader, "dispatchSensorEvent", new Object[]{Integer.TYPE, float[].class, Integer.TYPE, Long.TYPE, new API24()});
        } else if (VERSION.SDK_INT == 23) {
            XposedHelpers.findAndHookMethod("android.hardware.SystemSensorManager$SensorEventQueue", lpparam.classLoader, "dispatchSensorEvent", new Object[]{Integer.TYPE, float[].class, Integer.TYPE, Long.TYPE, new API23()});
        } else if (VERSION.SDK_INT >= 18) {
            XposedHelpers.findAndHookMethod("android.hardware.SystemSensorManager$SensorEventQueue", lpparam.classLoader, "dispatchSensorEvent", new Object[]{Integer.TYPE, float[].class, Integer.TYPE, Long.TYPE, new API18()});
        } else if (VERSION.SDK_INT >= 16) {
            XposedHelpers.findAndHookMethod("android.hardware.SystemSensorManager$ListenerDelegate", lpparam.classLoader, "onSensorChangedLocked", new Object[]{Sensor.class, float[].class, long[].class, Integer.TYPE, new API16()});
        } else {
            XposedBridge.log("VirtualSensor: Using SDK version " + VERSION.SDK_INT + ", this is not supported");
        }
    }

    private void addSensors(LoadPackageParam lpparam) {
        if (VERSION.SDK_INT >= 24) {
            XposedHelpers.findAndHookConstructor("android.hardware.SystemSensorManager", lpparam.classLoader, new Object[]{Context.class, Looper.class, new fr.frazew.virtualgyroscope.hooks.constructor.API24(lpparam)});
        } else if (VERSION.SDK_INT == 23) {
            XposedHelpers.findAndHookConstructor("android.hardware.SystemSensorManager", lpparam.classLoader, new Object[]{Context.class, Looper.class, new fr.frazew.virtualgyroscope.hooks.constructor.API23(lpparam)});
        } else if (VERSION.SDK_INT >= 18) {
            XposedHelpers.findAndHookConstructor("android.hardware.SystemSensorManager", lpparam.classLoader, new Object[]{Context.class, Looper.class, new fr.frazew.virtualgyroscope.hooks.constructor.API18(lpparam)});
        } else if (VERSION.SDK_INT >= 16) {
            XposedHelpers.findAndHookConstructor("android.hardware.SystemSensorManager", lpparam.classLoader, new Object[]{Looper.class, new fr.frazew.virtualgyroscope.hooks.constructor.API16(lpparam)});
        } else {
            XposedBridge.log("VirtualSensor: Using SDK version " + VERSION.SDK_INT + ", this is not supported");
        }
    }

    private void enableSensors(LoadPackageParam lpparam) throws ClassNotFoundError {
        if (VERSION.SDK_INT >= 18) {
            try {
                XposedHelpers.findAndHookMethod("android.hardware.SystemSensorManager$BaseEventQueue", lpparam.classLoader, "enableSensor", new Object[]{Sensor.class, Integer.TYPE, Integer.TYPE, new C01593()});
            } catch (NoSuchMethodError e) {
                XposedBridge.log("VirtualSensor: Could not hook the AOSP enableSensor method, trying an alternative hook.");
                try {
                    XposedHelpers.findAndHookMethod("android.hardware.SystemSensorManager$BaseEventQueue", lpparam.classLoader, "enableSensor", new Object[]{Sensor.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, new C01604()});
                } catch (NoSuchMethodError e2) {
                    XposedBridge.log("VirtualSensor: The alternative enableSensor hook failed, but the module might still work.");
                }
            }
        } else if (VERSION.SDK_INT >= 16) {
            XposedHelpers.findAndHookMethod("android.hardware.SystemSensorManager", lpparam.classLoader, "enableSensorLocked", new Object[]{Sensor.class, Integer.TYPE, new C01615()});
        } else {
            XposedBridge.log("VirtualSensor: Using SDK version " + VERSION.SDK_INT + ", this is not supported");
        }
    }

    private void registerListenerHook(LoadPackageParam lpparam) {
        if (VERSION.SDK_INT <= 18) {
            XposedHelpers.findAndHookMethod("android.hardware.SensorManager", lpparam.classLoader, "registerListener", new Object[]{SensorEventListener.class, Sensor.class, Integer.TYPE, Handler.class, new C01626()});
        } else {
            XposedHelpers.findAndHookMethod("android.hardware.SensorManager", lpparam.classLoader, "registerListener", new Object[]{SensorEventListener.class, Sensor.class, Integer.TYPE, Integer.TYPE, new C01637()});
            XposedHelpers.findAndHookMethod("android.hardware.SensorManager", lpparam.classLoader, "registerListener", new Object[]{SensorEventListener.class, Sensor.class, Integer.TYPE, Handler.class, new C01648()});
            XposedHelpers.findAndHookMethod("android.hardware.SensorManager", lpparam.classLoader, "registerListener", new Object[]{SensorEventListener.class, Sensor.class, Integer.TYPE, Integer.TYPE, Handler.class, new C01659()});
        }
        XposedHelpers.findAndHookMethod("android.hardware.SystemSensorManager", lpparam.classLoader, "unregisterListenerImpl", new Object[]{SensorEventListener.class, Sensor.class, new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                List<SensorEventListener> listenersToRemove = new ArrayList();
                for (Entry<Object, Object> entry : ((HashMap) XposedHelpers.getObjectField(param.thisObject, "mSensorListeners")).entrySet()) {
                    SensorEventListener listener = (SensorEventListener) entry.getKey();
                    if ((listener instanceof VirtualSensorListener) && ((VirtualSensorListener) listener).getRealListener() == param.args[0]) {
                        listenersToRemove.add(listener);
                    }
                }
                for (SensorEventListener listener2 : listenersToRemove) {
                    XposedHelpers.callMethod(param.thisObject, "unregisterListenerImpl", new Object[]{listener2, null});
                }
            }
        }});
    }

    private void hookPackageFeatures(final LoadPackageParam lpparam) {
        if (VERSION.SDK_INT >= 21) {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.android.server.SystemConfig", lpparam.classLoader), "getAvailableFeatures", new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Map<String, FeatureInfo> mAvailableFeatures = (Map) param.getResult();
                    int openGLEsVersion = ((Integer) XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader), "getInt", new Object[]{"ro.opengles.version", Integer.valueOf(0)})).intValue();
                    if (!mAvailableFeatures.containsKey("android.hardware.sensor.gyroscope")) {
                        FeatureInfo gyro = new FeatureInfo();
                        gyro.name = "android.hardware.sensor.gyroscope";
                        gyro.reqGlEsVersion = openGLEsVersion;
                        mAvailableFeatures.put("android.hardware.sensor.gyroscope", gyro);
                    }
                    XposedHelpers.setObjectField(param.thisObject, "mAvailableFeatures", mAvailableFeatures);
                    param.setResult(mAvailableFeatures);
                }
            });
            return;
        }
        Class<?> pkgMgrSrv = XposedHelpers.findClass("com.android.server.pm.PackageManagerService", lpparam.classLoader);
        XposedBridge.hookAllMethods(pkgMgrSrv, "getSystemAvailableFeatures", new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param.getResult() != null) {
                    synchronized (XposedHelpers.getObjectField(param.thisObject, "mPackages")) {
                        Map<String, FeatureInfo> mAvailableFeatures = (Map) XposedHelpers.getObjectField(param.thisObject, "mAvailableFeatures");
                        int openGLEsVersion = ((Integer) XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader), "getInt", new Object[]{"ro.opengles.version", Integer.valueOf(0)})).intValue();
                        if (!mAvailableFeatures.containsKey("android.hardware.sensor.gyroscope")) {
                            FeatureInfo gyro = new FeatureInfo();
                            gyro.name = "android.hardware.sensor.gyroscope";
                            gyro.reqGlEsVersion = openGLEsVersion;
                            mAvailableFeatures.put("android.hardware.sensor.gyroscope", gyro);
                        }
                        XposedHelpers.setObjectField(param.thisObject, "mAvailableFeatures", mAvailableFeatures);
                    }
                }
            }
        });
        XposedBridge.hookAllMethods(pkgMgrSrv, "hasSystemFeature", new XC_MethodHook() {
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!((Boolean) param.getResult()).booleanValue() && param.args[0] == "android.hardware.sensor.gyroscope") {
                    synchronized (XposedHelpers.getObjectField(param.thisObject, "mPackages")) {
                        Map<String, FeatureInfo> mAvailableFeatures = (Map) param.getResult();
                        int openGLEsVersion = ((Integer) XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader), "getInt", new Object[]{"ro.opengles.version", Integer.valueOf(0)})).intValue();
                        FeatureInfo gyro = new FeatureInfo();
                        gyro.name = "android.hardware.sensor.gyroscope";
                        gyro.reqGlEsVersion = openGLEsVersion;
                        mAvailableFeatures.put("android.hardware.sensor.gyroscope", gyro);
                        XposedHelpers.setObjectField(param.thisObject, "mAvailableFeatures", mAvailableFeatures);
                        param.setResult(Boolean.valueOf(true));
                    }
                }
            }
        });
    }

    private void hookCardboard(LoadPackageParam lpparam) {
        try {
            Class headTransformTMP = XposedHelpers.findClassIfExists("com.google.vrtoolkit.cardboard.HeadTransform", lpparam.classLoader);
            if (headTransformTMP == null) {
                headTransformTMP = XposedHelpers.findClassIfExists("com.google.vr.sdk.base.HeadTransform", lpparam.classLoader);
                if (headTransformTMP != null) {
                    XposedBridge.log("VirtualSensor: Did not find com.google.vrtoolkit.cardboard.HeadTransform but found com.google.vr.sdk.base.HeadTransform");
                }
            }
            final Class headTransform = headTransformTMP;
            final int sensorToUse = VERSION.SDK_INT >= 18 ? 15 : 11;
            if (headTransform != null) {
                XposedBridge.log("VirtualSensor: Found the Google Cardboard library in " + lpparam.packageName + ", hooking HeadTransform");
                XposedHelpers.findAndHookConstructor(headTransform, new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                        SensorManager mgr = (SensorManager) AndroidAppHelper.currentApplication().getSystemService("sensor");
                        mgr.registerListener(new SensorEventListener() {
                            public void onSensorChanged(SensorEvent event) {
                                if (event.sensor.getType() == sensorToUse && event.values != null) {
                                    try {
                                        Field htMatrix = XposedHelpers.findFirstFieldByExactType(headTransform, float[].class);
                                        float[] rotationMatrix = (float[]) htMatrix.get(param.thisObject);
                                        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                                        XposedHelpers.setObjectField(param.thisObject, htMatrix.getName(), rotationMatrix);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                            }
                        }, mgr.getDefaultSensor(sensorToUse), mgr.getDefaultSensor(sensorToUse).getMinDelay());
                        super.afterHookedMethod(param);
                    }
                }});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
