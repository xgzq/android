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
		
		
		Logger.v("ceshi vvvv");
		Logger.d("ceshi dddd");
		Logger.i("ceshi iiii");
		Logger.w("ceshi wwww");
		Logger.e("ceshi eeee");
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
