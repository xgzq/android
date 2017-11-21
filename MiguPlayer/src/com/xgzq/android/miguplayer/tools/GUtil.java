package com.xgzq.android.miguplayer.tools;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.animation.LinearInterpolator;

/**
 * @author Administrator 自由落体工具类
 */
@SuppressLint("NewApi")
public class GUtil
{
	private static final String TAG = "Migu_GUtil";

	public static final float G = 9.8f * 100;

	public static final LinearInterpolator sLinearInterpolator = new LinearInterpolator();

	/**
	 * @param anim
	 *            StandardFreeFallEvalutor 估值器的时间是自由落体运动的时间:anim.getDuration()
	 */
	public static void doStandardFreeFall(ObjectAnimator anim)
	{
		anim.setInterpolator(sLinearInterpolator);
		anim.setEvaluator(new StandardFreeFallEvalutor(anim.getDuration()));
		anim.start();
	}

	public static void doFreeFallWithDurationAndDistance(ObjectAnimator anim)
	{
		anim.setInterpolator(sLinearInterpolator);
		anim.setEvaluator(new TransformableFreeFallEvalutor());
//		anim.start();
	}

	/**
	 * @author Administrator 时间流逝是均匀的，不均匀的是速度。所以默认使用线性插值器LinearInterpolator
	 */
	static class StandardFreeFallEvalutor implements TypeEvaluator<Float>
	{
		private long mDuration;

		public StandardFreeFallEvalutor(long duration)
		{
			this.mDuration = duration;
		}

		@Override
		public Float evaluate(float fraction, Float startValue, Float endValue)
		{
			float time = this.mDuration / 1000f * fraction;
			float result = startValue.floatValue() + G / 2f * time * time;
			Log.i(TAG, "[StandardFreeFallEvalutor][evaluate] result is " + result);
			return result;
		}
	}

	private static final float[] sDistances = { 1.0f, 0.5f, 0.25f, 0.125f, 0.1f, 0.06f, 0.04f, 0.02f, 0f };

	public static class TransformableFreeFallEvalutor implements TypeEvaluator<Float>
	{
		private boolean isPositive = true;
		private float mPercentTimes[] = new float[sDistances.length * 2 - 1];

		/**
		 * 计算每个阶段所花的平均时间
		 */
		public TransformableFreeFallEvalutor()
		{
			float allTime = 0f;
			for (int i = 0; i < sDistances.length; i++)
			{
				allTime += i == 0 ? sDistances[i] : sDistances[i] * 2;
			}
//			Log.i(TAG, "[TransformableFreeFallEvalutor] allTime is " + allTime);
			for (int i = 0; i < mPercentTimes.length; i++)
			{
				mPercentTimes[i] = i == 0 ? sDistances[i] / allTime
						: mPercentTimes[i - 1] + sDistances[(i + 1) / 2] / allTime;
//				Log.i(TAG, "[TransformableFreeFallEvalutor] mPercentTimes[" + i + "] = " + mPercentTimes[i]);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.animation.TypeEvaluator#evaluate(float, java.lang.Object,
		 * java.lang.Object)
		 * 
		 * 1 / 2 * g * t^2 = s ft = f(percent) * t d = 1 / 2 * g * (ft)^2 = 1 / 2 * g *
		 * (f * t)^2 = 1 / 2 * g * f^2 * t^2 d = f^2 * s;
		 */
		@Override
		public Float evaluate(float fraction, Float startValue, Float endValue)
		{
			float result = 0f;
			int currentIndex = 0;
			for (int i = 0; i < mPercentTimes.length; i++)
			{
				if (fraction <= mPercentTimes[i])
				{
					// 获取当前运动方向
					isPositive = i % 2 == 0;
					currentIndex = i;
					break;
				}
			}
//			Log.i(TAG, "[evaluate] isPositive is " + isPositive + " , currentIndex is " + currentIndex);
			float percent = currentIndex == 0 ? fraction / mPercentTimes[currentIndex]
					: (fraction - mPercentTimes[currentIndex - 1])
							/ (mPercentTimes[currentIndex] - mPercentTimes[currentIndex - 1]);
//			Log.i(TAG, "[evaluate] fraction is " + fraction + " , percent is " + percent);
			float allDistance = endValue.floatValue() - startValue.floatValue();
			float distance1 = 0;
			float distance2 = 0;
			if (isPositive)
			{
				// d = f^2 * cs = f^2 * s * percent
				// 每个正阶段根据时间计算移动的距离
				distance1 = percent * percent * allDistance * sDistances[(currentIndex + 1) / 2];
				// 加上偏移距离：每个阶段的起始位置不一样导致的偏移距离：总移动距离与当前阶段的总距离的差
				distance2 = (1 - sDistances[(currentIndex + 1) / 2]) * allDistance;
				result = distance1 + distance2;
//				Log.i(TAG, "[evaluate] result is " + result + ", distance1 is " + distance1 + ", distance2 is "
//						+ distance2);
				return result;
			}
			else
			{
				// 上升过程移动的距离：速度越来越慢。仅仅表示移动的距离（700 -> 0）
				distance1 = (1 - percent) * (1 - percent) * allDistance * sDistances[(currentIndex + 1) / 2];
				// 偏移量：
				distance2 = allDistance * (1 - sDistances[(currentIndex + 1) / 2]);
				result = distance1 + distance2;
//				Log.i(TAG, "[evaluate] result is " + result + ", distance1 is " + distance1 + ", distance2 is "
//						+ distance2);
				return result;
			}
		}

	}
}
