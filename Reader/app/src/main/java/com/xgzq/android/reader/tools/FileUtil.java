package com.xgzq.android.reader.tools;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtil
{
    public static final String TAG = "Reader_FileUtil";

    public static String getFileContent(File file)
    {
        StringBuilder sb = new StringBuilder();
        if (file != null && file.exists() && file.isFile())
        {
            String encode = "GBK";
            try
            {
                encode = FileEncodeUtil.guessFileEncoding(file);
            }
            catch (IOException e)
            {
                Log.e(TAG, "[getFileContent] get file encoding failed.");
                e.printStackTrace();
            }
            FileInputStream fis = null;
            try
            {
                fis = new FileInputStream(file);
                int len = 0;
                byte[] b = new byte[1024 * 8 * 4];
                while ((len = fis.read(b)) != -1)
                {
                    sb.append(new String(b, 0, len, encode));
                }
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (fis != null)
                {
                    try
                    {
                        fis.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sb.toString().trim();
    }

    public static String getFileContent(String path)
    {
        return getFileContent(new File(path));
    }

    /**
     * 简单的判断文件的编码格式
     *
     * @param fileName :file
     * @return 文件编码格式
     * @throws Exception
     */
    public static String getFileEncode(String fileName)
    {
        BufferedInputStream bin = null;
        int p = 0;
        try
        {
            bin = new BufferedInputStream(new FileInputStream(fileName));
            p = (bin.read() << 8) + bin.read();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bin != null)
            {
                try
                {
                    bin.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        String code = null;
        // 其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
        switch (p)
        {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            case 0x5c75:
                code = "ANSI|ASCII";
                break;
            default:
                code = "GBK";
        }
        Log.i(TAG, "[getFileEncode] encode is " + code);
        return code;
    }
}
