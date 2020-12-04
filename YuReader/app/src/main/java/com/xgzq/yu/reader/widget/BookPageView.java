package com.xgzq.yu.reader.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.xgzq.yu.reader.model.bean.Chapter;
import com.xgzq.yu.reader.model.util.CachePool;
import com.xgzq.yu.reader.widget.abs.ABookPageView;

public class BookPageView extends ABookPageView {

    public static final int DEFAULT_BEFORE_CACHE_COUNT = 1;
    public static final int DEFAULT_AFTER_CACHE_COUNT = 2;

    /**
     * 缓存章节
     *
     * @param context
     */

    private CachePool<Chapter> mChapterPool;
    private int mCurrentChapterIndex = 0;
    private int mCacheBeforeChapterCount = DEFAULT_BEFORE_CACHE_COUNT;
    private int mCacheAfterChapterCount = DEFAULT_AFTER_CACHE_COUNT;


    public BookPageView(Context context) {
        super(context);
    }

    public BookPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BookPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);

        mChapterPool = new CachePool<>(mCacheBeforeChapterCount, mCacheAfterChapterCount);
    }

    @Override
    protected void onSizeMeasured() {

    }

    @Override
    protected void drawPreviousPageContent(Canvas canvas) {

    }

    @Override
    protected void drawCurrentPageContent(Canvas canvas) {

    }

    @Override
    protected void drawNextPageContent(Canvas canvas) {

    }

    /**
     * 缓存章节数据
     *
     * @param beforeChapterCount 当前章节之前需要缓存的章节数
     * @param afterChapterCount  当前章节之后的需要缓存的章节数
     */
    public void cacheChapters(int beforeChapterCount, int afterChapterCount) {
    }

}
