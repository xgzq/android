package com.xgzq.android.interfaces;

import android.content.Context;
import android.util.AttributeSet;

public interface ICommonView
{

	void initView(Context ctx,AttributeSet attrs);
	
	void initParam();
	
	void initOperation();
}
