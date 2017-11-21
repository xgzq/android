package com.xgzq.android.miguplayer.activity;

import java.lang.Thread.UncaughtExceptionHandler;

import com.xgzq.android.miguplayer.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

@SuppressLint({ "RtlHardcoded", "NewApi" })
public class MainActivity extends Activity implements OnTouchListener,OnClickListener
{

	private ViewStub mViewStub;
	private Button mFloatingButton;
	private LayoutParams mLayoutParams;
	private WindowManager mWindowManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mViewStub = (ViewStub) findViewById(R.id.main_view_sub);
		
		mFloatingButton = new Button(this);
		mFloatingButton.setText("FloatingButton");
		
		mLayoutParams = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0,0,PixelFormat.TRANSPARENT);
		mLayoutParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_SHOW_WHEN_LOCKED;
		mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		mLayoutParams.x = 100;
		mLayoutParams.y = 300;
		
		mWindowManager = getWindowManager();
		mWindowManager.addView(mFloatingButton, mLayoutParams);
		
		mFloatingButton.setOnTouchListener(this);
		mFloatingButton.setOnClickListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if(v == mFloatingButton)
		{
			float rawX = event.getRawX();
			float rawY = event.getRawY();
			switch (event.getAction())
			{
				case MotionEvent.ACTION_MOVE:
					mLayoutParams.x = (int) rawX;
					mLayoutParams.y = (int) rawY;
					mWindowManager.updateViewLayout(mFloatingButton, mLayoutParams);
					
					break;
	
				default:
					break;
			}
		}
		return false;
	}
	
	class CrashHandler implements UncaughtExceptionHandler
	{

		@Override
		public void uncaughtException(Thread t, Throwable e)
		{
			
		}
		
	}

	@Override
	public void onClick(View v)
	{
		boolean isF = mViewStub.isInLayout();
		Log.i("MainActivity", "" + isF);
		if(isF)
		{
			View inflate = mViewStub.inflate();
			Log.i("MainActivity", "" + inflate);
		}
	}
}
