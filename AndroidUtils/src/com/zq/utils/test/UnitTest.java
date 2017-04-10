package com.zq.utils.test;

import android.test.AndroidTestCase;

import com.zq.utils.log.Logger;

public class UnitTest extends AndroidTestCase{

	
	public UnitTest() {
		testLogger();
		
	}

	private void testLogger() {
		Logger.initLogger(getContext());
	}
}
