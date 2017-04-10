package com.zq.utils.log;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;


public final class Logger {

	//********************* public static final *********************//
	
	/**
	 * 所以的日志都不打印
	 */
	public static final int NONE = 1;
	
	/**
	 * VERBOSE级别的日志
	 */
	public static final int VERBOSE = 2;
	
	/**
	 * DEBUG级别的日志
	 */
	public static final int DEBUG = 3;
	
	/**
	 * INFO级别的日志
	 */
	public static final int INFO = 4;
	
	/**
	 * WARN级别的日志
	 */
	public static final int WARN = 5;
	
	/**
	 * ERROR级别的日志
	 */
	public static final int ERROR = 6;
	
	/**
	 * 打印所有级别的日志
	 */
	public static final int ALL = 7;
	
	/**
	 * 下划线
	 */
	public static final String sLine = "_";
	
	
	
	//********************* public static *********************//
	
	/**
	 * 当前级别大于日志级别的时候才打印日志
	 */
	public static int sCurrentLogLevel = INFO;
	
	/**
	 * 日志中是否打印日志时间
	 */
	public static boolean isLogTime = false;
	
	/**
	 * 是否已经初始化过
	 */
	private static boolean isInited = false;
	
	/**
	 * 是否打印高级比的日志</br>
	 * true:只打印比当前定义级别sCurrentLevel高的日志</br>
	 * false:只打印比当前定义级别sCurrentLevel低的日志</br>
	 */
	private static boolean isLogBigLevel = true;
	
	private static String sTagHeader = "Logger";
	private static String sTagFooter = "";
	private static String sMethodName = "";
	private static int sLineNumber = 0;
	
	private Logger() 
	{
		
	}
	
	public static void initLogger(Context ctx)
	{
		if(!isInited)
		{
			Log.d(sTagHeader + "_Logger", formatLocalInfo("start init logger..."));
			if(ctx != null)
			{
				PackageManager pm = ctx.getPackageManager();
				if(pm != null)
				{
					ApplicationInfo appInfo = ctx.getApplicationInfo();
					if(appInfo != null)
					{
						CharSequence appLabel = appInfo.loadLabel(pm);
						sTagHeader = appLabel.toString();
						Log.d(sTagHeader + "_Logger", formatLocalInfo("sTagHeader is " + appLabel.toString()));
					}
					else
					{
						Log.e(sTagHeader + "_Logger", formatLocalInfo("appInfo is null,use default tag header..."));
					}
				}
				else
				{
					Log.e(sTagHeader + "_Logger", formatLocalInfo("pm is null,use default tag header..."));
				}
			}
			else
			{
				Log.e(sTagHeader + "_Logger", formatLocalInfo("context is null,use default tag header..."));
			}
			isInited = true;
			Log.d(sTagHeader + "_Logger", formatLocalInfo("end init logger..."));
		}
		else
		{
			Log.e(sTagHeader + "_Logger", formatLocalInfo("The Logger has init once,and it's only can init once!"));
		}
	}
	
	public static void v(String msg)
	{
		printf(msg, VERBOSE);
	}
	
	public static void d(String msg)
	{
		printf(msg, DEBUG);
	}
	
	public static void i(String msg)
	{
		printf(msg, INFO);
	}
	
	public static void w(String msg)
	{
		printf(msg, WARN);
	}
	
	public static void e(String msg)
	{
		printf(msg, ERROR);
	}
	
	public static void isLogBigLevel(boolean b)
	{
		isLogBigLevel = b;
	}
	
	public static void isLogTime(boolean b)
	{
		isLogTime = b;
	}
	
	public static void setCurrentLogLevel(int l)
	{
		sCurrentLogLevel = l;
	}
	
	private static void printf(String msg,int level)
	{
		if(isLogBigLevel && level >= sCurrentLogLevel)
		{
			Log.println(level, formatTag(),	formatInfo(msg));
		}
		else if(!isLogBigLevel && sCurrentLogLevel >= level)
		{
			Log.println(level, formatTag(),	formatInfo(msg));
		}
	}
	
	private static String formatTag()
	{
		getTagInfo(5);
		String tag = sTagHeader + sLine + sTagFooter;
		return tag;
	}
	
	private static String formatInfo(String msg)
	{
		getTagInfo(5);
		String info = "[" + sMethodName + ":" + sLineNumber + "L] " + msg;
		return info;
	}
	
	private static String formatLocalInfo(String msg)
	{
		getTagInfo(3);
		String info = "[" + sMethodName + ":" + sLineNumber + "L] " + msg;
		return info;
	}
	
	private static void getTagInfo(int count)
	{
		Exception exception = new Exception();
		StackTraceElement[] elements = exception.getStackTrace();
		if(elements != null && elements.length >= count)
		{
			StackTraceElement element = elements[count - 1];
			sTagFooter = element.getFileName();
			sMethodName = element.getMethodName();
			sLineNumber = element.getLineNumber();
		}
	}
}
