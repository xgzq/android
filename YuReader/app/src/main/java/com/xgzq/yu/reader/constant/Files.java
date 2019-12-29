package com.xgzq.yu.reader.constant;

import android.os.Environment;
import android.util.Log;

import com.xgzq.yu.reader.App;

import java.io.File;

public class Files {

    public static final String TAG = "XGZQ:Files";

    public static final String DIR_FILES;
    public static final String DIR_PUBLIC_DOWNLOAD;


    static {
        DIR_FILES = App.getApp().getFilesDir().getAbsolutePath();
        Log.d(TAG, "DIR_FILES : " + DIR_FILES);
        DIR_PUBLIC_DOWNLOAD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Log.d(TAG, "DIR_PUBLIC_DOWNLOAD : " + DIR_PUBLIC_DOWNLOAD);
        initDirs();
    }

    private static void initDirs() {
        File booksDir = new File(DIR_FILES, NAME_OF_DIR_BOOKS);
        if (!booksDir.exists()) {
            booksDir.mkdirs();
        }
    }

    public static final String NAME_OF_DIR_BOOKS = "books";
    //    public static final String DIR_BOOKS = DIR_FILES + File.separator + NAME_OF_DIR_BOOKS;
    public static final String DIR_BOOKS = DIR_PUBLIC_DOWNLOAD;
}
