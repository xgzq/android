package com.xgzq.android.sudoku.interfaces;

import com.xgzq.android.sudoku.view.SudokuItemView;

import android.graphics.Point;

/**
 * @author Administrator
 *
 */
public interface IView extends ISudoku
{
	/**
	 * 设置所有数据
	 * @param data
	 * 原始数据
	 */
	void setAllData(int[][] data);
	
	/**
	 * 清除所有用户输入的数据（恢复到原始数据状态）
	 */
	void clearAllData();
	
	/**
	 * @return
	 * 获取已显示的数据
	 */
	int[][] getShownData();
	
	/**
	 * 设置指定的单元格数据
	 * @param point
	 * 需要设置的单元格坐标
	 * @param data
	 * 需要设置的单元格数据
	 */
	void setCellData(Point point,int data);
	
	/**
	 * 清除指定单元格的数据
	 * @param point
	 * 指定的单元格坐标
	 */
	void clearCellData(Point point);
	
	/**
	 * 设置当前选中的单元格的数据
	 * @param data
	 * 需要设置的数据
	 */
	void setCurrentCellData(int data);
	
	/**
	 * 获取当前选中的单元格中的数据
	 * @return
	 * 选中的单元格的数据
	 */
	int getCurrentData();
	
	
	/**
	 * 清除当前选择的单元格中的数据
	 */
	void clearCurrentCellData();
	
	/**
	 * 获取指定的单元格的ItemView
	 * @param point
	 * 指定的单元格
	 * @return
	 * 指定单元格的ItemView
	 */
	SudokuItemView getItemView(Point point);
	
	/**
	 * 获取当前选中的单元格的ItemView
	 * @return
	 */
	SudokuItemView getCurrentView();
	
	/**
	 * 获取当前选择的单元格的坐标
	 * @return
	 */
	Point getCurrentPoint();
	
	void setAllHintable(boolean[][] hints);
	
	boolean isContainsInRow(int data,int row);
	
	boolean isContainsInColumn(int data,int column);
	
	boolean isContainsInBlock(int data,Point point);
	
	boolean isContainsInDiagonal(int data);
	
	boolean isContainsInBackDiagonal(int data);
	
	boolean isComplete();
}
