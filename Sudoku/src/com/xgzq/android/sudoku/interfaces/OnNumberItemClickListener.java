package com.xgzq.android.sudoku.interfaces;

public interface OnNumberItemClickListener
{
	/**
	 * Item点击回调方法
	 * @param number
	 * 点击的Item的值：如果type为TYPE_CLEAR或者TYPE_CLEAR_ALL，number则返回-1
	 * @param type
	 * 点击的Item的类型
	 */
	void onItemClick(int number,int type);
}
