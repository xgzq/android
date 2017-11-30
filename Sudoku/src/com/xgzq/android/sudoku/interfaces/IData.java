package com.xgzq.android.sudoku.interfaces;

/**
 * @author Administrator 数据接口，方便获取和处理数据
 */
public interface IData extends ISudoku
{
	/**
	 * <b>根据算法自动生成所有符合数独规则的二位数组</b></br>
	 * 第一次根据算法生成，生成 后存储到文件中，下次启动直接从文件中读取（节约算法生成时间）
	 * 
	 * @return 返回所有符合数独规则的二位数组的List
	 */
//	List<int[][]> generateAllData();

	/**
	 * <b>随机生成一个随机数，然后根据随机数从所有数独数据中获取一个二位数组</b>
	 * 
	 * @return 随机生成返回一个符合数独规则的二位数组数据
	 */
//	int[][] getRandomData();

	/**
	 * 从数据判断是否符合，或者直接判断是否在所有二位数组队列中
	 * 
	 * @param data
	 * @return 是否符合数独规则的数据
	 */
	boolean isSudokuData(int[][] data);

	/**
	 * 所有队列中是否存在此数据
	 * 
	 * @param data
	 * @return
	 */
	boolean containsData(int[][] data);

	/**
	 * 生成给出的提示数字的坐标
	 * @return
	 */
	boolean[][] getHintPoints();
}
