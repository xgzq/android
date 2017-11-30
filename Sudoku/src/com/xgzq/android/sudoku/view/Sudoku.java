package com.xgzq.android.sudoku.view;

import com.xgzq.android.sudoku.R;
import com.xgzq.android.sudoku.interfaces.IView;
import com.xgzq.android.sudoku.interfaces.OnCompleteListener;
import com.xgzq.android.sudoku.interfaces.OnSukoduItemClickListener;
import com.xgzq.android.sudoku.tools.ArrayUtil;
import com.xgzq.android.sudoku.tools.ScreenUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;

public class Sudoku extends GridLayout implements IView
{
	private final String TAG = "Sudoku_Sudoku";
	public static final int LENGTH_DEFAULT_ARRAY = sDefaultData.length;
	public static final int PADDING_DEFAULT_SUKODU_INNER = 0;
	public static final int PADDING_DEFAULT_DIVIDING = 3;
	public static final int PADDING_DEFAULT_ITEM_VIEW_THICK = 5;
	public static final int SIZE_DEFAULT_TEXT = 16;

	/**
	 * 背景颜色：边框颜色
	 */
	public static final int COLOR_DEFAULT_BACKGROUND = R.drawable.sudoku_view_bg_shape;

	/**
	 * 选择的单元格的背景颜色
	 */
	public static final int COLOR_DEFAULT_TV_SELECTED = Color.parseColor("#ff8050");

	/**
	 * 文字颜色
	 */
	public static final int COLOR_DEFAULT_TEXT = Color.parseColor("#75574f");

	/**
	 * 存放原始数据，用来在clearAll时恢复初始数据
	 */
	private int[][] mOriginData;

	/**
	 * 存放已显示的数据，未显示的部分存放0
	 */
	private int[][] mShownData;

	/**
	 * 存放初始显示情况，用来在clearAll时恢复初始数据
	 */
	private boolean[][] mOriginHints;

	/**
	 * 存放当前显示情况，true为显示，false为未显示
	 */
	private boolean[][] mShownHints;

	/**
	 * 存放所有ItemView
	 */
	private SudokuItemView[][] mChildViews;

	/**
	 * 当前点击的ItemView，初始化后未点击Item时为null
	 */
	private SudokuItemView mCurrentItemView;

	/**
	 * 当前点击的ItemView的数据
	 */
	private int mCurrentItemData = 0;

	/**
	 * 当前点击的ItemView的坐标
	 */
	private Point mCurrentItemPoint;

	private int mRemainingEmptyCellCount = 0;

	/**
	 * ItemView点击事件监听器
	 */
	private OnSukoduItemClickListener mOnItemClickListener;

	private OnCompleteListener mOnCompleteListener;

	public Sudoku(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initView(context, attrs);
	}

