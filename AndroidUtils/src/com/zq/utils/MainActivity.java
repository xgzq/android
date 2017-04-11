package com.zq.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;

import com.zq.utils.log.Logger;
import com.zq.utils.test.Test;
import com.zq.utils.test.Test2;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String[] a = new String[]{"zzzzzzzzzz","ssssssssssss","ddddddddddd","qqqqqqqqqqqq","ggggggggggggg","tttttttttttttt","yyyyyyyyyyyyyyy","ooooooooooooooo","jjjjjjjjjjjjj"};
		String c = "this is c..";
		Test t = new Test();
		Test2 t2 = new Test2("name", 20, t);
		
		List<Test2> list = new ArrayList<Test2>();
		list.add(new Test2("111", 11, t));
		list.add(new Test2("222", 22, t));
		list.add(new Test2("333", 33, t));
		list.add(new Test2("444", 44, t));
		list.add(new Test2("555", 55, t));
		
		Map<String, Test2> map = new HashMap<String, Test2>();
		map.put("one", new Test2("one", 1, t));
		map.put("two", new Test2("two", 1, t));
		map.put("three", new Test2("three", 1, t));
		map.put("four", new Test2("four", 1, t));
		
		int[] b = {88,59,45,464,84,54,6};
		long[] l = {111,3333,4444,5555,6666};
		float[] f = {5.2f,12.5f,0.02f,55f};
		boolean[] bs = {true,true,false,false,true};
		char[] cs = {'a','b','d','e','1'};
		double[] ds = {2.02,45.56,45.00,15,-5.6};
		
//		Logger.logObjects(c,t,t2,5,6.2f);
//		Logger.logArray(a);
		Logger.logArray(b);
		Logger.logArray(l);
		Logger.logArray(f);
		Logger.logArray(bs);
		Logger.logArray(cs);
		Logger.logArray(ds);
		
//		Logger.logList(list);
//		Logger.logMap(map);
	}

}
