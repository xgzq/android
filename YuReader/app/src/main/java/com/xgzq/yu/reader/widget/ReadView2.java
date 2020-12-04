package com.xgzq.yu.reader.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.xgzq.yu.reader.App;
import com.xgzq.yu.reader.R;
import com.xgzq.yu.reader.model.bean.Book;
import com.xgzq.yu.reader.model.bean.Chapter;
import com.xgzq.yu.reader.model.bean.ChapterLine;
import com.xgzq.yu.reader.model.model.BookModel;
import com.xgzq.yu.reader.utils.ChapterUtil;
import com.xgzq.yu.reader.utils.DistanceUtil;
import com.xgzq.yu.reader.widget.abs.ABookPageView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ReadView2 extends ABookPageView implements ABookPageView.OnReadListener {

    public interface ReadCallback {
        void onStartLoading();

        void onStopLoading();

        void onNextChapter();

        void onPreviousChapter();
    }

    private static final String TAG = "XGZQ:ReadView";

    public static final int FONT_TITLE_MORE_THAN_SIZE = 4;
    public static final int FONT_SIZE = 16;
    public static final int MIN_FONT_SIZE = 10;
    public static final int MAX_FONT_SIZE = 36;

    public static final int LINE_SPACING = 12;
    public static final int MIN_LINE_SPACING = 4;
    public static final int MAX_LINE_SPACING = 48;
    public static final int COLOR_TITLE = App.getApp().getResources().getColor(R.color.read_title);
    public static final int COLOR_TEXT = App.getApp().getResources().getColor(R.color.read_text);
    public static final int COLOR_BACKGROUND = App.getApp().getResources().getColor(R.color.read_background);

    public static final float PERCENT_SETTINGS_WIDTH = 0.3f;
    public static final float PERCENT_SETTING_HEIGHT = 0.7f;


    public static final int OFFSET_X = 12;
    public static final int OFFSET_Y = 4;


    // 阅读配置
    private BookModel.ReadConfig mReadConfig;
    // 当前书籍
    private Book mBook;

    // 当前阅读点距章节开始点的页面偏移量
    private long mReadOffsetPage;

    // 行间距dp
    private int mLineSpacing;
    // 行间距px
    private float mLineSpacingPx;
    // 字体大小dp
    private int mTextFontSize;
    // 字体大小px
    private float mTextFontSizePx;
    private int mStartX;
    // X轴绘制起点px
    private float mStartXPx;
    // Y轴绘制起点px
    private int mStartY;
    private float mStartYPx;
    // Y轴绘制偏移量，一行一行加
    private float mOffsetYPx;

    /**
     * 合并多个换行符（当多个换行符在连起来的时候，只作用一个换行）
     */
    private boolean mergeMultipleNewLines = true;

    private boolean mAsyncSegmentPage = true;

    private boolean mShowLoadingWhenAsyncSegmentPage = true;


    private TextPaint mTitlePaint;
    private Rect mTitleRect;

    private TextPaint mTextPaint;
    private Rect mTextRect;

    // 上一章节
    private Chapter mPreviousChapter;
    // 当前章节
    private Chapter mCurrentChapter;
    // 下一章节
    private Chapter mNextChapter;

    /**
     * 当前章节分页分行数据
     */
    private ChapterLine mCurrentChapterLine;

    private ChapterLine mPreviousChapterLine;

    private ChapterLine mNextChapterLine;

    /**
     * 阅读回调
     */
    private ReadCallback mReadCallback;


    // TODO: 前后章节预加载

    public ReadView2(Context context) {
        this(context, null);
    }

    public ReadView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ReadView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);
        mTextFontSize = FONT_SIZE;
        mTextFontSizePx = DistanceUtil.sp2px(mTextFontSize);

        setBackgroundColor(COLOR_BACKGROUND);
        mTitlePaint = new TextPaint();
        mTitlePaint.setTextSize(DistanceUtil.sp2px(mTextFontSize + FONT_TITLE_MORE_THAN_SIZE));
        mTitlePaint.setColor(COLOR_TITLE);
        mTitlePaint.setTextAlign(Paint.Align.LEFT);
        mTitlePaint.setAntiAlias(true);
        mTitleRect = new Rect();

        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(mTextFontSizePx);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(COLOR_TEXT);
        mTextPaint.setAntiAlias(true);
        mTextRect = new Rect();

        mReadOffsetPage = 0;

        mStartX = OFFSET_X;
        mStartY = OFFSET_Y;

        mStartXPx = DistanceUtil.dp2px(mStartX);
        mStartYPx = DistanceUtil.dp2px(mStartY);

        mOffsetYPx = mStartYPx;

        mLineSpacing = LINE_SPACING;
        mLineSpacingPx = DistanceUtil.dp2px(mLineSpacing);

        mCurrentChapterLine = new ChapterLine();
        setOnReadListener(this);
    }

    private long startTime = 0;

    private Paint mPreviousPaint = new Paint();
    private Paint mNextPaint = new Paint();
    private Paint mSettingPaint = new Paint();


    @Override
    protected void drawPreviousPageContent(Canvas canvas) {
        // 初始化绘制的Y轴起点，每次绘制都从顶部开始
        mOffsetYPx = mStartYPx - mTextPaint.ascent();
        if (mReadOffsetPage <= 0) {
            // 上一章可能为null，需要判断
            if (mBook != null && mPreviousChapter != null && mPreviousChapterLine != null) {
                final int pageCount = mPreviousChapterLine.pageCount();
                drawTopTitle(canvas, pageCount == 1 ? mBook.getName() : mPreviousChapter.getTitle(), pageCount, pageCount);
                if (pageCount == 1) {
                    drawChapterTitle(canvas, mPreviousChapter.getTitle());
                }
            }
            if (mPreviousChapterLine != null) {
                drawChapterContent(canvas, mPreviousChapterLine.get(mPreviousChapterLine.pageCount() - 1));
            }
        } else {
            // 如果当前章节为null，则无法渲染
            if (mCurrentChapter == null) {
                Log.d(TAG, "mCurrentChapter is null!");
                return;
            }
            final int previousReadOffsetPage = (int) (mReadOffsetPage - 1);
            drawTopTitle(canvas, previousReadOffsetPage);
            // 如果上一页是第一页，则绘制标题
            if (previousReadOffsetPage == 0) {
                drawChapterTitle(canvas);
            }
            drawChapterContent(canvas, previousReadOffsetPage);
        }
        Log.d(TAG, "drawPreviousPageContent time: " + (System.currentTimeMillis() - startTime));
    }

    @Override
    protected void drawCurrentPageContent(Canvas canvas) {
        // 如果当前章节为null，则无法渲染
        if (mCurrentChapter == null) {
            Log.d(TAG, "mCurrentChapter is null!");
            return;
        }
        // 初始化绘制的Y轴起点，每次绘制都从顶部开始
        mOffsetYPx = mStartYPx - mTextPaint.ascent();
        if (mBook != null) {
            drawTopTitle(canvas);
        }
        // 当前章节第一页显示章节名，且只有第一页才会显示
        if (mReadOffsetPage <= 0) {
            drawChapterTitle(canvas);
        }
        drawChapterContent(canvas, (int) mReadOffsetPage);
        Log.d(TAG, "drawCurrentPageContent time: " + (System.currentTimeMillis() - startTime));
    }

    @Override
    protected void drawNextPageContent(Canvas canvas) {
        // 初始化绘制的Y轴起点，每次绘制都从顶部开始
        mOffsetYPx = mStartYPx - mTextPaint.ascent();
        // 当前页是当前章节的最后一页
        if (mReadOffsetPage == mCurrentChapterLine.pageCount() - 1) {
            // 下一章节第一页
            if (mNextChapter != null && mNextChapterLine != null) {
                drawTopTitle(canvas, mBook.getName(), 1, mNextChapterLine.pageCount());
                drawChapterTitle(canvas, mNextChapter.getTitle());
                drawChapterContent(canvas, mNextChapterLine.get(0));
            }
        } else {
            // 如果当前章节为null，则无法渲染
            if (mCurrentChapter == null) {
                Log.d(TAG, "mCurrentChapter is null!");
                return;
            }
            final int nextReadOffsetPage = (int) (mReadOffsetPage + 1);
            drawTopTitle(canvas, nextReadOffsetPage);
            drawChapterContent(canvas, nextReadOffsetPage);
        }
        Log.d(TAG, "drawNextPageContent time: " + (System.currentTimeMillis() - startTime));
    }

    @Override
    protected void onSizeMeasured() {

    }

    /**
     * 绘制顶部标题：如果是当前章节第一页，则显示书籍名称；如果不是第一页，则显示当前章节标题（普通字体）
     * 当前方法至关绘制，不管条件（实际上只有在mBook和mCurrentChapter都不为null的时候绘制）
     *
     * @param canvas
     */
    private void drawTopTitle(Canvas canvas) {
        // 当前章节在第一页就显示书籍名称，不是第一页就显示章节名
        drawTopTitle(canvas, (int) mReadOffsetPage);
//        String title = mReadOffsetPage <= 0 ? mBook.getName() : mCurrentChapter.getTitle();
//        drawTopTitle(canvas, title, mReadOffsetPage + 1, mCurrentChapterLine.pageCount());
//        canvas.drawText(title, mStartXPx, mOffsetYPx, mTextPaint);
//        String pageProcess = (mReadOffsetPage + 1) + "/" + mCurrentChapterLine.pageCount();
//        canvas.drawText(pageProcess, DistanceUtil.getScreenWidth() - mTextPaint.measureText(pageProcess) - mStartXPx, mOffsetYPx, mTextPaint);
//        mOffsetYPx += mLineSpacingPx / 2;
    }

    private void drawTopTitle(Canvas canvas, int pageOffset) {
        String title = pageOffset <= 0 ? mBook.getName() : mCurrentChapter.getTitle();
        drawTopTitle(canvas, title, pageOffset + 1, mCurrentChapterLine.pageCount());
    }

    private void drawTopTitle(Canvas canvas, String title, int currentPage, int pageCount) {
        canvas.drawText(title, mStartXPx, mOffsetYPx, mTextPaint);
        String pageProcess = currentPage + "/" + pageCount;
        canvas.drawText(pageProcess, DistanceUtil.getScreenWidth() - mTextPaint.measureText(pageProcess) - mStartXPx, mOffsetYPx, mTextPaint);
        mOffsetYPx += mLineSpacingPx / 2;
    }

    /**
     * 绘制标题（大写加黑的）
     *
     * @param canvas
     */
    private void drawChapterTitle(Canvas canvas) {
        drawChapterTitle(canvas, mCurrentChapter.getTitle());
    }

    private void drawChapterTitle(Canvas canvas, String title) {
        mOffsetYPx += mLineSpacingPx - mTitlePaint.ascent();
        canvas.drawText(title, mStartXPx, mOffsetYPx, mTitlePaint);
        mOffsetYPx += mLineSpacingPx;
    }

    /**
     * 绘制内容正文
     *
     * @param canvas
     */
    private void drawChapterContent(Canvas canvas, int currentPage) {
        drawChapterContent(canvas, mCurrentChapterLine.get(currentPage));
//        for (int i = 0; i < mCurrentChapterLine.lineCount(currentPage); i++) {
//            ChapterLine.TextLine line = mCurrentChapterLine.getLine(currentPage, i);
//            if (line == null) continue;
//            canvas.drawText(line.getContent(), mStartXPx, line.getOffsetY(), mTextPaint);
//        }
    }

    private void drawChapterContent(Canvas canvas, ArrayList<ChapterLine.TextLine> lines) {
        for (ChapterLine.TextLine line : lines) {
            if (line == null) continue;
            canvas.drawText(line.getContent(), mStartXPx, line.getOffsetY(), mTextPaint);
        }
    }

    @SuppressLint("CheckResult")
    private void segmentPageAsync(final Chapter chapter, Consumer<ChapterLine> consumer) {
        Observable.create(new ObservableOnSubscribe<ChapterLine>() {
            @Override
            public void subscribe(ObservableEmitter<ChapterLine> emitter) throws Exception {
                emitter.onNext(segmentPage(chapter));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }

    /**
     * 给内容分页：在设置章节、改变字体大小、改变行间距、改变边距间距的时候都需要调用，需要重新分页
     * 在改变字体后调用onFontSizeChanged后再调用此方法
     */
    private ChapterLine segmentPage(Chapter chapter) {
        // 计时
        long startTime = System.currentTimeMillis();

        if (chapter == null) return null;
        // 保存数据
        final ChapterLine chapterLine = new ChapterLine();
        // 保存文本
        final StringBuilder contentBuilder = new StringBuilder(chapter.getContent());
        // 去掉顶部padding和底部padding
        final float topAndBottomSpacingHeight = mStartYPx * 2 + mLineSpacingPx - mTextPaint.ascent();
        // 最大绘制宽度
        final float maxWidth = DistanceUtil.getScreenWidth() - mStartXPx * 2;
        // 正文文本的行高
        final float lineHeight = mLineSpacingPx + mTextRect.height();

        // 分页偏移量
        int pageOffset = 0;
        // 绘制行Y轴偏移量
        float offsetYPx;
        // 每行的正文内容
        String lineContent;

        while (contentBuilder.length() > 0) {
            // 起始位置
            offsetYPx = mStartYPx - mTextPaint.ascent();
            // 小标题间隔
            offsetYPx += mLineSpacingPx / 2;
            // 只有在第一页的时候显示章节大标题
            if (pageOffset == 0) {
                // 标题位移
                offsetYPx += mLineSpacingPx - mTitlePaint.ascent();
                // 标题间隔
                offsetYPx += mLineSpacingPx;
            }
            while (offsetYPx <= DistanceUtil.getScreenHeight() - topAndBottomSpacingHeight) {
                int currentLineTextSize = mTextPaint.breakText(contentBuilder.toString(), true, maxWidth, null);
                // currentLineTextSize为字符数，从0到下标为currentLineTextSize（不包括下标为currentLineTextSize的字符）的字符数量正好是currentLineTextSize个
                lineContent = contentBuilder.substring(0, currentLineTextSize);
                if (lineContent.contains("\n") || lineContent.contains("\r")) {
                    int index = lineContent.contains("\n") ? lineContent.indexOf("\n") : lineContent.indexOf("\r");
                    if (index == 0) {
                        contentBuilder.delete(0, 1);
                        // 当没有合并多个换行符的时候，增加一行空字符串
                        if (!mergeMultipleNewLines) {
                            offsetYPx += lineHeight;
                        }
                        chapterLine.addTextLine(pageOffset, offsetYPx, "");
                    } else {
                        offsetYPx += lineHeight;
                        contentBuilder.delete(0, index + 1);

                        chapterLine.addTextLine(pageOffset, offsetYPx, lineContent.substring(0, index + 1));
                    }
                } else {
                    offsetYPx += lineHeight;
                    contentBuilder.delete(0, currentLineTextSize);

                    chapterLine.addTextLine(pageOffset, offsetYPx, lineContent);
                }
            }
            pageOffset++;
        }
        Log.d(TAG, "segment page time: " + (System.currentTimeMillis() - startTime));
        return chapterLine;
    }

    private void onFontSizeChanged() {
        String text = mCurrentChapter != null ? mCurrentChapter.getTitle() : ChapterUtil.MATCH_CHAPTER_TEXT;
        mTitlePaint.getTextBounds(text, 0, text.length() - 1, mTitleRect);
        mTextPaint.getTextBounds(text, 0, text.length() - 1, mTextRect);
    }

    private void resetPageOffset(final boolean fromStart) {
        if (fromStart) {
            mReadOffsetPage = 0;
        } else {
            if (mCurrentChapterLine == null) {
                throw new NullPointerException("resetPageOffset => mCurrentChapterLine is null!");
            }
            mReadOffsetPage = mCurrentChapterLine.pageCount() - 1;
        }
    }

    private void segmentPageAndDraw(final boolean async) {
        segmentPageAndDraw(async, false, false);
    }

    /**
     * 分割章节为一行一行的数据并更新界面<br/>
     * 有些操作可以触发重新分割页面和重绘，但是不需要重置页面偏移量<br/>
     * 有些操作不仅要触发重新分割，也要确定是从头还是从尾显示：<br/>
     * - 如当前章节为第一页，此时点击上一页到上一章节的结束位置时:resetPageOffset=true, fromStart=false
     * - 如当前章节为第最后一页，此时点击下一页到下一章节的起始位置时:resetPageOffset=true, fromStart=true
     *
     * @param async           是否异步分割
     * @param resetPageOffset 是否重置阅读页面偏移量
     * @param fromStart       是否从头开始，只有在resetPageOffset为true时起作用
     */
    private void segmentPageAndDraw(final boolean async, final boolean resetPageOffset, final boolean fromStart) {
        // 先更新字体数据
        onFontSizeChanged();
        // 同步
        if (!async) {
            mCurrentChapterLine.clearAll();
            mCurrentChapterLine = segmentPage(mCurrentChapter);
            if (mReadCallback != null) {
                mReadCallback.onStopLoading();
            }
            // 如果需要重置阅读页面偏移量（有些设置，如果字体加减，行距加减是不用重置的）
            if (resetPageOffset) {
                resetPageOffset(fromStart);
            }
            invalidate();
        } else {
            Observable.create(new ObservableOnSubscribe<ChapterLine>() {
                @Override
                public void subscribe(ObservableEmitter<ChapterLine> emitter) throws Exception {
                    emitter.onNext(segmentPage(mCurrentChapter));
                    emitter.onComplete();
                }
            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ChapterLine>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            if (mReadCallback != null && mShowLoadingWhenAsyncSegmentPage) {
                                mReadCallback.onStartLoading();
                            }
                        }

                        @Override
                        public void onNext(ChapterLine chapterLine) {
                            if (chapterLine == null) {
                                return;
                            }
                            mCurrentChapterLine.clearAll();
                            mCurrentChapterLine = chapterLine;
                            if (resetPageOffset) {
                                resetPageOffset(fromStart);
                            }
                            invalidate();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mReadCallback != null) {
                                mReadCallback.onStopLoading();
                                // TODO: 展示错误页面
                            }
                        }

                        @Override
                        public void onComplete() {
                            if (mReadCallback != null) {
                                mReadCallback.onStopLoading();
                            }
                        }
                    });
        }
    }

    public void setReadCallback(ReadCallback callback) {
        mReadCallback = callback;
    }

    public void setBook(Book book) {
        mBook = book;
    }

    public void setPreviousChapter(Chapter chapter) {
        mPreviousChapter = chapter;
        segmentPageAsync(chapter, new Consumer<ChapterLine>() {
            @Override
            public void accept(ChapterLine chapterLine) throws Exception {
                mPreviousChapterLine = chapterLine;
            }
        });
    }

    public void setChapter(Chapter chapter, boolean fromStart) {
        mCurrentChapter = chapter;
        segmentPageAndDraw(mAsyncSegmentPage, true, fromStart);
    }

    public void setNextChapter(Chapter chapter) {
        mNextChapter = chapter;
        segmentPageAsync(chapter, new Consumer<ChapterLine>() {
            @Override
            public void accept(ChapterLine chapterLine) throws Exception {
                mNextChapterLine = chapterLine;
            }
        });
    }

    public void setReadConfig(BookModel.ReadConfig readConfig) {
        mReadConfig = readConfig;
        setBackgroundColor(mReadConfig.getBackgroundColor());

        mTextFontSize = mReadConfig.getFontSize();
        mTextFontSizePx = DistanceUtil.sp2px(mTextFontSize);
        mTextPaint.setColor(mReadConfig.getFontColor());
        mTextPaint.setTextSize(mTextFontSizePx);
        mTitlePaint.setTextSize(DistanceUtil.sp2px(mTextFontSize + FONT_TITLE_MORE_THAN_SIZE));

        mLineSpacing = mReadConfig.getLineSpacing();
        mLineSpacingPx = DistanceUtil.dp2px(mLineSpacing);

        mReadOffsetPage = mReadConfig.getOffset();

        segmentPageAndDraw(mAsyncSegmentPage);
    }


    public void subFontSize() {
        if (mTextFontSize <= MIN_FONT_SIZE) return;
        mTextFontSize -= 1;
        mTextFontSizePx = DistanceUtil.sp2px(mTextFontSize);

        mTitlePaint.setTextSize(DistanceUtil.sp2px(mTextFontSize + FONT_TITLE_MORE_THAN_SIZE));
        mTextPaint.setTextSize(mTextFontSizePx);

        if (mTextFontSize <= MIN_FONT_SIZE) {
            // TODO: disabled sub font button
            // Toast.makeText(getContext(), "字号不能再小了，再小就看不见啦！", Toast.LENGTH_SHORT).show();
        }
        segmentPageAndDraw(mAsyncSegmentPage);
    }

    public void addFontSize() {
        if (mTextFontSize >= MAX_FONT_SIZE) return;
        mTextFontSize += 1;
        mTextFontSizePx = DistanceUtil.sp2px(mTextFontSize);

        mTitlePaint.setTextSize(DistanceUtil.sp2px(mTextFontSize + FONT_TITLE_MORE_THAN_SIZE));
        mTextPaint.setTextSize(mTextFontSizePx);

        if (mTextFontSize >= MAX_FONT_SIZE) {
            // TODO: disabled add font button
        }
        segmentPageAndDraw(mAsyncSegmentPage);
    }

    public void subLineSpacing() {
        if (mLineSpacing <= MIN_LINE_SPACING) return;
        mLineSpacing -= 1;
        mLineSpacingPx = DistanceUtil.sp2px(mLineSpacing);

        if (mLineSpacing <= MIN_LINE_SPACING) {
            // TODO: disabled sub line spacing button
        }
        segmentPageAndDraw(mAsyncSegmentPage);
    }

    public void addLineSpacing() {
        if (mLineSpacing >= MAX_LINE_SPACING) return;
        mLineSpacing += 1;
        mLineSpacingPx = DistanceUtil.sp2px(mLineSpacing);

        if (mLineSpacing >= MAX_LINE_SPACING) {
            // TODO: disabled add line spacing button
        }
        segmentPageAndDraw(mAsyncSegmentPage);
    }

    public void subPaddingX() {
        mStartX -= 1;
        mStartXPx = DistanceUtil.dp2px(mStartX);
        segmentPageAndDraw(mAsyncSegmentPage);
    }

    public void addPaddingX() {
        mStartX += 1;
        mStartXPx = DistanceUtil.dp2px(mStartX);
        segmentPageAndDraw(mAsyncSegmentPage);
    }

    public void subPaddingY() {
        mStartY -= 1;
        mStartYPx = DistanceUtil.dp2px(mStartY);
        segmentPageAndDraw(mAsyncSegmentPage);
    }

    public void addPaddingY() {
        mStartY += 1;
        mStartYPx = DistanceUtil.dp2px(mStartY);
        segmentPageAndDraw(mAsyncSegmentPage);
    }

    public void nextPage() {
        if (mReadOffsetPage + 1 >= mCurrentChapterLine.pageCount()) {
            if (mReadCallback != null) {
                mReadOffsetPage = 0;
                mReadCallback.onNextChapter();
            }
            return;
        }
        mReadOffsetPage += 1;
        invalidate();
    }

    public void previousPage() {
        if (mReadOffsetPage <= 0) {
            if (mReadCallback != null) {
                mReadOffsetPage = 0;
                mReadCallback.onPreviousChapter();
            }
            return;
        }
        mReadOffsetPage -= 1;
        invalidate();
    }

    @Override
    public void onNextStart() {

    }

    @Override
    public void onNextEnd() {
        Log.d(TAG, "onNextEnd");
        nextPage();
    }

    @Override
    public void onNextCancel() {

    }

    @Override
    public void onPreviousStart() {

    }

    @Override
    public void onPreviousEnd() {
        previousPage();
    }

    @Override
    public void onPreviousCancel() {

    }

    @Override
    public void onMenu() {

    }
}
