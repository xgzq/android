package com.zq.utils.other;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

	
	
	public static String getDateTime(String format)
	{
		String date = "";
		long currentTimeMillis = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CHINA);
		date = sdf.format(new Date(currentTimeMillis));
		return date;
	}
}
