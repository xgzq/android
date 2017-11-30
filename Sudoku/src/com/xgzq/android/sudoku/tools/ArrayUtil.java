package com.xgzq.android.sudoku.tools;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.util.Log;

public class ArrayUtil
{
	private static final String TAG = "Sudoku_ArrayUtil";

	public static int[] getRow(int[][] data, int row)
	{
		if (data != null && 0 <= row && row < data.length)
		{
			return data[row];
		}
		return null;
	}

	public static int[] getColumn(int[][] data, int column)
	{
		if (data != null && 0 <= column)
		{
			int result[] = new int[data.length];
			for (int i = 0; i < data.length; i++)
			{
				if (data[i] != null && column < data[i].length)
				{
					result[i] = data[i][column];
				}
			}
			return result;
		}
		return null;
	}

	public static void printArray(int[] data)
	{
		if (data == null)
		{
			Log.e(TAG, "[printArray] array is null");
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for (int i = 0; i < data.length; i++)
		{
			sb.append(data[i]);
			sb.append((i == data.length - 1) ? "" : ",");
		}
		sb.append("}");
		Log.e(TAG, "[printArray] " + sb.toString());
	}

	public static void printArray(int[][] data)
	{
		for (int i = 0; i < data.length; i++)
		{
			printArray(data[i]);
		}
	}

	public static int[] getDiagonalData(int[][] data)
	{
		List<Integer> r = new ArrayList<Integer>();
		if (data != null)
		{
			for (int i = 0; i < data.length; i++)
			{
				if (data.length != data[i].length)
				{
					Log.e(TAG, "[getDiagonalData] row length not equal column length.");
					return null;
				}
				for (int j = 0; j < data[i].length; j++)
				{
					if (i == j)
					{
						r.add(data[i][j]);
					}
				}
			}
		}
		Integer[] re = r.toArray(new Integer[] {});
		int[] result = new int[re.length];
		for (int i = 0; i < re.length; i++)
		{
			result[i] = re[i].intValue();
		}
		return result;
	}

	public static int[] getBackDiagonalData(int[][] data)
	{
		List<Integer> r = new ArrayList<Integer>();
		if (data != null)
		{
			for (int i = 0; i < data.length; i++)
			{
				if (data.length != data[i].length)
				{
					Log.e(TAG, "[getDiagonalData] row length not equal column length.");
					return null;
				}
				for (int j = 0; j < data[i].length; j++)
				{
					if (i + j == data.length - 1)
					{
						r.add(data[i][j]);
					}
				}
			}
		}
		Integer[] re = r.toArray(new Integer[] {});
		int[] result = new int[re.length];
		for (int i = 0; i < re.length; i++)
		{
			result[i] = re[i].intValue();
		}

		return result;
	}

	public static boolean contains(int[] data, int d)
	{
		if (data != null)
		{
			for (int i = 0; i < data.length; i++)
			{
				if (data[i] == d)
				{
					return true;
				}
			}
		}
		return false;
	}

	public static boolean contains(int[][] data, int d)
	{
		if (data != null)
		{
			for (int i = 0; i < data.length; i++)
			{
				for (int j = 0; j < data[i].length; j++)
				{
					if (data[i][j] == d)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int[][] getBlockData(Point point, int[][] data)
	{
		int[][] re = new int[3][3];
		int x = point.x / 3;
		int y = point.y / 3;
		for (int i = 0; i < re.length; i++)
		{
			for (int j = 0; j < re[i].length; j++)
			{
				re[i][j] = data[3 * x + i][3 * y + j];
			}
		}
		return re;
	}

	public static int arraySum(int[] data)
	{
		int sum = 0;
		if (data != null)
		{
			for (int i = 0; i < data.length; i++)
			{
				sum += data[i];
			}
		}
		return sum;
	}

	public static int arraySum(int[][] data)
	{
		int sum = 0;
		if (data != null)
		{
			for (int i = 0; i < data.length; i++)
			{
				sum += arraySum(data[i]);
			}
		}
		return sum;
	}
}
