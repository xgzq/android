package com.xgzq.yu.reader.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.xgzq.yu.reader.widget.abs.ABookPageView;

public class BookPageView extends ABookPageView {
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
    protected void drawCurrentPageContent(Canvas canvas) {

    }

    @Override
    protected void drawNextPageContent(Canvas canvas) {

    }
}
