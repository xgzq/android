package com.xgzq.android.miguplayer.tools;

import android.content.Context;

public class ScreenUtil
{
	public static int getScreenWidth(Context ctx)
	{
		return ctx != null ? ctx.getResources().getDisplayMetrics().widthPixels : 0;
	}
	
	public static int getScreenHeight(Context ctx)
	{
		return ctx != null ? ctx.getResources().getDisplayMetrics().heightPixels : 0;
	}
	
	public static int getStatusBarHeight(Context ctx)
	{
		if(ctx == null) return 0;
		int resId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if(resId > 0)
		{
			return ctx.getResources().getDimensionPixelSize(resId);
		}
		return 0;
	}
	
	public static float getDensity(Context ctx)
	{
		return ctx != null ? ctx.getResources().getDisplayMetrics().density : 1f;
	}
}
