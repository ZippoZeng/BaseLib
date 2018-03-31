package com.github.zippo.lib.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Zippo on 2018/2/23.
 * Date: 2018/2/23
 * Time: 16:21:12
 */

public class NetworkUitl {

    private static final String TAG = "NetWorkUitl";


    /**
     * 打开网络设置界面
     *
     * @param activity Activity
     */
    public static void openNetSetting(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 获取本机IP地址
     *
     * @return null：没有网络连接
     */
    public static String getIpAddress() {
        try {
            NetworkInterface nerworkInterface;
            InetAddress inetAddress;
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()){
                nerworkInterface = en.nextElement();
                Enumeration<InetAddress> inetAddresses = nerworkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()){
                    inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
            return null;
        } catch (SocketException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
