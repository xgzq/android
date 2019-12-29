package com.xgzq.yu.reader.utils;

import android.util.Log;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {

    private static final String TAG = "XGZQ:FileUtil";


    public static String readFile(String path) {
        if (path == null) {
            throw new NullPointerException("path is null");
        }
        return readFile(new File(path));
    }

    public static byte[] getBytes(char[] chars) {
        byte[] result = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            result[i] = (byte) chars[i];
        }
        return result;
    }

    public static String readFile(File file) {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        return readFile(file, guessFileEncoding(file));
    }


    public static String readFile(File file, String encoding) {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream, encoding);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(fileInputStream);
            closeStream(inputStreamReader);
            closeStream(bufferedReader);
        }
        return sb.toString();
    }


    public static String guessFileEncoding(File file) {
        return guessFileEncoding(file, new nsDetector());
    }


    private static String guessFileEncoding(File file, nsDetector det) {
        // Set an observer...
        // The Notify() will be called when a matching charset is found.
        det.Init(new nsICharsetDetectionObserver() {
            public void Notify(String charset) {
                encoding = charset;
                found = true;
            }
        });
        try {
            return guessFileEncoding(new FileInputStream(file), det);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String encoding = null;
    private static boolean found = false;

    /**
     * 获取文件编码
     *
     * @param is
     * @param det
     * @return
     * @throws IOException
     */
    public static String guessFileEncoding(InputStream is, nsDetector det) throws IOException {
        // Set an observer...
        // The Notify() will be called when a matching charset is found.
        det.Init(new nsICharsetDetectionObserver() {
            public void Notify(String charset) {
                encoding = charset;
                found = true;
            }
        });
        BufferedInputStream imp = new BufferedInputStream(is);
        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = false;

        while ((len = imp.read(buf, 0, buf.length)) != -1) {
            // Check if the stream is only ascii.
            isAscii = det.isAscii(buf, len);
            if (isAscii) {
                break;
            }
            // DoIt if non-ascii and not done yet.
            done = det.DoIt(buf, len, false);
            if (done) {
                break;
            }
        }
        imp.close();
        det.DataEnd();

        if (isAscii) {
            encoding = "ASCII";
            found = true;
        }

        if (!found) {
            String[] prob = det.getProbableCharsets();
            // 这里将可能的字符集组合起来返回
            for (int i = 0; i < prob.length; i++) {
                if (i == 0) {
                    encoding = prob[i];
                } else {
                    encoding += "," + prob[i];
                }
            }

            if (prob.length > 0) {
                // 在没有发现情况下,也可以只取第一个可能的编码,这里返回的是一个可能的序列
                return encoding;
            } else {
                return null;
            }
        }
        Log.i(TAG, "guessFileEncoding => file encoding is " + encoding);
        return encoding;
    }


    public static void closeStream(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
