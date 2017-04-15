package com.zq.utils.log;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zq.utils.constant.Constant.DateFormats;
import com.zq.utils.other.Utils;

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
	public static final String UNDER_LINE = "_";
	
	
	//********************* private static *********************//
	
	/**
	 * 当前级别大于日志级别的时候才打印日志
	 */
	private static int sCurrentLogLevel = ALL;
	
	/**
	 * 日志中是否打印日志时间
	 */
	private static boolean isLogTime = true;
	
	private static String sDateTimeFormat = DateFormats.F;
	
	
	/**
	 * 是否已经初始化过
	 */
	private static boolean isInited = false;
	
	/**
	 * 是否打印高级比的日志</br>
	 * true:只打印比当前定义级别sCurrentLevel高的日志</br>
	 * false:只打印比当前定义级别sCurrentLevel低的日志</br>
	 */
	private static boolean isLogBigLevel = false;
	
	/**
	 * 日志打印总开关，默认打开:true</br>
	 * true:打开打印</br>
	 * false:关闭打印</br>
	 */
	private static boolean isOpenLogger = true;
	
	private static String sTagHeader = "Logger";
	private static String sTagFooter = "";
	private static String sMethodName = "";
	private static int sLineNumber = 0;
	
	private Logger() 
	{
		
	}
	
	public synchronized static void initLogger(Context ctx)
	{
		if(!isInited)
		{
			getTagInfo(3);
			Log.d(sTagHeader + "_Logger", formatInfo("start init logger..."));
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
						Log.d(sTagHeader + "_Logger", formatInfo("sTagHeader is " + appLabel.toString()));
					}
					else
					{
						Log.e(sTagHeader + "_Logger", formatInfo("appInfo is null,use default tag header..."));
					}
				}
				else
				{
					Log.e(sTagHeader + "_Logger", formatInfo("pm is null,use default tag header..."));
				}
			}
			else
			{
				Log.e(sTagHeader + "_Logger", formatInfo("context is null,use default tag header..."));
			}
			isInited = true;
			Log.d(sTagHeader + "_Logger", formatInfo("end init logger..."));
		}
		else
		{
			Log.e(sTagHeader + "_Logger", formatInfo("The Logger has init once,and it's only can init once!"));
		}
	}
	
	public static void v(String msg)
	{
		printf(msg, VERBOSE,5);
	}
	
	public static void v(String msg,int count)
	{
		printf(msg, VERBOSE,count);
	}
	
	public static void d(String msg)
	{
		printf(msg, DEBUG,5);
	}
	
	public static void d(String msg,int count)
	{
		printf(msg, DEBUG,count);
	}
	
	public static void i(String msg)
	{
		printf(msg, INFO,5);
	}
	
	public static void i(String msg,int count)
	{
		printf(msg, INFO,count);
	}
	
	public static void w(String msg)
	{
		printf(msg, WARN,5);
	}
	
	public static void w(String msg,int count)
	{
		printf(msg, WARN,count);
	}
	
	public static void e(String msg)
	{
		printf(msg, ERROR,5);
	}
	
	public static void e(String msg,int count)
	{
		printf(msg, ERROR,count);
	}
	
	/**
	 * 打印数组，以toString方式
	 * @param objs
	 */
	public static void logArray(Object[] objs)
	{
		if(objs != null && objs.length > 0)
		{
			for (int i = 0; i < objs.length; i++)
			{
				i("Array[" + i + "]=" + (objs[i] == null ? "null" : objs[i].toString()),5);
			}
		}
	}
	
	public static void logArray(int[] ints)
	{
		if(ints != null && ints.length > 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("int{");
			for (int i = 0; i < ints.length; i++)
			{
				sb.append(ints[i] + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
			i(sb.toString(),5);
		}
	}
	
	public static void logArray(long[] longs)
	{
		if(longs != null && longs.length > 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("long{");
			for (int i = 0; i < longs.length; i++)
			{
				sb.append(longs[i] + "L,");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
			i(sb.toString(),5);
		}
	}
	
	public static void logArray(float[] floats)
	{
		if(floats != null && floats.length > 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("float{");
			for (int i = 0; i < floats.length; i++)
			{
				sb.append(floats[i] + "f,");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
			i(sb.toString(),5);
		}
	}
	
	public static void logArray(double[] floats)
	{
		if(floats != null && floats.length > 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("double{");
			for (int i = 0; i < floats.length; i++)
			{
				sb.append(floats[i] + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
			i(sb.toString(),5);
		}
	}
	
	public static void logArray(boolean[] booleans)
	{
		if(booleans != null && booleans.length > 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("boolean{");
			for (int i = 0; i < booleans.length; i++)
			{
				sb.append(booleans[i] + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
			i(sb.toString(),5);
		}
	}
	
	public static void logArray(char[] chars)
	{
		if(chars != null && chars.length > 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("char{");
			for (int i = 0; i < chars.length; i++)
			{
				sb.append("'" + chars[i] + "',");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
			i(sb.toString(),5);
		}
	}
	
	/**
	 * 打印多个对象，以toString方式
	 * @param objs
	 */
	public static void logObjects(Object... objs)
	{
		if(objs != null && objs.length > 0)
		{
			for (int i = 0; i < objs.length; i++)
			{
				i("Array[" + i + "]=" + (objs[i] == null ? "null" : objs[i].toString()),5);
			}
		}
	}
	
	/**
	 * 打印List中的每个对象，以toString方式
	 * @param list
	 */
	public static <E> void logList(List<E> list)
	{
		if(list != null && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++)
			{
				E e = list.get(i);
				i("List[" + i + "]=" + (e == null ? "null" : e.toString()),5);
			}
		}
	}
	
	public static <K,V> void logMap(Map<K, V> map)
	{
		if(map != null && map.size() > 0)
		{
			Set<K> keySet = map.keySet();
			if(keySet != null && keySet.size() > 0)
			{
				for (K k : keySet)
				{
					if(k != null)
					{
						V v = map.get(k);
						i("Map<" + k.toString() + ">=" + (v == null ? "null" : v.toString()),5);
					}
				}
			}
		}
	}
	
	public synchronized static void isOnlyLogBigLevel(boolean b)
	{
		isLogBigLevel = b;
	}
	
	public synchronized static void isLogTime(boolean b)
	{
		isLogTime = b;
	}
	
	public synchronized static void setCurrentLogLevel(int l)
	{
		sCurrentLogLevel = l;
	}
	
	public synchronized static void isOpenLogger(boolean b)
	{
		isOpenLogger = b;
	}
	
	/**
	 * 设置时间打印格式
	 * @param format 必须是从DateFormats中获取的常量
	 */
	public synchronized static void setDateTimeFormat(String format)
	{
		sDateTimeFormat = format;
	}
	
	private static void printf(String msg,int level,int count)
	{
		if(isOpenLogger)
		{
			getTagInfo(count);
			if(isLogBigLevel && level >= sCurrentLogLevel)
			{
				Log.println(level, formatTag(),	formatInfo(msg));
			}
			else if(!isLogBigLevel && sCurrentLogLevel >= level)
			{
				Log.println(level, formatTag(),	formatInfo(msg));
			}
		}
	}
	
	private static String formatTag()
	{
		String tag = sTagHeader + UNDER_LINE + sTagFooter;
		return tag;
	}
	
	private static String formatInfo(String msg)
	{
		String info = "";
		if(isLogTime)
		{
			info = "[" + sMethodName + ":" + sLineNumber + "L " + Utils.getDateTime(sDateTimeFormat) + "]" + msg;
		}
		else
		{
			info = "[" + sMethodName + ":" + sLineNumber + "L ]" + msg;
		}
		return info;
	}
	
//	private static String formatLocalInfo(String msg)
//	{
//		String info = "";
//		if(isLogTime)
//		{
//			info = "[" + sMethodName + ":" + sLineNumber + "L " + Utils.getDateTime(sDateTimeFormat) + "]" + msg;
//		}
//		else
//		{
//			info = "[" + sMethodName + ":" + sLineNumber + "L ]" + msg;
//		}
//		return info;
//	}
	
	private static void getTagInfo(int count)
	{
		Exception exception = new Exception();
		StackTraceElement[] elements = exception.getStackTrace();
		if(elements != null && elements.length >= count)
		{
			StackTraceElement element = elements[count - 1];
			sTagFooter = element.getFileName().replace(".java", "");
			sMethodName = element.getMethodName();
			sLineNumber = element.getLineNumber();
		}
	}
}
