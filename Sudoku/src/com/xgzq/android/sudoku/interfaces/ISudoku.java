package com.xgzq.android.sudoku.interfaces;

import android.graphics.Point;

/**
 * @author Administrator
 * 公共接口：IData和ISudoku的公共接口，都需要实现具体逻辑
 */
public interface ISudoku
{
	final int[] sDefaultData = new int[9];
	
	/**
	 * @return
	 * 获取整个二维数组所有数据，具体实现根据情况
	 */
	int[][] getAllData();

	/**
	 * @param point 坐标(x,y) -> int[x][y]
	 * @return
	 * 获取一个小九宫格的数据，根据传入的坐标来确定是哪个九宫格。</br>
	 * 具体实现逻辑视情况而定
	 */
	int[][] getBlockData(Point point);

	/**
	 * @param row 下标（位于哪一行）
	 * @return
	 * 返回二位数组的一行数据
	 */
	int[] getRowData(int row);

	/**
	 * @param column 下标（位于哪一列）
	 * @return
	 * 返回二位数组的一列数据
	 */
	int[] getColumnData(int column);

	/**
	 * @return
	 * 获取对角线的数据（从上到下排序）
	 */
	int[] getDiagonalData();

	/**
	 * @return
	 * 获取反对角线的数据（从上到下排序）
	 */
	int[] getBackDiagonalData();

	/**
	 * @param point 坐标（x,y） -> int[x][y]
	 * @return
	 * 返回传入坐标位置的数据
	 */
	int getPointData(Point point);
	
	boolean isValidPoint(Point point);
}
