package com.xgzq.android.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.xgzq.android.interfaces.sudoku.IData;
import com.xgzq.android.tools.ArrayUtil;

import android.graphics.Point;

public class SudokuData implements IData
{
	private Map<String, int[][]> mDataMap = new HashMap<String, int[][]>();

	private int[][] mData = new int[9][9];

	public SudokuData()
	{
		// new ScheduledThreadPoolExecutor(1).execute(new Runnable()
		// {
		//
		// @Override
		// public void run()
		// {
		// generateAllData();
		// getRandomData();
		// }
		// });

	}

	@Override
	public int[][] getCurrentData()
	{
		return mData;
	}

	@Override
	public int[] getRowData(int row)
	{
		return ArrayUtil.getRow(mData, row);
	}

	@Override
	public int[] getColumnData(int column)
	{
		return ArrayUtil.getColumn(mData, column);
	}

	@Override
	public int[] getDiagonalData()
	{
		return ArrayUtil.getDiagonalData(mData);
	}

	@Override
	public int[] getBackDiagonalData()
	{
		return ArrayUtil.getBackDiagonalData(mData);
	}

	@Override
	public int getPointData(Point point)
	{
		return isValidPoint(point, mData) ? mData[point.x][point.y] : 0;
	}

	@Override
	public int[][] getBlockData(Point point)
	{
		return ArrayUtil.getBlockData(point, mData);
	}

	@Override
	public List<int[][]> generateAllData()
	{
		// TODO Auto-generated method stub
		// 利用算法计算出所有符合规则的数独数据
		// mDataMap = result;
		return null;
	}

	@Override
	public int[][] getRandomData()
	{
		if (mDataMap != null)
		{
			Random random = new Random();
			int index = random.nextInt(mDataMap.size());
			Set<String> keySet = mDataMap.keySet();
			int i = 0;
			for (String key : keySet)
			{
				if (i == index)
				{
					mData = mDataMap.get(key);
					return mData;
				}
				i++;
			}
		}
		return null;
	}

	@Override
	public boolean isSudokuRuleData(int[][] data)
	{
		//TODO
		return mDataMap.containsKey(getKey(data));
	}

	@Override
	public boolean containsData(int[][] data)
	{
		return mDataMap.containsKey(getKey(data));
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

	private String getKey(int[][] data)
	{
		StringBuffer sb = new StringBuffer();
		if (data != null)
		{
			for (int i = 0; i < data.length; i++)
			{
				for (int j = 0; j < data[i].length; j++)
				{
					sb.append(data[i][j]);
				}
			}
		}
		return sb.toString();
	}
}
