package com.xgzq.android.sudoku.interfaces;

import com.xgzq.android.sudoku.view.SudokuItemView;

import android.graphics.Point;

public interface OnSukoduItemClickListener
{
	void onItemClick(Point point, SudokuItemView itemView, int data);
}
