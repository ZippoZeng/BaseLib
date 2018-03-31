package com.github.zippo.lib.utils;

import android.util.Log;

/**
 * Created by Zippo on 2018/2/7.
 * Date: 2018/2/7
 * Time: 15:17:18
 */
public class LeLog {

    private static final String TAG = "hpplay-java";
    private static final String PERFERMANCE = "hpplay-java:perfermance";
    private static final int LOG_ENABLE = 0;
    private static final int LOGV = 10;
    private static final int LOGD = 20;
    private static final int LOGI = 30;
    private static final int LOGW = 40;
    private static final int LOGE = 50;
    private static final int LOG_DISABLE = 100;
    private static int sLevel = 30;

    public LeLog() {
    }

    public static void enableTrace(boolean enable) {
        if (enable) {
            sLevel = 30;
        } else {
            sLevel = 100;
        }

    }

    public static void enableAllTrace() {
        sLevel = 0;
    }

    public static void V(String tag, String msg) {
        if (sLevel <= 10) {
            msg = formatMessage(tag, msg);
            Log.v("hpplay-java:perfermance", msg);
        }
    }

    public static void v(String tag, String msg) {
        if (sLevel <= 10) {
            msg = formatMessage(tag, msg);
            Log.v("hpplay-java", msg);
        }
    }

    public static void V(String tag, String msg, Throwable tr) {
        if (sLevel <= 10) {
            msg = formatMessage(tag, msg);
            Log.v("hpplay-java:perfermance", msg, tr);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (sLevel <= 10) {
            msg = formatMessage(tag, msg);
            Log.v("hpplay-java", msg, tr);
        }
    }

    public static void D(String tag, String msg) {
        if (sLevel <= 20) {
            msg = formatMessage(tag, msg);
            Log.d("hpplay-java:perfermance", msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sLevel <= 20) {
            msg = formatMessage(tag, msg);
            Log.d("hpplay-java", msg);
        }
    }

    public static void D(String tag, String msg, Throwable tr) {
        if (sLevel <= 20) {
            msg = formatMessage(tag, msg);
            Log.d("hpplay-java:perfermance", msg, tr);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (sLevel <= 20) {
            msg = formatMessage(tag, msg);
            Log.d("hpplay-java", msg, tr);
        }
    }

    public static void I(String tag, String msg) {
        if (sLevel <= 30) {
            msg = formatMessage(tag, msg);
            Log.i("hpplay-java:perfermance", msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sLevel <= 30) {
            msg = formatMessage(tag, msg);
            Log.i("hpplay-java", msg);
        }
    }

    public static void I(String tag, String msg, Throwable tr) {
        if (sLevel <= 30) {
            msg = formatMessage(tag, msg);
            Log.i("hpplay-java:perfermance", msg, tr);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (sLevel <= 30) {
            msg = formatMessage(tag, msg);
            Log.i("hpplay-java", msg, tr);
        }
    }

    public static void W(String tag, String msg) {
        if (sLevel <= 40) {
            msg = formatMessage(tag, msg);
            Log.w("hpplay-java:perfermance", msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sLevel <= 40) {
            msg = formatMessage(tag, msg);
            Log.w("hpplay-java", msg);
        }
    }

    public static void W(String tag, String msg, Throwable tr) {
        if (sLevel <= 40) {
            msg = formatMessage(tag, msg);
            Log.w("hpplay-java:perfermance", msg, tr);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (sLevel <= 40) {
            msg = formatMessage(tag, msg);
            Log.w("hpplay-java", msg, tr);
        }
    }

    public static void W(String tag, Throwable tr) {
        if (sLevel <= 40) {
            String msg = formatMessage(tag, (String) null);
            Log.w("hpplay-java:perfermance", msg, tr);
        }
    }

    public static void w(String tag, Throwable tr) {
        if (sLevel <= 40) {
            String msg = formatMessage(tag, (String) null);
            Log.w("hpplay-java", msg, tr);
        }
    }

    public static void E(String tag, String msg) {
        if (sLevel <= 50) {
            msg = formatMessage(tag, msg);
            Log.e("hpplay-java:perfermance", msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sLevel <= 50) {
            msg = formatMessage(tag, msg);
            Log.e("hpplay-java", msg);
        }
    }

    public static void E(String tag, String msg, Throwable tr) {
        if (sLevel <= 50) {
            msg = formatMessage(tag, msg);
            Log.e("hpplay-java:perfermance", msg, tr);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (sLevel <= 50) {
            msg = formatMessage(tag, msg);
            Log.e("hpplay-java", msg, tr);
        }
    }

    private static String formatMessage(String tag, String msg) {
        if (tag == null) {
            tag = "";
        }

        if (msg == null) {
            msg = "";
        }

        String ret = tag + ":" + msg;
        ret = "[" + Thread.currentThread().getName() + "]:" + ret;
        return ret;
    }

}
