package com.xgzq.android.sudoku.view;

import com.xgzq.android.sudoku.interfaces.ICommonView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class SudokuItemView extends TextView implements ICommonView
{

	public SudokuItemView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initView(context, attrs);
	}

	public SudokuItemView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context, attrs);
	}

	public SudokuItemView(Context context)
	{
		super(context);
		initView(context, null);
	}

	@Override
	public void initView(Context ctx, AttributeSet attrs)
	{
		setClickable(true);
	}

	@Override
	public void initParam()
	{
	}

	@Override
	public void initOperation()
	{

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	@Override
	public void layout(int l, int t, int r, int b)
	{
		super.layout(l + getPaddingLeft(), t + getPaddingTop(), r - getPaddingRight(), b - getPaddingBottom());
	}
}
