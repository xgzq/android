package com.xgzq.android.sudoku.view;

import com.xgzq.android.sudoku.R;
import com.xgzq.android.sudoku.interfaces.ICommonView;
import com.xgzq.android.sudoku.interfaces.OnNumberItemClickListener;
import com.xgzq.android.sudoku.tools.ScreenUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SudokuNumberPanel extends LinearLayout implements ICommonView
{
	private final String TAG = "Sudoku_SukokuNumberPanel";

	public static final int TYPE_NUMBER = 1;

	public static final int TYPE_CLEAR = 2;

	public static final int TYPE_CLEAR_ALL = 3;

	public static final int TYPE_NEW_GAME = 4;

	private LinearLayout mUpChild;

	private LinearLayout mDownChild;

	private LayoutParams mCommomLp;

	private LayoutParams mUpChildLp;

	private LayoutParams mDownChildLp;

	private final String[] fUpChildTexts = new String[] { "1", "2", "3", "4", "5", "6" };

	private final String[] fDownChildTexts = new String[] { "7", "8", "9", "删除", "重来", "新游戏" };

	private OnNumberItemClickListener mOnClickListener;

	public SudokuNumberPanel(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initView(context, attrs);
	}

	public SudokuNumberPanel(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context, attrs);
	}

	public SudokuNumberPanel(Context context)
	{
		super(context);
		initView(context, null);
	}

	@Override
	public void initView(Context ctx, AttributeSet attrs)
	{
		initParam();
		if (attrs != null)
		{

		}
		initOperation();
	}

	@Override
	public void initParam()
	{
		setOrientation(VERTICAL);
		setGravity(Gravity.CENTER);
		mUpChild = new LinearLayout(getContext());
		mDownChild = new LinearLayout(getContext());
		mCommomLp = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		int margin = (int) getContext().getResources().getDimension(R.dimen.sudoku_btn_group_margin);
		mCommomLp.setMargins(margin, margin, margin, margin);

		mUpChildLp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		int itemMargin = (int) getContext().getResources().getDimension(R.dimen.sudoku_item_btn_margin);
		mUpChildLp.setMargins(itemMargin, itemMargin, itemMargin, itemMargin);

		mDownChildLp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		mDownChildLp.setMargins(itemMargin, 0, itemMargin, itemMargin);

		mUpChild.setOrientation(HORIZONTAL);
		mUpChild.setLayoutParams(mCommomLp);

		mDownChild.setOrientation(HORIZONTAL);
		mDownChild.setLayoutParams(mCommomLp);
	}

	@Override
	public void initOperation()
	{
		for (int i = 0; i < fUpChildTexts.length; i++)
		{
			mUpChild.addView(getItemButton(i, fUpChildTexts[i]), mUpChildLp);
		}
		for (int i = 0; i < fDownChildTexts.length; i++)
		{
			mDownChild.addView(getItemButton(i, fDownChildTexts[i]), mDownChildLp);
		}
		setPadding(0, 10, 0, 0);
		addView(mUpChild);
		addView(mDownChild);
	}

	@SuppressWarnings("deprecation")
	private Button getItemButton(int index, String text)
	{
		Button btn = new Button(getContext());
		btn.setBackgroundResource(R.drawable.sukodu_item_btn);
		btn.setGravity(Gravity.CENTER);
		btn.setTag(text);
		btn.setOnClickListener(mNumberClickListener);
		btn.setText(text);
		btn.getPaint().setFakeBoldText(true);
		btn.setTextSize(ScreenUtil.getDensity(getContext()) * 12);
		btn.setTextColor(getContext().getResources().getColor(R.color.sudoku_item_btn_text));
		btn.setSingleLine(true);
		return btn;
	}

	private OnClickListener mNumberClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Button btn = (Button) v;
			String value = btn.getText().toString();
			int number = 0;
			int type = TYPE_NUMBER;
			try
			{
				number = Integer.valueOf(value);
			}
			catch (NumberFormatException e)
			{
				number = -1;
				if (fDownChildTexts[fDownChildTexts.length - 3].equals(value))
				{
					type = TYPE_CLEAR;
				}
				else if (fDownChildTexts[fDownChildTexts.length - 2].equals(value))
				{
					type = TYPE_CLEAR_ALL;
				}
				else if (fDownChildTexts[fDownChildTexts.length - 1].equals(value))
				{
					type = TYPE_NEW_GAME;
				}
			}
			if (mOnClickListener != null)
			{
				Log.i(TAG, "[onClick] type is " + type);
				mOnClickListener.onItemClick(number, type);
			}
			else
			{
				Log.e(TAG, "[onClick] mOnClickListener is null");
			}
		}
	};

	public void setOnItemClickListener(OnNumberItemClickListener l)
	{
		if (l != null)
		{
			this.mOnClickListener = l;
		}
		else
		{
			Log.e(TAG, "[setOnItemClickListener] OnNumberItemClickListener must not be null");
		}
	}
}
