package com.xgzq.android.interfaces.sudoku;

import android.graphics.Point;

public interface ISudoku
{
	final int[] sDefaultData = new int[9];

	int[][] getCurrentData();

	int[] getRowData(int row);

	int[] getColumnData(int column);

	int[] getDiagonalData();

	int[] getBackDiagonalData();

	int getPointData(Point point);

	int[][] getBlockData(Point point);
	
	boolean isValidPoint(Point point, int[][] data);
}
