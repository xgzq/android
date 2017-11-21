package com.xgzq.android.miguplayer.view;

import java.util.ArrayList;
import java.util.List;

import com.xgzq.android.miguplayer.tools.ScreenUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SudokuView extends View
{
	private final String TAG = "Migu_SudokuView";

	public static final int DEFAULT_MARGIN = 4;

	private float mStartX;

	private float mStartY;

	private int mAllCellNumber;

	private int mCellNumber;

	private float mLineWidth;

	private float mCellWidth;

	private float mThinLineWidth;

	private int mThinLineNumber;

	private float mThickLineWidth;

	private int mThickLineNumber;

	private int mBackgroundColor;

	private int mThinLineColor;

	private int mThickLineColor;

	private float mViewWidth;

	private Paint mBackgroundPaint;

	private Paint mThickPaint;

	private Paint mThinPaint;

	private Point mLastPoint;

	private List<Point> mOpenedPointList;

	private Canvas mCanvas;

	public SudokuView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initView(context, attrs);
	}

	public SudokuView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context, attrs);
	}

	public SudokuView(Context context)
	{
		super(context);
		initView(context, null);
	}

	private void initView(Context context, AttributeSet attrs)
	{
		initParams();
		if (attrs != null)
		{

		}
	}

	private void initParams()
	{
		mCellNumber = 9;
		mThickLineNumber = mCellNumber / 3 + 1;
		mThickLineWidth = ScreenUtil.getDensity(getContext()) * 2f;
		mThinLineNumber = mCellNumber + 1 - mThickLineNumber;
		mThinLineWidth = ScreenUtil.getDensity(getContext()) * 1.5f;
		mStartX = DEFAULT_MARGIN * ScreenUtil.getDensity(getContext());
		mStartY = mStartX;

		mAllCellNumber = mCellNumber * mCellNumber;

		mBackgroundColor = Color.parseColor("#FFDB69");
		mThickLineColor = Color.BLACK;
		mThinLineColor = Color.GRAY;

		mBackgroundPaint = new Paint();
		mBackgroundPaint.setAntiAlias(true);
		mBackgroundPaint.setColor(mBackgroundColor);

		mThickPaint = new Paint();
		mThickPaint.setAntiAlias(true);
		mThickPaint.setColor(mThickLineColor);
		mThickPaint.setStrokeWidth(mThickLineWidth);

		mThinPaint = new Paint();
		mThinPaint.setAntiAlias(true);
		mThinPaint.setColor(mThinLineColor);
		mThinPaint.setStrokeWidth(mThinLineWidth);

		mOpenedPointList = new ArrayList<Point>();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		this.mCanvas = canvas;
		mLineWidth = getWidth() * 1f - mStartX * 2;
		mCellWidth = mLineWidth / mCellNumber;

		canvas.drawRect(mStartX, mStartY, mStartX + mLineWidth, mStartY + mLineWidth, mBackgroundPaint);
		drawSudoku(canvas);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			float rawX = event.getX();
			float rawY = event.getY();
			mLastPoint = getPoint(rawX, rawY);
			Log.i(TAG, "[dispatchTouchEvent] point is " + mLastPoint);
			break;

		case MotionEvent.ACTION_UP:
			drawPoint();
			break;

		default:
			break;
		}
		return true;
	}

	private void drawPoint()
	{
		if (mLastPoint != null)
		{
			Log.i(TAG, "[drawPoint] x is " + mLastPoint.x + ", y is " + mLastPoint.y);
			float x = mStartX + mLastPoint.x * mCellWidth + mThickLineWidth - 0.5f;
			float y = mStartY + mLastPoint.y * mCellWidth + mThickLineWidth - 0.5f;
			float e_x = x + mCellWidth - mThinLineWidth - 2.5f;
			float e_y = y + mCellWidth - mThinLineWidth - 2.5f;
			mCanvas.clipRect(x, y, e_x, e_y, Region.Op.DIFFERENCE);
			mOpenedPointList.add(mLastPoint);
			mLastPoint = null;
			invalidate();
		}
	}

	public Point getPoint(float x, float y)
	{
		int indexX = (int) ((x - mStartX) / mCellWidth);
		int indexY = (int) ((y - mStartY) / mCellWidth);
		return new Point(indexX, indexY);
	}

	private void drawSudoku(Canvas canvas)
	{
		for (int i = 0; i <= mCellNumber; i++)
		{
			// 粗线
			if (i % Math.sqrt(mCellNumber) == 0 || i == mCellNumber)
			{
				// 水平
				canvas.drawLine(mStartX, mStartY + i * mCellWidth, mStartX + mLineWidth, mStartY + i * mCellWidth,
						mThickPaint);
				// 竖直
				canvas.drawLine(mStartX + i * mCellWidth, mStartY, mStartX + i * mCellWidth, mStartY + mLineWidth,
						mThickPaint);
			}
			// 细线
			else
			{
				// 水平
				canvas.drawLine(mStartX, mStartY + i * mCellWidth, mStartX + mLineWidth, mStartY + i * mCellWidth,
						mThinPaint);
				// 竖直
				canvas.drawLine(mStartX + i * mCellWidth, mStartY, mStartX + i * mCellWidth, mStartY + mLineWidth,
						mThinPaint);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
		mViewWidth = getWidth();
	}
}
