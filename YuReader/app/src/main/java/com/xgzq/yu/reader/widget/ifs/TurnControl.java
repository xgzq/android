package com.xgzq.yu.reader.widget.ifs;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.xgzq.yu.reader.model.enums.Position;
import com.xgzq.yu.reader.utils.DistanceUtil;

import java.util.ArrayList;

public class TurnControl implements IControl, GestureDetector.OnGestureListener {

    private static final String TAG = "XGZQ:TurnControl";

    private Context mViewContext;
    private GestureDetector mGestureDetector;

    private IAction mTurnAction;

    private PointF mTouch, mCorner, g, e, h, c, j, b, k, d, i;

    public TurnControl(Context context, IAction action) {
        mViewContext = context;
        mTurnAction = action;
        mGestureDetector = new GestureDetector(context, this);
    }

    private boolean onUp(MotionEvent e) {
        Log.d(TAG, "onUp");
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown");
        if (mTouch == null) {
            mTouch = new PointF(e.getX(), e.getY());
        } else {
            mTouch.x = e.getX();
            mTouch.y = e.getY();
        }
        calculateCornerPointByTouch(e);
        calculatePointCoordinates();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG, "onShowPress");
    }

    /**
     * 只在单击的时候松开才会有此事件触发
     *
     * @param e
     * @return
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp");
        return onUp(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, "onScroll: ");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG, "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "onFling");
        return true;
    }

    private MotionEvent mLastEvent;

    @Override
    public boolean onTouchControl(MotionEvent event) {
        Log.d(TAG, "onTouchControl: " + event.getX() + ", " + event.getY() + ", " + event.getAction());
        boolean isTaken = mGestureDetector.onTouchEvent(event);
        Log.d(TAG, "isTaken: " + isTaken);
        if (!isTaken && event.getAction() == MotionEvent.ACTION_UP) {
            // 在当onSingleTapUp触发的时候，isTaken是true所以不同担心此处会再次触发onUp
            return onUp(event);
        }
        this.mLastEvent = event;
        return isTaken;
    }

    @Override
    public Position calculateTouchPosition(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float oneWidth = DistanceUtil.getScreenWidth() * 0.33f;
        float oneHeight = DistanceUtil.getScreenHeight() * 0.33f;
        if (0 <= x && x <= oneWidth) {
            if (0 <= y && y <= oneHeight) {
                return Position.LeftTop;
            }
            if (oneHeight < y && y < oneHeight * 2) {
                return Position.Left;
            }
            if (oneHeight * 2 <= y && y <= DistanceUtil.getScreenHeight()) {
                return Position.LeftBottom;
            }
        } else if (oneWidth < x && x < oneWidth * 2) {
            if (0 <= y && y <= oneHeight) {
                return Position.Top;
            }
            if (oneHeight < y && y < oneHeight * 2) {
                return Position.Center;
            }
            if (oneHeight * 2 <= y && y <= DistanceUtil.getScreenHeight()) {
                return Position.Bottom;
            }
        } else if (2 * oneWidth < x && x < DistanceUtil.getScreenWidth()) {
            if (0 <= y && y <= oneHeight) {
                return Position.RightTop;
            }
            if (oneHeight < y && y < oneHeight * 2) {
                return Position.Right;
            }
            if (oneHeight * 2 <= y && y <= DistanceUtil.getScreenHeight()) {
                return Position.RightBottom;
            }
        }
        return Position.Out;
    }

    private void calculateCornerPointByTouch(MotionEvent event) {
        Position position = calculateTouchPosition(event);
        if (mNextPositionList.contains(position)) {
            if (mCorner == null) {
                mCorner = new PointF(DistanceUtil.getScreenWidth(), DistanceUtil.getScreenHeight());
            } else {
                mCorner.x = DistanceUtil.getScreenWidth();
                mCorner.y = DistanceUtil.getScreenHeight();
            }
        } else if (mPrevPositionList.contains(position)) {
            if (mCorner == null) {
                mCorner = new PointF(DistanceUtil.getScreenWidth(), 0);
            } else {
                mCorner.x = DistanceUtil.getScreenWidth();
                mCorner.y = 0;
            }
        } else {
            if (mCorner == null) {
                mCorner = new PointF(-1, -1);
            } else {
                mCorner.x = -1;
                mCorner.y = -1;
            }
        }
    }

    private void calculatePointCoordinates() {
        if (mTouch != null && mCorner != null) {
            g.x = (mTouch.x + mCorner.x) / 2;
            g.y = (mTouch.y + mCorner.y) / 2;

            e.x = g.x - (mCorner.y - g.y) * (mCorner.y - g.y) / (mCorner.x - g.x);
            e.y = mCorner.y;

            h.x = mCorner.x;
            h.y = g.y - (mCorner.x - g.x) * (mCorner.x - g.x) / (mCorner.y - g.y);

            c.x = e.x - (mCorner.x - e.x) / 2;
            c.y = mCorner.y;

            j.x = mCorner.x;
            j.y = h.y - (mCorner.y - h.y) / 2;

            b = calculateLineIntersectsPoint(mTouch, e, c, j);
            k = calculateLineIntersectsPoint(mTouch, h, c, j);

            d.x = (c.x + 2 * e.x + b.x) / 4;
            d.y = (2 * e.y + c.y + b.y) / 4;

            i.x = (j.x + 2 * h.x + k.x) / 4;
            i.y = (2 * h.y + j.y + k.y) / 4;

//            float lA = mTouch.y - e.y;
//            float lB = e.x - mTouch.x;
//            float lC = mTouch.x * e.y - e.x * mTouch.y;
//            mCurrentLeftShadowWidth = Math.abs((lA * d.x + lB * d.y + lC) / (float) Math.hypot(lA, lB));

//            float rA = mTouch.y - h.y;
//            float rB = h.x - mTouch.x;
//            float rC = mTouch.x * h.y - h.x * mTouch.y;
//            mCurrentRightShadowWidth = Math.abs((rA * i.x + rB * i.y + rC) / (float) Math.hypot(rA, rB));
        }
    }

    private PointF calculateLineIntersectsPoint(PointF xLineStart, PointF xLineEnd, PointF yLineStart, PointF yLineEnd) {
        float x1 = xLineStart.x;
        float y1 = xLineStart.y;
        float x2 = xLineEnd.x;
        float y2 = xLineEnd.y;
        float x3 = yLineStart.x;
        float y3 = yLineStart.y;
        float x4 = yLineEnd.x;
        float y4 = yLineEnd.y;

        float pointX = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
        float pointY = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));

        return new PointF(pointX, pointY);
    }

    private ArrayList<Position> mNextPositionList = new ArrayList<>(4);
    private ArrayList<Position> mPrevPositionList = new ArrayList<>(4);
    private ArrayList<Position> mCenterPositionList = new ArrayList<>(4);

    {
        mNextPositionList.add(Position.RightTop);
        mNextPositionList.add(Position.Right);
        mNextPositionList.add(Position.RightBottom);
        mNextPositionList.add(Position.Bottom);

        mPrevPositionList.add(Position.LeftBottom);
        mPrevPositionList.add(Position.Left);
        mPrevPositionList.add(Position.LeftTop);
        mPrevPositionList.add(Position.Top);

        mCenterPositionList.add(Position.Center);

        g = new PointF();
        e = new PointF();
        h = new PointF();
        c = new PointF();
        j = new PointF();
        b = new PointF();
        k = new PointF();
        d = new PointF();
        i = new PointF();
    }
}
