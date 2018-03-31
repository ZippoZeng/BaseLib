package com.github.zippo.lib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Zippo on 2018/2/7.
 * Date: 2018/2/7
 * Time: 15:01:05
 */
public class DeviceUtil {

    private static final String TAG = "DeviceUtil";

    /**
     * 获取DisplayMetrics
     *
     * @param context
     * @return
     */
    private static DisplayMetrics obtain(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDeviceWidth(Context context) {
        DisplayMetrics outMetrics = obtain(context);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getDeviceHeight(Context context) {
        DisplayMetrics outMetrics = obtain(context);
        return outMetrics.heightPixels;
    }

    /**
     * 获取屏幕大小[0]宽，[1]高
     *
     * @param context
     * @return
     */
    public static int[] getDeviceSize(Context context) {
        DisplayMetrics outMetrics = obtain(context);
        int[] sizes = new int[2];
        sizes[0] = outMetrics.widthPixels;
        sizes[1] = outMetrics.heightPixels;
        return sizes;
    }

    /**
     * 获取手机IMEI号
     * add <uses-permission android:name="android.permission.READ_PHONE_STATE" /> in AndroidManifest.xml
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return null == tm ? "" : tm.getDeviceId();
    }

    public boolean isMac(String mac) {
        if (null == mac || mac.length() <= 0) {
            return false;
        }
        String trueMacAddress = "([A-Fa-f0-9]{2}-){5}[A-Fa-f0-9]{2}";
        // 这是真正的MAV地址；正则表达式；
        if (mac.matches(trueMacAddress)) {
            return true;
        } else {
            return false;
        }
    }
}
