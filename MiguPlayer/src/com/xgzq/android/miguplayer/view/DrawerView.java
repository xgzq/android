package com.xgzq.android.miguplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class DrawerView extends FrameLayout
{
	private final String TAG = "MiguPlayer_DrawerView";
	
//	private Context mContext;
//	
//	private LinearLayout mMenuView;
//	
//	private LinearLayout mContentView;

	public DrawerView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}

	public DrawerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context,attrs);
	}

	public DrawerView(Context context)
	{
		super(context);
	}

	private void initView(Context context, AttributeSet attrs)
	{
		Log.i(TAG, "[initView]");
		if(context != null)
		{
			Log.i(TAG, "[initView] context is not null");
//			this.mContext = context;
		}
		if(attrs != null)
		{
//			TypedArray typedArray = mContext.obtainStyledAttributes(attrs, null);
			
		}
		
	}
	
}
