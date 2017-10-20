package com.xgzq.apifun.ui.activity;

import com.xgzq.apifun.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ModelActivity extends Activity
{
	private final String TAG = "APIFUN_ModelActivity";
	
//	private TitleView mTitleView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.i(TAG,"[onCreate]");
		setContentView(R.layout.layout_model);
		findViews();
		initPrarm();
		operation();
	}

	private void findViews()
	{
//		mTitleView = (TitleView) findViewById(R.id.layout_model_title_view);
	}

	private void initPrarm()
	{
		if(getIntent() != null)
		{
			getIntent().getBundleExtra("params");
		}
	}

	private void operation()
	{
//		mTitleView.setCenterTitleText("ModelActivity");
//		mTitleView.setLeftTextClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View arg0)
//			{
//				finish();
//			}
//		});
	}
}
