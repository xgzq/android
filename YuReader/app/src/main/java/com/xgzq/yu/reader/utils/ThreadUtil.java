package com.xgzq.yu.reader.utils;

import android.util.Log;

public class ThreadUtil {

    public static void printThread(String tag) {
        Thread thread = Thread.currentThread();
        Log.d(tag, thread.toString());
    }
}
