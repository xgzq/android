package com.xgzq.android.interfaces.sudoku;

import java.util.List;

public interface IData extends ISudoku
{
	List<int[][]> generateAllData();

	int[][] getRandomData();

	boolean isSudokuRuleData(int[][] data);

	boolean containsData(int[][] data);

}
