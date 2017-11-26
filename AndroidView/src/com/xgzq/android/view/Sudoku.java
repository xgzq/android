package com.xgzq.android.view;

import com.xgzq.android.interfaces.sudoku.IView;
import com.xgzq.android.tools.ArrayUtil;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class Sudoku extends FrameLayout implements IView
{
	private int[][] mData;

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
		this.mData = new int[9][9];
	}

	private void initOperation()
	{

	}

	@Override
	public int[][] getCurrentData()
	{
		if (mData == null)
		{
			this.mData = new int[9][9];
		}
		return mData;
	}

	@Override
	public int[] getRowData(int row)
	{
		if (mData != null && 0 <= row && row < mData.length)
		{
			return mData[row];
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
		if (mData != null)
		{
			return ArrayUtil.getColumn(mData, column);
		}
		return sDefaultData;
	}

	@Override
	public int[] getDiagonalData()
	{
		if (mData != null)
		{
			return ArrayUtil.getDiagonalData(mData);
		}
		return sDefaultData;
	}

	@Override
	public int[] getBackDiagonalData()
	{
		if (mData != null)
		{
			return ArrayUtil.getBackDiagonalData(mData);
		}
		return sDefaultData;
	}

	@Override
	public int[][] getBlockData(Point point)
	{
		if (isValidPoint(point, mData) && mData != null)
		{
			int[][] result = new int[3][3];
			int x = point.x / 3;
			int y = point.y / 3;
			for (int i = 0; i < result.length; i++)
			{
				for (int j = 0; j < result[i].length; j++)
				{
					result[i][j] = mData[3 * x + i][3 * y + j];
				}
			}
			return result;
		}
		return null;
	}

	@Override
	public int getPointData(Point point)
	{
		if (isValidPoint(point, mData))
		{
			return mData[point.x][point.y];
		}
		return 0;
	}

	@Override
	public void setAllData(int[][] data)
	{
		if (data != null && data.length == 9 && data[0].length == 9)
		{
			mData = data;
		}
	}

	@Override
	public void setCellData(Point point, int data)
	{
		if (isValidPoint(point, mData) && 1 <= data && data <= 9)
		{
			// TODO
			// tv.setText(String.valueOf(data));
		}

	}

	@Override
	public void clearCellData(Point point)
	{
		if (isValidPoint(point, mData))
		{
			// TODO
			// tv.setText("");
		}
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
	public int getCellData(Point point)
	{
		if (isValidPoint(point, mData))
		{
			return mData[point.x][point.y];
		}		
		return 0;
	}

	@Override
	public boolean isValidPoint(Point point, int[][] data)
	{
		if (point != null && data != null)
		{
			if (0 <= point.x && point.x < data.length)
			{
				if (data[point.x] != null && 0 <= point.y && point.y < data[point.x].length)
				{
					return true;
				}
			}
		}
		return false;
	}

}
