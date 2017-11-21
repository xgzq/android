package com.xgzq.android.miguplayer.activity;

import com.xgzq.android.miguplayer.R;
import com.xgzq.android.miguplayer.tools.GUtil;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class AnimActivity extends Activity
{
	private final String TAG = "Migu_AnimActivity";
	
	private Button mBtn;
	private ImageView mTv;
	private ImageView mTv2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anim);
		
		mBtn = (Button) findViewById(R.id.anim_btn_start);
		mTv = (ImageView) findViewById(R.id.anim_tv_tip);
		mTv2 = (ImageView) findViewById(R.id.anim_tv_tip2);
		
		mTv.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				
				mTv2.setTranslationY(mTv2.getTranslationY()-5);
			}
		});
		mBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				final AnimatorSet anims1 = new AnimatorSet();
				final AnimatorSet anims2 = new AnimatorSet();
				
				ObjectAnimator animY = ObjectAnimator.ofFloat(mTv2, "translationY", 0,1000);
				animY.setInterpolator(GUtil.sLinearInterpolator);
				animY.setEvaluator(new GUtil.TransformableFreeFallEvalutor());
				
				ObjectAnimator animX = ObjectAnimator.ofFloat(mTv2, "translationX", 0,500);
				animX.setInterpolator(new LinearInterpolator());
				
				final ValueAnimator widthAnim = ValueAnimator.ofInt(0,100);
				final int w = mTv2.getRight() - mTv2.getLeft();
				Log.i(TAG, " w is " + w);
				Log.i(TAG, " width is " + mTv2.getWidth());
				widthAnim.addUpdateListener(new AnimatorUpdateListener()
				{
					
					@Override
					public void onAnimationUpdate(ValueAnimator animation)
					{
						int width = (Integer) animation.getAnimatedValue();
						if((w + width) == 100)
						{
							Log.i(TAG,"[widthAnim] width is " + (w + width) + ", width is " + width);
//							mTv2.layout(mTv2.getLeft(), mTv2.getTop(),mTv2.getWidth(), mTv2.getBottom());
							mTv2.getLayoutParams().width = 300;
						}
					}
				});
				
				final ValueAnimator angleAnim = ValueAnimator.ofFloat(45,25);
				final GradientDrawable d = (GradientDrawable) mTv2.getBackground();
				angleAnim.addUpdateListener(new AnimatorUpdateListener()
				{
					
					@Override
					public void onAnimationUpdate(ValueAnimator animation)
					{
//						float angle = (Float) animation.getAnimatedValue();
//						Log.i(TAG, "[angleAnim] angle is " + angle);
//						d.setCornerRadius(angle);
					}
				});
				
				anims1.playTogether(animY,animX);
				anims1.setDuration(1000);
				anims1.start();
				
				anims2.playSequentially(widthAnim,angleAnim);
				anims2.setDuration(2000);
				anims2.setStartDelay(1000);
				anims2.start();
			}
		});
		
	}
}
