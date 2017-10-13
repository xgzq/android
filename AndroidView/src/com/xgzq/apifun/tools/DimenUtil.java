package com.xgzq.apifun.tools;

import android.content.Context;

public class DimenUtil
{

	public static float dp2Px(Context context, int dip)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	public static float px2Dp(Context context, int px)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}

	public static float sp2Px(Context context, float spValue)
	{
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (spValue * fontScale + 0.5f);
	}

	public static float px2Sp(Context context, float pxValue)
	{
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (pxValue / fontScale + 0.5f);
	}
	
	public static float dp2Sp(Context context, float dpValue)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return dpValue * scale / fontScale;
	}
	
	public static float sp2Dp(Context context, float spValue)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return spValue * fontScale / scale;
	}

}
