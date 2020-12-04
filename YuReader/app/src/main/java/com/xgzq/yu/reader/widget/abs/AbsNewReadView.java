package com.xgzq.yu.reader.widget.abs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.xgzq.yu.reader.model.enums.Position;
import com.xgzq.yu.reader.utils.DistanceUtil;
import com.xgzq.yu.reader.widget.ifs.IControl;
import com.xgzq.yu.reader.widget.ifs.IDrawer;

public abstract class AbsNewReadView extends View {
    public static final String TAG = "XGZQ:NewReadView";

    private IDrawer mDrawer;
    private IControl mControl;

    private Paint mPaint = new Paint();

    public AbsNewReadView(Context context) {
        this(context, null);
    }

    public AbsNewReadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public AbsNewReadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iInit(context, attrs, defStyleAttr);
    }

    private void iInit(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint.setColor(Color.RED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mControl != null) {
            return mControl.onTouchControl(event);
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        super.onDraw(canvas);
        if (mDrawer != null) {
            mDrawer.onDrawContent(canvas);
        }
        canvas.drawLine(0, DistanceUtil.getScreenHeight() * 0.33f, DistanceUtil.getScreenWidth(), DistanceUtil.getScreenHeight() * 0.33f, mPaint);
        canvas.drawLine(0, DistanceUtil.getScreenHeight() * 0.33f * 2, DistanceUtil.getScreenWidth(), DistanceUtil.getScreenHeight() * 0.33f * 2, mPaint);

        canvas.drawLine(DistanceUtil.getScreenWidth() * 0.33f, 0, DistanceUtil.getScreenWidth() * 0.33f, DistanceUtil.getScreenHeight(), mPaint);
        canvas.drawLine(DistanceUtil.getScreenWidth() * 0.33f * 2, 0, DistanceUtil.getScreenWidth() * 0.33f * 2, DistanceUtil.getScreenHeight(), mPaint);
    }

    public void setDrawer(IDrawer drawer) {
        this.mDrawer = drawer;
    }

    public void setControl(IControl control) {
        this.mControl = control;
    }

}
