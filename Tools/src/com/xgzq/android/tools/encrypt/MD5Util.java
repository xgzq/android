package com.xgzq.android.tools.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MD5Util
{
	
	public static final String TAG = "Tools_MD5Util";
	
	public static final String getMd5(String src)
	{
		String result = "";
		try
		{
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(src.getBytes());
			result = byteToHexString(mDigest.digest());
		}
		catch (NoSuchAlgorithmException e)
		{
			result = String.valueOf(src.hashCode());
		}
		return result;
	}

	private static final String byteToHexString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++)
		{
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1)
			{
				sb.append("0");
			}
			sb.append(hex);
		}
		return sb.toString().toUpperCase(Locale.getDefault());
	}
}
