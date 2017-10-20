package com.xgzq.apifun.ui.view;

import com.xgzq.apifun.R;
import com.xgzq.apifun.tools.DimenUtil;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class DrawerView extends HorizontalScrollView
{
	private final String TAG = "APIFUN_DrawerView";

	public static final float DEFAULT_LEFT_MENU_WIDTH_PERCENT = 0.75f;

	private LinearLayout mRootLinearLayout;

	private LinearLayout mLeftMenuLinearLayout;

	private LinearLayout mRightContentLinearLayout;

	private int mLeftMenuWidth;

	private int mRightContentWidth;

	private boolean isShowContent = true;

	public DrawerView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(attrs);
	}

	public DrawerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(attrs);
	}

	public DrawerView(Context context)
	{
		super(context);
		initView(null);
	}

	private void initView(AttributeSet attrs)
	{
		findViews();
		if (mLeftMenuLinearLayout != null)
		{
			Log.i(TAG, "[initView] mLeftMenuLinearLayout is not null");
			mLeftMenuLinearLayout.setBackgroundColor(Color.YELLOW);
			mLeftMenuWidth = (int) (DimenUtil.getScreenWidth(getContext()) * DEFAULT_LEFT_MENU_WIDTH_PERCENT);
			mLeftMenuLinearLayout.getLayoutParams().width = mLeftMenuWidth;
			Log.i(TAG, "[initView] mLeftMenuWidth is " + mLeftMenuWidth);
		}
		if (mRightContentLinearLayout != null)
		{
			Log.i(TAG, "[initView] mRightContentLinearLayout is not null");
			mRightContentLinearLayout.setBackgroundColor(Color.BLUE);
			mRightContentWidth = DimenUtil.getScreenWidth(getContext());
			mRightContentLinearLayout.getLayoutParams().width = mRightContentWidth;
			Log.i(TAG, "[initView] mRightContentWidth is " + mRightContentWidth);
		}
		if (attrs != null)
		{
			Log.i(TAG, "[initView] attrs is not null");
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);
		scrollTo(mLeftMenuWidth, 0);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
	}

	private float startX = 0;
	private float endX = 0;

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN)
		{
			startX = ev.getRawX();
		}
		else if (action == MotionEvent.ACTION_UP)
		{
			endX = ev.getRawX();
			Log.i(TAG, "[onTouchEvent] startX = " + startX);
			Log.i(TAG, "[onTouchEvent] endX = " + endX);
			if (isShowContent)
			{
				if (endX - startX < mLeftMenuWidth * 0.3f && endX - startX >= 10)
				{
					// ª÷∏¥
					moveToContent();
					// scrollTo(mLeftMenuWidth, 0);
					// smoothScrollTo(mLeftMenuWidth, 0);
					isShowContent = true;
					Log.i(TAG, "[onTouchEvent] 1111111111");
				}
				else if(endX - startX >= mLeftMenuWidth * 0.3f)
				{
					// œ‘ æ≤Àµ•
					moveToMenu();
					// scrollTo(0, 0);
					// smoothScrollTo(0, 0);
					isShowContent = false;
					Log.i(TAG, "[onTouchEvent] 2222222222");
				}
			}
			else
			{
				if (startX - endX > mRightContentWidth * 0.3f)
				{
					// ª÷∏¥
					moveToContent();
					// scrollTo(mLeftMenuWidth, 0);
					// smoothScrollTo(mLeftMenuWidth, 0);
					Log.i(TAG, "[onTouchEvent] 3333333333");
					isShowContent = true;
				}
				else if(startX - endX > mRightContentWidth * 0.3f && startX - endX >= 10)
				{
					// œ‘ æ≤Àµ•
					moveToMenu();
					// scrollTo(0, 0);
					// smoothScrollTo(0, 0);
					isShowContent = false;
					Log.i(TAG, "[onTouchEvent] 444444444444");
				}
			}
		}
		return super.onTouchEvent(ev);
	}

	private void moveToMenu()
	{
		
		int[] location = new int[4];
		mLeftMenuLinearLayout.getLocationOnScreen(location);
		Log.i(TAG, "[moveToMenu] left1 is " + location[0]);
		ObjectAnimator anim = ObjectAnimator.ofFloat(mLeftMenuLinearLayout, "translationX",location[0], 0);
		anim.setDuration(800);
		anim.start();
		
		mRightContentLinearLayout.getLocationOnScreen(location);
		Log.i(TAG, "[moveToMenu] left2 is " + location[0]);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(mRightContentLinearLayout, "translationX",location[0], mLeftMenuWidth);
		anim2.setDuration(800);
		anim2.start();
	}

	private void moveToContent()
	{
		int[] location = new int[4];
		mLeftMenuLinearLayout.getLocationOnScreen(location);
		Log.i(TAG, "[moveToContent] left1 is " + location[0]);
		ObjectAnimator anim = ObjectAnimator.ofFloat(mLeftMenuLinearLayout, "translationX",location[0], -mLeftMenuWidth);
		anim.setDuration(800);
		anim.start();
		
		mRightContentLinearLayout.getLocationOnScreen(location);
		Log.i(TAG, "[moveToContent] left2 is " + location[0]);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(mRightContentLinearLayout, "translationX", location[0], 0);
		anim2.setDuration(800);
		anim2.start();
	}

	private void findViews()
	{
		View root = LayoutInflater.from(getContext()).inflate(R.layout.view_drawer, this);
		Log.i(TAG, "[findViews] root is " + root.getClass().getSimpleName());
		if (root != null && root instanceof LinearLayout)
		{
			mRootLinearLayout = (LinearLayout) root;
		}
		Log.i(TAG, "[findViews] mRootLinearLayout is " + mRootLinearLayout);
		if (root != null)
		{
			View view = root.findViewById(R.id.view_drawer_ll_root);
			if (view != null && view instanceof LinearLayout)
			{
				mRootLinearLayout = (LinearLayout) view;
				Log.i(TAG, "[findViews] mRootLinearLayout is not null");
			}

			if (mRootLinearLayout != null)
			{
				View leftView = mRootLinearLayout.findViewById(R.id.view_drawer_ll_left_menu);
				if (leftView != null && leftView instanceof LinearLayout)
				{
					mLeftMenuLinearLayout = (LinearLayout) leftView;
					Log.i(TAG, "[findViews] mLeftMenuLinearLayout is not null");
				}

				View rightView = mRootLinearLayout.findViewById(R.id.view_drawer_ll_right_content);
				if (rightView != null && rightView instanceof LinearLayout)
				{
					mRightContentLinearLayout = (LinearLayout) rightView;
					Log.i(TAG, "[findViews] mRightContentLinearLayout is not null");
				}
			}
		}
		setHorizontalScrollBarEnabled(false);
		setSmoothScrollingEnabled(true);
	}
}
