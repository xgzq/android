package com.xgzq.android.sudoku.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
		if (ctx == null)
			return 0;
		int resId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resId > 0)
		{
			return ctx.getResources().getDimensionPixelSize(resId);
		}
		return 0;
	}

	public static float getDensity(Context ctx)
	{
		return ctx != null ? ctx.getResources().getDisplayMetrics().density : 1f;
	}

	public static boolean isSupportImmersionStatuBar()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			return true;
		}
		return false;
	}

	public static void setImmersionStatuBar(Window window)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
		}
		else
		{
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}
}
