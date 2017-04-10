package com.zq.utils;

import android.app.Activity;
import android.os.Bundle;

import com.zq.utils.log.Logger;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Logger.isLogBigLevel(false);
		Logger.v("ceshi vvvv");
		Logger.d("ceshi dddd");
		Logger.i("ceshi iiii");
		Logger.w("ceshi wwww");
		Logger.e("ceshi eeee");
	}

}
