package com.xgzq.yu.reader.model.util;

import com.xgzq.yu.reader.model.bean.Chapter;

public class ChapterPool {

    public static final int DEFAULT_BEFORE_COUNT = 3;
    public static final int DEFAULT_AFTER_COUNT = 6;

    private CachePool<Chapter> mChapterPool;

    public ChapterPool() {
        mChapterPool = new CachePool<>(DEFAULT_BEFORE_COUNT, DEFAULT_AFTER_COUNT);
    }

    public Chapter getNext(){
        return mChapterPool.next();
    }
}
