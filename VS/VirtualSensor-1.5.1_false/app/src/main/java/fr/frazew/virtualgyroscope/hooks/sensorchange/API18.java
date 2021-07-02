package fr.frazew.virtualgyroscope.hooks.sensorchange;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.SparseArray;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import fr.frazew.virtualgyroscope.UDP.ReceiveSocket;
import fr.frazew.virtualgyroscope.UDP.UdpClientHandler;
import fr.frazew.virtualgyroscope.VirtualSensorListener;
import fr.frazew.virtualgyroscope.XposedMod;
import fr.frazew.virtualgyroscope.hooks.SensorChange;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static de.robv.android.xposed.XposedBridge.log;

public class API18 extends XC_MethodHook{
    private SensorChange mSensorChange;
    private UdpClientHandler udpClientHandler;
    public static float[] values = {0.4F,0.4F,0.4F};
    static ReceiveSocket receiveSocket;
    Context context;
    public API18() {
        super(XC_MethodHook.PRIORITY_HIGHEST);
//        this.mSensorChange = new SensorChange();
//        this.udpClientHandler=new UdpClientHandler();

        takeContext();
    }
    void takeContext() {
        XposedBridge.hookAllConstructors(XposedMod.class, new XC_MethodHook() {
            Context appContext;

            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("initializing");

                //The context that you mod (usually android)
                //It happens that the constructor of the constructor to be hooked has the context as the first argument
                //I think AndroidAppHelper.currentApplication() is a more general way to do it
                Context modContext = (Context) param.args[0];

                //Context of your app
                appContext = modContext.createPackageContext(
                        PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY);

                Resources modRes = modContext.getResources();
                Resources appRes = appContext.getResources();
                if (appContext != null) {

                    if ((receiveSocket == null) && (isNetworkAvailable(appContext))) {
                        Log.e("UDP", "New ReceiveSocket");
                        receiveSocket = new ReceiveSocket();
                        receiveSocket.execute();
                        Log.e("UDP", "ReceiveSocket execute");
                    }
                }
            }
        });
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
            //float[] values = this.mSensorChange.handleListener(s, (VirtualSensorListener) listener, ((float[]) param.args[1]).clone(), (int) param.args[2], (long) param.args[3], XposedMod.ACCELEROMETER_RESOLUTION, XposedMod.MAGNETIC_RESOLUTION);
            //float[] values=this.udpClientHandler.returnFloat();
            float[] values=ReceiveSocket.values;
            if (values != null) {
                System.arraycopy(values, 0, param.args[1], 0, values.length);
                param.args[0] = XposedMod.sensorTypetoHandle.get(((VirtualSensorListener) listener).getSensor().getType());
            }// else param.setResult(null);
        }

    }
    private boolean isNetworkAvailable(Context appContext) {
        Context context = AndroidAppHelper.currentApplication();
        if (context!=null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        Log.e("UDP","no Context");
        return false;

    }


}