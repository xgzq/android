package com.xgzq.android.interfaces.sudoku;

import android.graphics.Point;

public interface IView extends ISudoku
{
	void setAllData(int[][] data);
	
	void setCellData(Point point,int data);
	
	void clearCellData(Point point);
	
	boolean isContainsInRow(int data,int row);
	
	boolean isContainsInColumn(int data,int column);
	
	boolean isContainsInBlock(int data,Point point);
	
	boolean isContainsInDiagonal(int data);
	
	boolean isContainsInBackDiagonal(int data);
	
	int getCellData(Point point);
}