	public Sudoku(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context, attrs);
	}

	public Sudoku(Context context)
	{
		super(context);
		initView(context, null);
	}

	private void initView(Context context, AttributeSet attrs)
	{
		initParam();
		if (attrs != null)
		{

		}
		initOperation();
	}

	private void initParam()
	{
		this.mOriginData = new int[LENGTH_DEFAULT_ARRAY][LENGTH_DEFAULT_ARRAY];
		this.mShownData = new int[LENGTH_DEFAULT_ARRAY][LENGTH_DEFAULT_ARRAY];
		this.mOriginHints = new boolean[LENGTH_DEFAULT_ARRAY][LENGTH_DEFAULT_ARRAY];
		this.mShownHints = new boolean[LENGTH_DEFAULT_ARRAY][LENGTH_DEFAULT_ARRAY];
		this.mChildViews = new SudokuItemView[LENGTH_DEFAULT_ARRAY][LENGTH_DEFAULT_ARRAY];

		this.mCurrentItemPoint = new Point(-1, -1);
		this.mRemainingEmptyCellCount = LENGTH_DEFAULT_ARRAY * LENGTH_DEFAULT_ARRAY;

		setRowCount(LENGTH_DEFAULT_ARRAY);
		setColumnCount(LENGTH_DEFAULT_ARRAY);
		// setBackgroundColor(COLOR_DEFAULT_BACKGROUND);
		setBackgroundResource(COLOR_DEFAULT_BACKGROUND);
		setPadding(PADDING_DEFAULT_SUKODU_INNER, PADDING_DEFAULT_SUKODU_INNER, PADDING_DEFAULT_SUKODU_INNER,
				PADDING_DEFAULT_SUKODU_INNER);
	}

	private void initOperation()
	{
		post(new Runnable()
		{
			@Override
			public void run()
			{
				initItemView();
			}
		});
	}

	@Override
	public int[][] getAllData()
	{
		if (mOriginData == null)
		{
			this.mOriginData = new int[9][9];
		}
		return mOriginData;
	}

	@Override
	public int[][] getShownData()
	{
		return mShownData;
	}

	@Override
	public int[] getRowData(int row)
	{
		if (mShownData != null && 0 <= row && row < mShownData.length)
		{
			return mShownData[row];
		}
		return sDefaultData;
	}

	@Override
	public int[] getColumnData(int column)
	{
		if (column < 0 || column >= 9)
		{
			return sDefaultData;
		}
		if (mShownData != null)
		{
			return ArrayUtil.getColumn(mShownData, column);
		}
		return sDefaultData;
	}

	@Override
	public int[] getDiagonalData()
	{
		if (mShownData != null)
		{
			return ArrayUtil.getDiagonalData(mShownData);
		}
		return sDefaultData;
	}

	@Override
	public int[] getBackDiagonalData()
	{
		if (mShownData != null)
		{
			return ArrayUtil.getBackDiagonalData(mShownData);
		}
		return sDefaultData;
	}

	@Override
	public int[][] getBlockData(Point point)
	{
		if (isValidPoint(point) && mShownData != null)
		{
			int[][] result = new int[3][3];
			int x = point.x / 3;
			int y = point.y / 3;
			for (int i = 0; i < result.length; i++)
			{
				for (int j = 0; j < result[i].length; j++)
				{
					result[i][j] = mShownData[3 * x + i][3 * y + j];
				}
			}
			return result;
		}
		return null;
	}

	@Override
	public int getPointData(Point point)
	{
		if (isValidPoint(point) && mShownData != null)
		{
			return mShownData[point.x][point.y];
		}
		return 0;
	}

	@Override
	public void setAllData(int[][] data)
	{
		if (data != null && data.length == sDefaultData.length && data[0] != null
				&& data[0].length == sDefaultData.length)
		{
			mOriginData = data;
		}
	}

	@Override
	public void clearCellData(Point point)
	{
		if (isValidPoint(point))
		{
			if (mChildViews != null)
			{
				mShownData[point.x][point.y] = 0;
				mShownHints[point.x][point.y] = false;
				mChildViews[point.x][point.y].setText("");
				mRemainingEmptyCellCount++;
			}
			else
			{
				Log.e(TAG, "[clearCellData] mChildViews is null");
			}
		}
		else
		{
			Log.e(TAG, "[clearCellData] point is invalid");
		}
	}

	@Override
	public SudokuItemView getItemView(Point point)
	{
		if (isValidPoint(point) && mChildViews != null)
		{
			return mChildViews[point.x][point.y];
		}
		return null;
	}

	@Override
	public SudokuItemView getCurrentView()
	{
		return mCurrentItemView;
	}

	@Override
	public void setCellData(Point point, int data)
	{
		if (isValidPoint(point) && 1 <= data && data <= 9)
		{
			if (mChildViews != null)
			{
				if (mShownData[point.x][point.y] == 0 && !mShownHints[point.x][point.y])
				{
					mRemainingEmptyCellCount--;
				}
				mShownData[point.x][point.y] = data;
				mShownHints[point.x][point.y] = true;
				mChildViews[point.x][point.y].setText(String.valueOf(data));

				if (mRemainingEmptyCellCount <= 0)
				{
					if (mOnCompleteListener != null)
					{
						mOnCompleteListener.OnComplete(isComplete());
					}
				}
			}
		}
		else
		{
			Log.e(TAG, "[setCellData] param is invalid");
		}
	}

	@Override
	public int getCurrentData()
	{
		return mCurrentItemData;
	}

	@Override
	public boolean isContainsInRow(int data, int row)
	{
		return ArrayUtil.contains(getRowData(row), data);
	}

	@Override
	public boolean isContainsInColumn(int data, int column)
	{
		return ArrayUtil.contains(getColumnData(column), data);
	}

	@Override
	public boolean isContainsInBlock(int data, Point point)
	{
		return ArrayUtil.contains(getBlockData(point), data);
	}

	@Override
	public boolean isContainsInDiagonal(int data)
	{
		return ArrayUtil.contains(getDiagonalData(), data);
	}

	@Override
	public boolean isContainsInBackDiagonal(int data)
	{
		return ArrayUtil.contains(getBackDiagonalData(), data);
	}

	@Override
	public void setAllHintable(boolean[][] hints)
	{
		if (hints != null && hints.length == LENGTH_DEFAULT_ARRAY && hints[0] != null
				&& hints[0].length == LENGTH_DEFAULT_ARRAY)
		{
			mOriginHints = hints;
		}
	}

	/*
	 * 
	 * @see com.xgzq.android.miguplayer.interfaces.sudoku.IView#clearAllData()
	 * 删除所有输入或者理解为重新加载
	 */
	@Override
	public void clearAllData()
	{
		mRemainingEmptyCellCount = LENGTH_DEFAULT_ARRAY * LENGTH_DEFAULT_ARRAY;
		if (mChildViews != null && mOriginData != null && mOriginHints != null)
		{
			for (int i = 0; i < mChildViews.length; i++)
			{
				for (int j = 0; j < mChildViews[i].length; j++)
				{
					if (mOriginHints[i][j])
					{
						mRemainingEmptyCellCount--;
						mShownData[i][j] = mOriginData[i][j];
						mShownHints[i][j] = true;
						mChildViews[i][j].setText(String.valueOf(mOriginData[i][j]));
						mChildViews[i][j].setBackgroundResource(R.drawable.sukodu_item_siv_shown);
					}
					else
					{
						mShownData[i][j] = 0;
						mShownHints[i][j] = false;
						mChildViews[i][j].setText("");
						mChildViews[i][j].setBackgroundResource(R.drawable.sukodu_item_siv_gone);
					}
				}
			}
		}
		// 重新初始化
		mCurrentItemData = 0;
		mCurrentItemPoint.x = -1;
		mCurrentItemPoint.y = -1;
		mCurrentItemView = null;
	}

	@Override
	public Point getCurrentPoint()
	{
		return mCurrentItemPoint;
	}

	@Override
	public void setCurrentCellData(int data)
	{
		if (mCurrentItemView != null)
		{
			if (1 <= data && data <= 9)
			{
				if (mCurrentItemData == 0 && !mShownHints[mCurrentItemPoint.x][mCurrentItemPoint.y])
				{
					mRemainingEmptyCellCount--;
				}
				mCurrentItemData = data;
				mShownData[mCurrentItemPoint.x][mCurrentItemPoint.y] = mCurrentItemData;
				mShownHints[mCurrentItemPoint.x][mCurrentItemPoint.y] = true;
				mCurrentItemView.setText(String.valueOf(mCurrentItemData));

				if (mRemainingEmptyCellCount <= 0)
				{
					if (mOnCompleteListener != null)
					{
						mOnCompleteListener.OnComplete(isComplete());
					}
				}
			}
			else
			{
				mCurrentItemData = 0;
				mShownData[mCurrentItemPoint.x][mCurrentItemPoint.y] = mCurrentItemData;
				mShownHints[mCurrentItemPoint.x][mCurrentItemPoint.y] = false;
				mCurrentItemView.setText("");
			}
		}
	}

	@Override
	public void clearCurrentCellData()
	{
		if (mCurrentItemView != null)
		{
			if (mCurrentItemData != 0)
			{
				mRemainingEmptyCellCount++;
			}
			mCurrentItemData = 0;
			mCurrentItemView.setText("");
			mShownData[mCurrentItemPoint.x][mCurrentItemPoint.y] = mCurrentItemData;
			mShownHints[mCurrentItemPoint.x][mCurrentItemPoint.y] = false;
		}
	}

	@Override
	public boolean isValidPoint(Point point)
	{
		if (point != null)
		{
			if (0 <= point.x && point.x < sDefaultData.length)
			{
				if (0 <= point.y && point.y < sDefaultData.length)
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isComplete()
	{
		if (mOriginData != null && mShownData != null && mRemainingEmptyCellCount <= 0)
		{
			for (int i = 0; i < mOriginData.length; i++)
			{
				for (int j = 0; j < mOriginData[i].length; j++)
				{
					if (mOriginData[i][j] != mShownData[i][j])
					{
						Log.i(TAG, "[isComplete] result is " + false);
						return false;
					}
				}
			}
			Log.i(TAG, "[isComplete] result is " + true);
			return true;
		}
		Log.i(TAG, "[isComplete] result is " + false);
		return false;
	}

	private void initItemView()
	{
		int width = getWidth();
		int cellWidth = (width - getPaddingLeft() - getPaddingRight()) / 9;
		int left = (width - cellWidth * 9) / 2;
		setPadding(left, left, left, left);
		for (int i = 0; i < mChildViews.length; i++)
		{
			for (int j = 0; j < mChildViews[i].length; j++)
			{
				final LayoutParams ll_lp = new LayoutParams();
				ll_lp.width = cellWidth;
				ll_lp.height = cellWidth;
				ll_lp.setGravity(Gravity.CENTER);

				SudokuItemView itemView = new SudokuItemView(getContext());
				itemView.setGravity(Gravity.CENTER);
				itemView.setTextColor(COLOR_DEFAULT_TEXT);
				itemView.getPaint().setFakeBoldText(true);
				itemView.setTextSize(ScreenUtil.getDensity(getContext()) * SIZE_DEFAULT_TEXT);
				if (mOriginHints != null && mOriginHints[i][j])
				{
					// 如果此单元格显示，就减一
					mRemainingEmptyCellCount--;
					mShownData[i][j] = mOriginData[i][j];
					mShownHints[i][j] = true;
					itemView.setText(String.valueOf(mOriginData[i][j]));
					itemView.setBackgroundResource(R.drawable.sukodu_item_siv_shown);
				}
				else
				{
					mShownData[i][j] = 0;
					mShownHints[i][j] = false;
					itemView.setText("");
					itemView.setBackgroundResource(R.drawable.sukodu_item_siv_gone);
				}
				itemView.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						onItemViewClick(v);
					}
				});

				int paddingLeft = PADDING_DEFAULT_DIVIDING;
				int paddingTop = PADDING_DEFAULT_DIVIDING;
				int paddingRight = PADDING_DEFAULT_DIVIDING;
				int paddingBottom = PADDING_DEFAULT_DIVIDING;
				// if (((i + 1) % 3 == 0 && i != mChildViews.length - 1))
				if ((i + 1) % 3 == 0)
				{
					paddingBottom = PADDING_DEFAULT_ITEM_VIEW_THICK;
				}
				if (i % 3 == 0)
				{
					paddingTop = PADDING_DEFAULT_ITEM_VIEW_THICK;
				}
				if ((j + 1) % 3 == 0)
				{
					paddingRight = PADDING_DEFAULT_ITEM_VIEW_THICK;
				}
				if (j % 3 == 0)
				{
					paddingLeft = PADDING_DEFAULT_ITEM_VIEW_THICK;
				}
				itemView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
				itemView.setLayoutParams(ll_lp);
				addView(itemView);
				mChildViews[i][j] = itemView;
			}
		}
	}

	private void onItemViewClick(View v)
	{
		if (mCurrentItemView != null && mCurrentItemPoint != null)
		{
			if (mOriginHints[mCurrentItemPoint.x][mCurrentItemPoint.y])
			{
				mCurrentItemView.setBackgroundResource(R.drawable.sukodu_item_siv_shown);
			}
			else
			{
				mCurrentItemView.setBackgroundResource(R.drawable.sukodu_item_siv_gone);
			}
		}
		mCurrentItemView = (SudokuItemView) v;
		mCurrentItemView.setBackgroundResource(R.drawable.sukodu_item_siv_selected);
		// 二维数组和x，y坐标相反
		mCurrentItemPoint.x = (int) (mCurrentItemView.getY() / mCurrentItemView.getHeight());
		mCurrentItemPoint.y = (int) (mCurrentItemView.getX() / mCurrentItemView.getWidth());
		try
		{
			mCurrentItemData = Integer.valueOf(mCurrentItemView.getText().toString());
		}
		catch (NumberFormatException e)
		{
			mCurrentItemData = 0;
			Log.i(TAG, "[onItemViewClick] selected a empty cell");
		}
		if (mOnItemClickListener != null)
		{
			mOnItemClickListener.onItemClick(mCurrentItemPoint, mCurrentItemView, mRemainingEmptyCellCount);
		}
		else
		{
			Log.i(TAG, "[onItemViewClick] OnItemClickListener is null");
		}
	}

	@Override
	protected void onMeasure(int widthSpec, int heightSpec)
	{
		super.onMeasure(widthSpec, widthSpec);
	}

	public void setOnItemClickListener(OnSukoduItemClickListener l)
	{
		if (l != null)
		{
			this.mOnItemClickListener = l;
		}
		else
		{
			Log.e(TAG, "[setOnItemClick] OnItemClickListener must not be null");
		}
	}

	public void setOnCompleteListener(OnCompleteListener l)
	{
		if (l != null)
		{
			this.mOnCompleteListener = l;
		}
		else
		{
			Log.e(TAG, "[setOnCompleteListener] OnCompleteListener must not be null");
		}
	}
}
