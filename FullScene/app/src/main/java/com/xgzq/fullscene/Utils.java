package com.xgzq.fullscene;

import android.util.DisplayMetrics;

public class Utils {

    private static App sApp;
    private static DisplayMetrics sDisplayMetrics;

    public static void init() {
        sApp = App.getApp();
        sDisplayMetrics = sApp.getResources().getDisplayMetrics();
    }

    public static int dp2px(int dp) {
        return (int) (dp * sDisplayMetrics.density);
    }
}
