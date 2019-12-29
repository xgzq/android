package com.xgzq.yu.reader.utils;

import android.os.Build;

public class Util {

    public static boolean isOver6() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isOver7() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }


}
