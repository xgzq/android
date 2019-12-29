package com.xgzq.yu.reader.utils;

import android.util.DisplayMetrics;

import com.xgzq.yu.reader.App;

public class DistanceUtil {

    private static final DisplayMetrics sDisplayMetrics;

    static {
        sDisplayMetrics = App.getApp().getResources().getDisplayMetrics();
    }

    public static int getScreenWidth() {
        return sDisplayMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        return sDisplayMetrics.heightPixels;
    }

    public static float dp2px(int dp) {
        return sDisplayMetrics.density * dp;
    }

    public static float sp2px(int sp) {
        return sDisplayMetrics.scaledDensity * sp;
    }

    public static float px2dp(float px) {
        return px / sDisplayMetrics.density;
    }

    public static float px2sp(float px) {
        return px / sDisplayMetrics.scaledDensity;
    }
}
