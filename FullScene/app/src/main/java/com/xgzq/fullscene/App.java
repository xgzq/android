package com.xgzq.fullscene;

import android.app.Application;

public class App extends Application {

    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        Utils.init();
    }

    public static App getApp() {
        return sApp;
    }
}
