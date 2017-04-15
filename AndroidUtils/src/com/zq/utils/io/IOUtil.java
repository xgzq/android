package com.zq.utils.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import android.text.TextUtils;

import com.zq.utils.constant.Constant.Encode;
import com.zq.utils.log.Logger;



public class IOUtil {
	
	public static String inputStream2String(InputStream is)
	{
		return inputStream2String(is,Encode.UTF_8);
	}

	public static String inputStream2String(InputStream is,String charset)
	{
		StringBuffer sb = new StringBuffer();
		if(null != is)
		{
			if(TextUtils.isEmpty(charset))
			{
				charset = new String(Encode.UTF_8);
			}
			BufferedReader bufferedReader = null;
			InputStreamReader inputStreamReader = null;
			try 
			{
				inputStreamReader = new InputStreamReader(is,charset);
				bufferedReader = new BufferedReader(inputStreamReader);
				String line = null;
				while((line = bufferedReader.readLine()) != null)
				{
					sb.append(line);
				}
			} 
			catch(UnsupportedEncodingException e)
			{
				Logger.e("readLine UnsupportedEncodingException is " + e.getMessage());
			}
			catch (IOException e)
			{
				Logger.e("readLine IOException is " + e.getMessage());
			}
			finally
			{
				try 
				{
					if(bufferedReader != null)
					{
						bufferedReader.close();
					}
					if(inputStreamReader != null)
					{
						inputStreamReader.close();
					}
					if(is != null)
					{
						is.close();
					}
				}
				catch (IOException e) 
				{
					Logger.e("close stream IOException is " + e.getMessage());
				}
			}
		}
		return sb.toString();
	}
	
	public static InputStream string2InputStream(String str)
	{
		return string2InputStream(str,Encode.UTF_8);
	}
	
	public static InputStream string2InputStream(String str,String charset)
	{
		ByteArrayInputStream in = null;
		if(str != null)
		{
			try
			{
				if(TextUtils.isEmpty(charset))
				{
					charset = new String(Encode.UTF_8);
				}
				in = new ByteArrayInputStream(str.getBytes(charset));
			} 
			catch (UnsupportedEncodingException e) 
			{
				Logger.e("getBytes(charset) happen UnsupportedEncodingException is " + e.getMessage());
			}
			finally
			{
				//InputStream不能关闭，后面需要用到
			}
		}
		return in;
	}
	
	public static String outputStream2String(OutputStream os)
	{
		String result = "";
		ByteArrayOutputStream baos = null;   
		try 
		{
			baos = new ByteArrayOutputStream();
			baos.writeTo(os);
			result = baos.toString();  
		} 
		catch (IOException e)
		{
			Logger.e("baos.writeTo(os) happen IOException is " + e.getMessage());
		}
		finally
		{
			try 
			{
				if(baos != null)
				{
					baos.close();
				}
				if(os != null)
				{
					os.close();
				}
			} 
			catch (IOException e) 
			{
				Logger.e("close baos or os happen IOException is " + e.getMessage());
			}
		}
		return result;
	}
	
	public static OutputStream string2OuptStream(String str)
	{
		return string2OuptStream(str, Encode.UTF_8);
	}
	
	public static OutputStream string2OuptStream(String str,String charset)
	{
		OutputStream os = null;
		if(str != null)
		{
			try 
			{
				os = System.out;
				if(TextUtils.isEmpty(charset))
				{
					charset = new String(Encode.UTF_8);
				}
				os.write(str.getBytes("utf-8"));
			}
			catch(UnsupportedEncodingException e)
			{
				Logger.e("str.getBytes happen UnsupportedEncodingException is " + e.getMessage());
			}
			catch (IOException e) 
			{
				Logger.e("os.write happen IOException is " + e.getMessage());
			}
			finally
			{
				//OutputStream不能关闭，后面需要用到
			}
		}
		return os;
	}
	
	public static String readFile(File file)
	{
		return readFile(file,Encode.UTF_8);
	}
	
	public static String readFile(File file,String charset)
	{
		String result = "";
		FileInputStream fis = null;
		try 
		{
			if(TextUtils.isEmpty(charset))
			{
				charset = new String(Encode.UTF_8);
			}
			fis = new FileInputStream(file);
			result = inputStream2String(fis, charset);
		}
		catch (FileNotFoundException e) 
		{
			Logger.e("FileInputStream(file) happen FileNotFoundException is " + e.getMessage());
		}
		finally
		{
			try 
			{
				if(fis != null)
				{	
					fis.close();
				}
			} 
			catch (IOException e)
			{
				Logger.e("close fis stream happen IOException is " + e.getMessage());
			}
		}
		return result;
	}
	
	public static String readFile(String path)
	{
		return readFile(path, Encode.UTF_8);
	}
	
	public static String readFile(String path,String charset)
	{
		File file = new File(path);
		return readFile(file, charset);
	}
	
	public static boolean writeNewFile(String content,String path)
	{
		if(path == null)
		{
			return false;
		}
		File file = new File(path);
		return writeNewFile(content, file);
	}
	
	public static boolean writeNewFile(String content,File file)
	{
		boolean result = false;
		FileOutputStream fos = null;
		PrintWriter pw = null;
		if(content == null)
		{
			content = "";
		}
		if(file != null)
		{
			try 
			{
				fos = new FileOutputStream(file);
				pw = new PrintWriter(fos);
				pw.write(content);
				pw.flush();
				result = true;
			} 
			catch (FileNotFoundException e)
			{
				Logger.e("FileOutputStream(file) happen FileNotFoundException is " + e.getMessage());
			}
			finally
			{
				try 
				{
					if(pw != null)
					{
						pw.close();
					}
					if(fos != null)
					{
						fos.close();
					}
				} 
				catch (IOException e) 
				{
					Logger.e("close pw or fos happen FileNotFoundException is " + e.getMessage());
				}
			}
		}
		return result;
	}
}
