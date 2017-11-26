package com.xgzq.android.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class DrawerView extends FrameLayout
{
	private final String TAG = "DrawerView";
	
	private LinearLayout mMenuLayout;
	
	private LinearLayout mContentLayout;
	
	private LayoutParams mMenuLp;
	
	private LayoutParams mContentLp;
	
	private int mScreenWidth;
	
	private int mScreenHeight;
	
	private int mMenuWidth;
	
	private int mContentWidth;
	
	private boolean showMenu = false;
	
	private boolean isTranslateTogether = false;
	
	private ValueAnimator mShowMenuAnim;
	
	private ValueAnimator mHiddenMenuAnim;
	
	private ValueAnimator mShowContentAnim;
	
	private ValueAnimator mHiddenContentAnim;
	
	private AnimatorUpdateListener mMenuAnimUpdateListener;
	
	private AnimatorUpdateListener mContentAnimUpdateListener;

	public DrawerView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initView(context,attrs);
	}

	public DrawerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context,attrs);
	}

	public DrawerView(Context context)
	{
		super(context);
		initView(context,null);
	}

	@SuppressLint("InlinedApi")
	private void initView(Context context, AttributeSet attrs)
	{
		Log.i(TAG, "[initView]");
		mMenuWidth = (int) (getScreenWidth() * 0.8f);
		mContentWidth = getScreenWidth();
		
		mMenuLayout = new LinearLayout(context);
		mMenuLp = new LayoutParams(mMenuWidth, getScreenHeight(),LinearLayout.VERTICAL);
		mMenuLp.gravity = Gravity.START;
		mMenuLayout.setLayoutParams(mMenuLp);
		mMenuLayout.setBackgroundColor(Color.RED);
		
		mContentLayout = new LinearLayout(context);
		mContentLp = new LayoutParams(mContentWidth, mScreenHeight, LinearLayout.VERTICAL);
		mContentLayout.setLayoutParams(mContentLp);
		mContentLayout.setBackgroundColor(Color.BLUE);
		
		initParams();
		
		if(attrs != null)
		{
			Log.i(TAG, "[initView] attrs is not null");
			
		}
		
		addView(mContentLayout);
		addView(mMenuLayout);
		post(new Runnable()
		{
			@Override
			public void run()
			{
				mMenuLayout.setTranslationX(-mMenuWidth*1.0f);
			}
		});
	}
	
	private void initParams()
	{
		mMenuAnimUpdateListener = new AnimatorUpdateListener()
		{
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				if(mMenuLayout != null)
				{
					mMenuLayout.setTranslationX((Float) animation.getAnimatedValue());
				}
			}
		};
		mContentAnimUpdateListener = new AnimatorUpdateListener()
		{
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation)
			{
				if(mContentLayout != null)
				{
					mContentLayout.setTranslationX((Float) animation.getAnimatedValue());
					Log.i(TAG, "getAnimatedFraction : " + animation.getAnimatedFraction());
					mContentLayout.setAlpha((1 - animation.getAnimatedFraction()) <= 0.2f ? 0.2f : 1 - animation.getAnimatedFraction());
				}
			}
		};
		
		mShowMenuAnim = ValueAnimator.ofFloat(-mMenuWidth,0);
		mShowMenuAnim.setDuration(600);
		mShowMenuAnim.setInterpolator(new DecelerateInterpolator());
		mShowMenuAnim.addUpdateListener(mMenuAnimUpdateListener);
		
		mHiddenMenuAnim = ValueAnimator.ofFloat(0,-mMenuWidth);
		mHiddenMenuAnim.setDuration(600);
		mHiddenMenuAnim.setInterpolator(new DecelerateInterpolator());
		mHiddenMenuAnim.addUpdateListener(mMenuAnimUpdateListener);
		
		mShowContentAnim = ValueAnimator.ofFloat(mMenuWidth,0);
		mShowContentAnim.setDuration(600);
		mShowContentAnim.setInterpolator(new DecelerateInterpolator());
		mShowContentAnim.addUpdateListener(mContentAnimUpdateListener);
		
		mHiddenContentAnim = ValueAnimator.ofFloat(0,mMenuWidth);
		mHiddenContentAnim.setDuration(600);
		mHiddenContentAnim.setInterpolator(new DecelerateInterpolator());
		mHiddenContentAnim.addUpdateListener(mContentAnimUpdateListener);
	}

	private int getScreenHeight()
	{
		if(mScreenHeight == 0)
		{
			mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
		}
		return mScreenHeight;
	}
	
	private int getScreenWidth()
	{
		if(mScreenWidth == 0)
		{
			mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
		}
		return mScreenWidth;
	}
	
	/**
	 * @param child
	 * 添加的子View。只能添加一个View
	 */
	public void setMenuView(View child)
	{
		if(mMenuLayout != null)
		{
			if(mMenuLayout.getChildCount() <= 1)
			{
				Log.i(TAG, "[setMenuView] add view to menu");
				mMenuLayout.addView(child);
			}
			else
			{
				Log.e(TAG, "[setMenuView] menu view can only add one child");
			}
		}
	}
	
	/**
	 * @param child
	 * 添加的子View。只能添加一个View
	 */
	public void setContentView(View child)
	{
		if(mContentLayout != null)
		{
			if(mContentLayout.getChildCount() <= 1)
			{
				Log.i(TAG, "[setContentView] content view to menu");
				mContentLayout.addView(child);
			}
			else
			{
				Log.e(TAG, "[setContentView] content view can only add one child");
			}
		}
	}
	
	public void showMenu()
	{
		if(isTranslateTogether)
		{
			mShowMenuAnim.start();
			mHiddenContentAnim.start();
		}
		else
		{
			mShowMenuAnim.start();
		}
		showMenu = true;
	}
	
	public void hiddenMenu()
	{
		if(isTranslateTogether)
		{
			mHiddenMenuAnim.start();
			mShowContentAnim.start();
		}
		else
		{
			mHiddenMenuAnim.start();
		}
		showMenu = false;
	}
	
	public boolean shownMenu()
	{
		return showMenu;
	}
	
	public void setTranslateTogether(boolean b)
	{
		this.isTranslateTogether = b;
	}
	
	public boolean isTranslateTogether()
	{
		return isTranslateTogether;
	}
}
