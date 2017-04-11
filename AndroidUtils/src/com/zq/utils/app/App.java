package com.zq.utils.app;

import com.zq.utils.log.Logger;

import android.app.Application;
import android.content.Context;

public class App extends Application{
	
	private static App sApp;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sApp = this;
		Logger.initLogger(this);
	}
	
	public synchronized static App newInstance(){
		if(sApp == null){
			sApp = new App();
		}
		return sApp;
	}

	
	public Context getContext(){
		return sApp;
	}
}
