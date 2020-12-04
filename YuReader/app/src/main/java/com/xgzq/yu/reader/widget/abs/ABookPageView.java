package com.xgzq.yu.reader.widget.abs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import com.xgzq.yu.reader.App;
import com.xgzq.yu.reader.R;
import com.xgzq.yu.reader.model.enums.Orientation;
import com.xgzq.yu.reader.utils.DistanceUtil;

public abstract class ABookPageView extends View implements GestureDetector.OnGestureListener {

    private static final String TAG = "XGZQ:ABookPageView";

    /**
     * 单击事件最小间隔时间
     */
    public static final long MIN_CLICK_TIME_INTERVAL = 300;

    /**
     * 翻页动画时长
     */
    public static final int TURN_PAGE_ANIMATION_TIME = 300;

    /**
     * 翻页最小移动距离
     */
    public static final float MIN_TURN_PAGE_DISTANCE = DistanceUtil.dp2px(100);

    public static final int DEFAULT_CURRENT_BACKGROUND_COLOR = App.getApp().getResources().getColor(R.color.read_current_background);
    public static final int DEFAULT_BG_BACKGROUND_COLOR = App.getApp().getResources().getColor(R.color.read_bg_background);
    public static final int DEFAULT_NEXT_BACKGROUND_COLOR = App.getApp().getResources().getColor(R.color.read_next_background);
    public static final int DEFAULT_PREVIOUS_BACKGROUND_COLOR = Color.RED;

    private PointF mTouchPoint, mVertex;
    private PointF g, e, h, c, j, b, k, d, i;

    private Paint mCurrentAreaPaint;
    private Path mCurrentAreaPath;

    private Paint mNextAreaPaint;
    private Path mBgAreaPath;

    private Paint mBgAreaPaint;
    private Path mNextAreaPath;

    protected int mViewWidth;
    protected int mViewHeight;

    private float mCurrentLeftShadowWidth = 0f;
    private float mCurrentRightShadowWidth = 0f;

    // 缓存bitmap
    private Bitmap mAreaBitmap;
    private Canvas mAreaBitmapCanvas;

    protected OnReadListener mOnReadListener;
    protected Scroller mScroller;
    private GestureDetector mGestureDetector;
    private Orientation mOrientation;
    private Matrix mMatrix;

    private long mLastTouchTime = 0;


    //设置翻转和旋转矩阵
    private float[] mMatrixArray = {0, 0, 0, 0, 0, 0, 0, 0, 1.0f};

    protected int mCurrentBackgroundColor;
    protected int mBgBackgroundColor;
    protected int mPreviousBackgroundColor;
    protected int mNextBackgroundColor;


    public ABookPageView(Context context) {
        this(context, null);
    }

    public ABookPageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ABookPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ABookPageView);
            mCurrentBackgroundColor = typedArray.getColor(R.styleable.ABookPageView_currentBackgroundColor, DEFAULT_CURRENT_BACKGROUND_COLOR);
            mBgBackgroundColor = typedArray.getColor(R.styleable.ABookPageView_bgBackgroundColor, DEFAULT_BG_BACKGROUND_COLOR);
            mNextBackgroundColor = typedArray.getColor(R.styleable.ABookPageView_nextBackgroundColor, DEFAULT_NEXT_BACKGROUND_COLOR);
            mPreviousBackgroundColor = typedArray.getColor(R.styleable.ABookPageView_previousBackgroundColor, DEFAULT_PREVIOUS_BACKGROUND_COLOR);
            typedArray.recycle();
        }

        mTouchPoint = new PointF();
        mVertex = new PointF();

        g = new PointF();
        e = new PointF();
        h = new PointF();
        c = new PointF();
        j = new PointF();
        b = new PointF();
        k = new PointF();
        d = new PointF();
        i = new PointF();

        mCurrentAreaPaint = new Paint();
        mCurrentAreaPaint.setColor(mCurrentBackgroundColor);
        mCurrentAreaPaint.setAntiAlias(true);//设置抗锯齿
        mCurrentAreaPath = new Path();

        mNextAreaPaint = new Paint();
        mNextAreaPaint.setColor(mNextBackgroundColor);
        mNextAreaPaint.setAntiAlias(true);//设置抗锯齿
        mNextAreaPath = new Path();

        mBgAreaPaint = new Paint();
        mBgAreaPaint.setColor(mBgBackgroundColor);
        mBgAreaPaint.setAntiAlias(true);//设置抗锯齿
        mBgAreaPath = new Path();

        mGestureDetector = new GestureDetector(getContext(), this);
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
        mMatrix = new Matrix();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(getMeasuredHeight(), heightMeasureSpec);
        int width = measureSize(getMeasuredWidth(), widthMeasureSpec);
        setMeasuredDimension(width, height);

        mViewWidth = width;
        mViewHeight = height;

        mTouchPoint.x = -1;
        mTouchPoint.y = -1;

        mVertex.x = mViewWidth;
        mVertex.y = mViewHeight;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        calculatePointCoordinates(mTouchPoint, mVertex);
        mAreaBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);
        mAreaBitmapCanvas = new Canvas(mAreaBitmap);

        onSizeMeasured();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTouchPoint.x == -1 && mTouchPoint.y == -1) {
            drawCurrentAreaContent(mAreaBitmapCanvas, getDefaultPath(), mCurrentAreaPaint);
        } else {
            Path currentAreaPath;
            if (mOrientation == Orientation.RightTop) {
                currentAreaPath = getCurrentAreaPathInRightTop();
                drawCurrentAreaContent(mAreaBitmapCanvas, currentAreaPath, mCurrentAreaPaint);
                drawBgAreaContent(mAreaBitmapCanvas, currentAreaPath, mBgAreaPaint);
                drawNextAreaContent(mAreaBitmapCanvas, currentAreaPath, mNextAreaPaint);
            } else if (mOrientation == Orientation.Left) {
                currentAreaPath = getCurrentAreaPathInRightBottom();
                drawCurrentAreaContent(mAreaBitmapCanvas, currentAreaPath, mCurrentAreaPaint, true);
                drawBgAreaContent(mAreaBitmapCanvas, currentAreaPath, mBgAreaPaint);
                drawNextAreaContent(mAreaBitmapCanvas, currentAreaPath, mNextAreaPaint);
            } else {
                currentAreaPath = getCurrentAreaPathInRightBottom();
                drawCurrentAreaContent(mAreaBitmapCanvas, currentAreaPath, mCurrentAreaPaint);
                drawBgAreaContent(mAreaBitmapCanvas, currentAreaPath, mBgAreaPaint);
                drawNextAreaContent(mAreaBitmapCanvas, currentAreaPath, mNextAreaPaint);
            }
        }
        canvas.drawBitmap(mAreaBitmap, 0, 0, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 防止抬起手指后滑动动画还未结束，就再次翻页滑动，造成动画错乱
        if (!mScroller.isFinished()) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP && !mGestureDetector.onTouchEvent(event)) {
            onUp(event);
        }
        return mGestureDetector.onTouchEvent(event);
    }

    private float mStartX = 0f;
    private float mStartY = 0f;
    private boolean isTouchUp = false;
    private boolean isFromTouch = false;

    @Override
    public boolean onDown(MotionEvent e) {
//        Log.d(TAG, "onDown");
        mStartX = e.getX();
        mStartY = e.getY();
        isTouchUp = false;
        isFromTouch = true;
        return true;
    }


    private void onUp(MotionEvent event) {
        Log.d(TAG, "onUp: " + event.getX() + ", " + event.getY());
        isTouchUp = true;
        final float x = event.getX();
        final float y = event.getY();
        final boolean isTurnPage = Math.hypot(Math.abs(mStartX - x), Math.abs(Math.abs(mStartY - y))) > MIN_TURN_PAGE_DISTANCE;
        if (mOrientation == Orientation.Center) {
            if (mOnReadListener != null) {
                mOnReadListener.onMenu();
            }
        } else if (mOrientation == Orientation.Left) {
            startScrollTo(mTouchPoint.x, mTouchPoint.y, isTurnPage ? mViewWidth : -mViewWidth, mTouchPoint.y);
            if (mOnReadListener != null) {
                if (isTurnPage) {
                    mOnReadListener.onPreviousStart();
                } else {
                    mOnReadListener.onPreviousCancel();
                }
            }
        } else {
            if (mOrientation == Orientation.RightTop) {
                startScrollTo(mTouchPoint.x, mTouchPoint.y, isTurnPage ? -mViewWidth : mViewWidth, mVertex.y);
            } else if (mOrientation == Orientation.RightCenter) {
                startScrollTo(mTouchPoint.x, mTouchPoint.y, isTurnPage ? -mViewWidth : mViewWidth, mTouchPoint.y);
            } else if (mOrientation == Orientation.RightBottom) {
                startScrollTo(mTouchPoint.x, mTouchPoint.y, isTurnPage ? -mViewWidth : mViewWidth, mVertex.y);
            }
            if (mOnReadListener != null) {
                if (isTurnPage) {
                    mOnReadListener.onNextStart();
                } else {
                    mOnReadListener.onNextCancel();
                }
            }
        }
    }


    @Override
    public void onShowPress(MotionEvent e) {
//        Log.d(TAG, "onShowPress: " + e.getAction());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        Log.d(TAG, "onSingleTapUp: " + e.getAction());
        if (System.currentTimeMillis() - mLastTouchTime > MIN_CLICK_TIME_INTERVAL) {
            isTouchUp = true;
            final float x = e.getX();
            final float y = e.getY();
            final Orientation orientation = calculateTouchArea(x, y);
            mOrientation = orientation;
            if (orientation == Orientation.Center) {
                if (mOnReadListener != null) {
                    mOnReadListener.onMenu();
                }
            } else if (orientation == Orientation.Left) {
                mTouchPoint.x = x;
                mTouchPoint.y = mViewHeight - 0.1f;
                mVertex.x = mViewWidth;
                mVertex.y = mViewHeight;
                startScrollTo(mTouchPoint.x, mTouchPoint.y, mViewWidth, mTouchPoint.y);
                if (mOnReadListener != null) {
                    mOnReadListener.onPreviousStart();
                }
            } else if (orientation == Orientation.RightTop) {
                mTouchPoint.x = x;
                mTouchPoint.y = y;
                mVertex.x = mViewWidth;
                mVertex.y = 0;
                calculatePointCoordinates(mTouchPoint, mVertex);
                startScrollTo(mTouchPoint.x, mTouchPoint.y, -mViewWidth, mVertex.y);
                if (mOnReadListener != null) {
                    mOnReadListener.onNextStart();
                }
            } else if (orientation == Orientation.RightCenter) {
                mTouchPoint.x = x;
                mTouchPoint.y = mViewHeight - 0.1f;
                mVertex.x = mViewWidth;
                mVertex.y = mViewHeight;
                startScrollTo(mTouchPoint.x, mTouchPoint.y, -mViewWidth, mTouchPoint.y);
                if (mOnReadListener != null) {
                    mOnReadListener.onNextStart();
                }
            } else if (orientation == Orientation.RightBottom) {
                mTouchPoint.x = x;
                mTouchPoint.y = y;
                mVertex.x = mViewWidth;
                mVertex.y = mViewHeight;
                startScrollTo(mTouchPoint.x, mTouchPoint.y, -mViewWidth, mVertex.y);
                if (mOnReadListener != null) {
                    mOnReadListener.onNextStart();
                }
            }
            mLastTouchTime = System.currentTimeMillis();
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Log.d(TAG, "onScroll => e1x: " + e1.getX() + ", e1y: " + e1.getY() + ", e2x: " + e2.getX() + ", e2y: " + e2.getY() + ", distanceX: " + distanceX + ", distanceY: " + distanceY + ", event: " + e2.getAction());
        final Orientation orientation = calculateTouchArea(e1.getX(), e1.getY());
        mOrientation = orientation;
        final float x = e2.getX();
        final float y = e2.getY();
        if (orientation == Orientation.Center) {
            // TODO: do nothing
        } else if (orientation == Orientation.Left) {
            mTouchPoint.x = x;
            mTouchPoint.y = mViewHeight - 0.1f;
            mVertex.x = mViewWidth;
            mVertex.y = mViewHeight;
            calculatePointCoordinates(mTouchPoint, mVertex);
        } else if (orientation == Orientation.RightTop) {
            mTouchPoint.x = x;
            mTouchPoint.y = y;
            mVertex.x = mViewWidth;
            mVertex.y = 0;
            calculatePointCoordinates(mTouchPoint, mVertex);
            if (calculateLeftVertexCoordinates(mTouchPoint, mVertex) < 0) {
                calculateSimilarTouchPointCoordinates();
                calculatePointCoordinates(mTouchPoint, mVertex);
            }
        } else if (orientation == Orientation.RightCenter) {
            mTouchPoint.x = x;
            mTouchPoint.y = mViewHeight - 0.1f;
            mVertex.x = mViewWidth;
            mVertex.y = mViewHeight;
            calculatePointCoordinates(mTouchPoint, mVertex);
        } else if (orientation == Orientation.RightBottom) {
            mTouchPoint.x = x;
            mTouchPoint.y = y;
            mVertex.x = mViewWidth;
            mVertex.y = mViewHeight;
            calculatePointCoordinates(mTouchPoint, mVertex);
            if (calculateLeftVertexCoordinates(mTouchPoint, mVertex) < 0) {
                calculateSimilarTouchPointCoordinates();
                calculatePointCoordinates(mTouchPoint, mVertex);
            }
        }
        invalidate();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
//        Log.d(TAG, "onLongPress: " + e.getAction());
//        TODO:
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        Log.d(TAG, "onFling => velocityX: " + velocityX + ", velocityY: " + velocityY);
        return false;
    }


    private void startScrollTo(float startX, float startY, float endX, float endY) {
        calculatePointCoordinates(mTouchPoint, mVertex);
        mScroller.startScroll((int) startX, (int) startY, (int) (endX - startX), (int) (endY - startY), TURN_PAGE_ANIMATION_TIME);
        invalidate();
    }


    private Orientation calculateTouchArea(float x, float y) {
        Orientation orientation;
        if (x < mViewWidth / 3f) {
            orientation = Orientation.Left;
        } else if (x < mViewWidth * 2f / 3f) {
            if (y < mViewHeight / 3f) {
                orientation = Orientation.RightTop;
            } else if (y < mViewHeight * 2f / 3f) {
                orientation = Orientation.Center;
            } else {
                orientation = Orientation.RightBottom;
            }
        } else {
            if (y < mViewHeight / 3f) {
                orientation = Orientation.RightTop;
            } else if (y < mViewHeight * 2f / 3f) {
                orientation = Orientation.RightCenter;
            } else {
                orientation = Orientation.RightBottom;
            }
        }
        return orientation;
    }


    @Override
    public void computeScroll() {
        Log.d(TAG, "computeScroll => isFinished: " + mScroller.isFinished() + ", mOrientation: " + mOrientation + ", isTouchUp: " + isTouchUp);
        if (mScroller.isFinished() && isTouchUp && isFromTouch && mOnReadListener != null) {
            isFromTouch = false;
            if (mOrientation == Orientation.Center) {
            } else if (mOrientation == Orientation.Left) {
                mOnReadListener.onPreviousEnd();
            } else {
                mOnReadListener.onNextEnd();
            }
        }
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() == mScroller.getFinalX() && mScroller.getCurrY() == mScroller.getFinalY()) {
                mTouchPoint.x = -1;
                mTouchPoint.y = -1;
            } else {
                mTouchPoint.x = mScroller.getCurrX();
                mTouchPoint.y = mScroller.getCurrY();
                calculatePointCoordinates(mTouchPoint, mVertex);
            }
            invalidate();
        }
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }


    private Path getDefaultPath() {
        mCurrentAreaPath.reset();
        mCurrentAreaPath.lineTo(0, mViewHeight);
        mCurrentAreaPath.lineTo(mViewWidth, mViewHeight);
        mCurrentAreaPath.lineTo(mViewWidth, 0);
        mCurrentAreaPath.close();
        return mCurrentAreaPath;
    }

    private Path getCurrentAreaPathInRightBottom() {
        mCurrentAreaPath.reset();
        mCurrentAreaPath.lineTo(0, mViewHeight);//移动到左下角
        mCurrentAreaPath.lineTo(c.x, c.y);//移动到c点
        mCurrentAreaPath.quadTo(e.x, e.y, b.x, b.y);//从c到b画贝塞尔曲线，控制点为e
        mCurrentAreaPath.lineTo(mTouchPoint.x, mTouchPoint.y);//移动到a点
        mCurrentAreaPath.lineTo(k.x, k.y);//移动到k点
        mCurrentAreaPath.quadTo(h.x, h.y, j.x, j.y);//从k到j画贝塞尔曲线，控制点为h
        mCurrentAreaPath.lineTo(mViewWidth, 0);//移动到右上角
        mCurrentAreaPath.close();//闭合区域
        return mCurrentAreaPath;
    }

    private Path getCurrentAreaPathInRightTop() {
        mCurrentAreaPath.reset();
        mCurrentAreaPath.lineTo(c.x, c.y);//移动到c点
        mCurrentAreaPath.quadTo(e.x, e.y, b.x, b.y);//从c到b画贝塞尔曲线，控制点为e
        mCurrentAreaPath.lineTo(mTouchPoint.x, mTouchPoint.y);//移动到a点
        mCurrentAreaPath.lineTo(k.x, k.y);//移动到k点
        mCurrentAreaPath.quadTo(h.x, h.y, j.x, j.y);//从k到j画贝塞尔曲线，控制点为h
        mCurrentAreaPath.lineTo(mViewWidth, mViewHeight);//移动到右下角
        mCurrentAreaPath.lineTo(0, mViewHeight);//移动到左下角
        mCurrentAreaPath.close();
        return mCurrentAreaPath;
    }

    private void drawCurrentAreaContent(Canvas canvas, Path currentAreaPath, Paint paint) {
        drawCurrentAreaContent(canvas, currentAreaPath, paint, false);
    }

    private void drawCurrentAreaContent(Canvas canvas, Path currentAreaPath, Paint paint, boolean isPrevious) {
        Bitmap contentBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.RGB_565);
        Canvas contentCanvas = new Canvas(contentBitmap);

        //下面开始绘制区域内的内容...
        contentCanvas.drawPath(currentAreaPath, paint);//绘制一个背景，用来区分各区域
        if (isPrevious) {
            drawPreviousPageContent(contentCanvas);
        } else {
            drawCurrentPageContent(contentCanvas);
        }

        //结束绘制区域内的内容...
        canvas.save();
//        canvas.clipPath(currentAreaPath, Region.Op.INTERSECT);//对绘制内容进行裁剪，取和A区域的交集
        canvas.drawBitmap(contentBitmap, 0, 0, null);

        if (mOrientation == Orientation.Left || mOrientation == Orientation.RightCenter) {
            drawCurrentAreaHorizontalShadow(canvas, currentAreaPath);
        } else {
            drawCurrentAreaLeftShadow(canvas, currentAreaPath);
            drawCurrentAreaRightShadow(canvas, currentAreaPath);
        }
        canvas.restore();
    }


    private void drawCurrentAreaLeftShadow(Canvas canvas, Path currentAreaPath) {
        canvas.restore();
        canvas.save();

        int deepColor = 0x33333333;
        int lightColor = 0x01333333;
        int[] gradientColors = {lightColor, deepColor};//渐变颜色数组

        int left;
        int right;
        int top = (int) e.y;
        int bottom = (int) (e.y + mViewHeight);

        GradientDrawable gradientDrawable;
        if (mOrientation == Orientation.RightTop) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (e.x - mCurrentLeftShadowWidth / 2);
            right = (int) (e.x);
        } else {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (e.x);
            right = (int) (e.x + mCurrentLeftShadowWidth / 2);
        }

        Path mPath = new Path();
        mPath.moveTo(mTouchPoint.x - Math.max(mCurrentLeftShadowWidth, mCurrentRightShadowWidth) / 2, mTouchPoint.y);
        mPath.lineTo(d.x, d.y);
        mPath.lineTo(e.x, e.y);
        mPath.lineTo(mTouchPoint.x, mTouchPoint.y);
        mPath.close();
        canvas.clipPath(currentAreaPath);
        canvas.clipPath(mPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(e.x - mTouchPoint.x, mTouchPoint.y - e.y));
        canvas.rotate(mDegrees, e.x, e.y);

        gradientDrawable.setBounds(left, top, right, bottom);
        gradientDrawable.draw(canvas);
    }

    private void drawCurrentAreaRightShadow(Canvas canvas, Path currentAreaPath) {
        canvas.restore();
        canvas.save();

        int deepColor = 0x33333333;
        int lightColor = 0x01333333;
        int[] gradientColors = {deepColor, lightColor, lightColor};//渐变颜色数组

        float viewDiagonalLength = (float) Math.hypot(mViewWidth, mViewHeight);//view对角线长度
        int left = (int) h.x;
        int right = (int) (h.x + viewDiagonalLength * 10);//需要足够长的长度
        int top;
        int bottom;

        GradientDrawable gradientDrawable;
        if (mOrientation == Orientation.RightTop) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            top = (int) (h.y - mCurrentRightShadowWidth / 2);
            bottom = (int) h.y;
        } else {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            top = (int) h.y;
            bottom = (int) (h.y + mCurrentRightShadowWidth / 2);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        Path mPath = new Path();
        mPath.moveTo(mTouchPoint.x - Math.max(mCurrentLeftShadowWidth, mCurrentRightShadowWidth) / 2, mTouchPoint.y);
        mPath.lineTo(h.x, h.y);
        mPath.lineTo(mTouchPoint.x, mTouchPoint.y);
        mPath.close();
        canvas.clipPath(currentAreaPath);
        canvas.clipPath(mPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(mTouchPoint.y - h.y, mTouchPoint.x - h.x));
        canvas.rotate(mDegrees, h.x, h.y);
        gradientDrawable.draw(canvas);
    }

    private void drawCurrentAreaHorizontalShadow(Canvas canvas, Path currentAreaPath) {
        canvas.restore();
        canvas.save();

        int deepColor = 0x44333333;
        int lightColor = 0x01333333;
        int[] gradientColors = {lightColor, deepColor};//渐变颜色数组

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        int maxShadowWidth = 30;//阴影矩形最大的宽度
        int left = (int) (mTouchPoint.x - Math.min(maxShadowWidth, (mCurrentRightShadowWidth / 2)));
        int right = (int) (mTouchPoint.x);
        int top = 0;
        int bottom = mViewHeight;
        gradientDrawable.setBounds(left, top, right, bottom);

        canvas.clipPath(currentAreaPath, Region.Op.INTERSECT);

        float mDegrees = (float) Math.toDegrees(Math.atan2(mTouchPoint.x - mTouchPoint.x, mVertex.y - h.y));
        canvas.rotate(mDegrees, mTouchPoint.x, mTouchPoint.y);
        gradientDrawable.draw(canvas);
    }

    private Path getNextAreaPath() {
        mNextAreaPath.reset();
        mNextAreaPath.lineTo(0, mViewHeight);//移动到左下角
        mNextAreaPath.lineTo(mViewWidth, mViewHeight);//移动到右下角
        mNextAreaPath.lineTo(mViewWidth, 0);//移动到右上角
        mNextAreaPath.close();//闭合区域
        return mNextAreaPath;
    }

    private void drawNextAreaContent(Canvas canvas, Path currentAreaPath, Paint paint) {
        Bitmap contentBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.RGB_565);
        Canvas contentCanvas = new Canvas(contentBitmap);

        //下面开始绘制区域内的内容...
        contentCanvas.drawPath(getNextAreaPath(), paint);
        drawNextPageContent(contentCanvas);

        //结束绘制区域内的内容...
        canvas.save();
        canvas.clipPath(currentAreaPath);//裁剪出A区域
        canvas.clipPath(getBgAreaPath(), Region.Op.UNION);//裁剪出A和C区域的全集
        canvas.clipPath(getNextAreaPath(), Region.Op.REVERSE_DIFFERENCE);//裁剪出B区域中不同于与AC区域的部分
        canvas.drawBitmap(contentBitmap, 0, 0, null);

        drawNextAreaShadow(canvas);
        canvas.restore();
    }


    private void drawNextAreaShadow(Canvas canvas) {
        int deepColor = 0xff111111;//为了让效果更明显使用此颜色代码，具体可根据实际情况调整
        int lightColor = 0x00111111;
        int[] gradientColors = new int[]{deepColor, lightColor};//渐变颜色数组

        int deepOffset = 0;//深色端的偏移值
        int lightOffset = 0;//浅色端的偏移值
        float aTof = (float) Math.hypot((mTouchPoint.x - mVertex.x), (mTouchPoint.y - mVertex.y));//a到f的距离
        float viewDiagonalLength = (float) Math.hypot(mViewWidth, mViewHeight);//对角线长度

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + c.y);
        GradientDrawable gradientDrawable;
        if (mOrientation == Orientation.RightTop) {//f点在右上角
            //从左向右线性渐变
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);//线性渐变

            left = (int) (c.x - deepOffset);//c点位于左上角
            right = (int) (c.x + aTof / 5 + lightOffset);
        } else {
            //从右向左线性渐变
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (c.x - aTof / 5 - lightOffset);//c点位于左下角
            right = (int) (c.x + deepOffset);
        }
        gradientDrawable.setBounds(left, top, right, bottom);//设置阴影矩形

        float rotateDegrees = (float) Math.toDegrees(Math.atan2(e.x - mVertex.x, h.y - mVertex.y));//旋转角度
        canvas.rotate(rotateDegrees, c.x, c.y);//以c为中心点旋转
        gradientDrawable.draw(canvas);
    }


    private Path getBgAreaPath() {
        mBgAreaPath.reset();
        mBgAreaPath.moveTo(i.x, i.y);//移动到i点
        mBgAreaPath.lineTo(d.x, d.y);//移动到d点
        mBgAreaPath.lineTo(b.x, b.y);//移动到b点
        mBgAreaPath.lineTo(mTouchPoint.x, mTouchPoint.y);//移动到a点
        mBgAreaPath.lineTo(k.x, k.y);//移动到k点
        mBgAreaPath.close();//闭合区域
        return mBgAreaPath;
    }

    private void drawBgAreaContent(Canvas canvas, Path currentAreaPath, Paint pathPaint) {
        Bitmap contentBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.RGB_565);
        Canvas contentCanvas = new Canvas(contentBitmap);

        //下面开始绘制区域内的内容...
        contentCanvas.drawPath(getNextAreaPath(), pathPaint);//绘制一个背景，path用B的就行
        drawCurrentPageContent(contentCanvas);

        canvas.save();
        canvas.clipPath(currentAreaPath);
        canvas.clipPath(getBgAreaPath(), Region.Op.REVERSE_DIFFERENCE);//裁剪出C区域不同于A区域的部分
        canvas.drawPath(getBgAreaPath(), mBgAreaPaint);

        float eh = (float) Math.hypot(mVertex.x - e.x, h.y - mVertex.y);
        float sin0 = (mVertex.x - e.x) / eh;
        float cos0 = (h.y - mVertex.y) / eh;
        mMatrixArray[0] = -(1 - 2 * sin0 * sin0);
        mMatrixArray[1] = 2 * sin0 * cos0;
        mMatrixArray[3] = 2 * sin0 * cos0;
        mMatrixArray[4] = 1 - 2 * sin0 * sin0;


        mMatrix.reset();
        mMatrix.setValues(mMatrixArray);//翻转和旋转
        mMatrix.preTranslate(-e.x, -e.y);//沿当前XY轴负方向位移得到 矩形A₃B₃C₃D₃
        mMatrix.postTranslate(e.x, e.y);//沿原XY轴方向位移得到 矩形A4 B4 C4 D4

        canvas.drawBitmap(contentBitmap, mMatrix, null);
        drawBgAreaShadow(canvas);
        canvas.restore();
    }

    private void drawBgAreaShadow(Canvas canvas) {
        int deepColor = 0x55333333;
        int lightColor = 0x00333333;
        int[] gradientColors = {lightColor, deepColor};//渐变颜色数组

        int deepOffset = 1;//深色端的偏移值
        int lightOffset = -30;//浅色端的偏移值
        float viewDiagonalLength = (float) Math.hypot(mViewWidth, mViewHeight);//view对角线长度
        int midpoint_ce = (int) (c.x + e.x) / 2;//ce中点
        int midpoint_jh = (int) (j.y + h.y) / 2;//jh中点
        float minDisToControlPoint = Math.min(Math.abs(midpoint_ce - e.x), Math.abs(midpoint_jh - h.y));//中点到控制点的最小值

        int left;
        int right;
        int top = (int) c.y;
        int bottom = (int) (viewDiagonalLength + c.y);
        GradientDrawable gradientDrawable;
        if (mOrientation == Orientation.RightTop) {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (c.x - lightOffset);
            right = (int) (c.x + minDisToControlPoint + deepOffset);
        } else {
            gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

            left = (int) (c.x - minDisToControlPoint - deepOffset);
            right = (int) (c.x + lightOffset);
        }
        gradientDrawable.setBounds(left, top, right, bottom);

        float mDegrees = (float) Math.toDegrees(Math.atan2(e.x - mVertex.x, h.y - mVertex.y));
        canvas.rotate(mDegrees, c.x, c.y);
        gradientDrawable.draw(canvas);
    }

    /**
     * 当左顶点x处于临界值的时候，找到触摸点相似的点
     */
    private void calculateSimilarTouchPointCoordinates() {
        float w0 = mViewWidth - c.x;

        float w1 = Math.abs(mVertex.x - mTouchPoint.x);
        float w2 = mViewWidth * w1 / w0;
        mTouchPoint.x = Math.abs(mVertex.x - w2);

        float h1 = Math.abs(mVertex.y - mTouchPoint.y);
        float h2 = w2 * h1 / w1;
        mTouchPoint.y = Math.abs(mVertex.y - h2);
    }

    /**
     * 计算左顶点是否超过界限，左顶点x坐标不能小于0，翻书不能超过左侧
     *
     * @param mTouchPoint
     * @param mVertex
     * @return
     */
    private float calculateLeftVertexCoordinates(PointF mTouchPoint, PointF mVertex) {
        PointF g, e;
        g = new PointF();
        e = new PointF();
        g.x = (mTouchPoint.x + mVertex.x) / 2;
        g.y = (mTouchPoint.y + mVertex.y) / 2;

        e.x = g.x - (mVertex.y - g.y) * (mVertex.y - g.y) / (mVertex.x - g.x);
        e.y = mVertex.y;

        return e.x - (mVertex.x - e.x) / 2;
    }

    private void calculateNegativePointCoordinates(PointF a, PointF f) {
        PointF a1 = new PointF(a.x - mViewWidth, a.y);
        PointF f1 = new PointF(f.x - mViewWidth, f.y);
        calculatePointCoordinates(a1, f1);
    }

    /**
     * 计算各个定点的坐标
     *
     * @param a 触摸点
     * @param f 右下角
     */
    private void calculatePointCoordinates(PointF a, PointF f) {
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        h.x = f.x;
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y);

        c.x = e.x - (f.x - e.x) / 2;
        c.y = f.y;

        j.x = f.x;
        j.y = h.y - (f.y - h.y) / 2;

        b = CalculateLineIntersectsPoint(a, e, c, j);
        k = CalculateLineIntersectsPoint(a, h, c, j);

        d.x = (c.x + 2 * e.x + b.x) / 4;
        d.y = (2 * e.y + c.y + b.y) / 4;

        i.x = (j.x + 2 * h.x + k.x) / 4;
        i.y = (2 * h.y + j.y + k.y) / 4;

        float lA = a.y - e.y;
        float lB = e.x - a.x;
        float lC = a.x * e.y - e.x * a.y;
        mCurrentLeftShadowWidth = Math.abs((lA * d.x + lB * d.y + lC) / (float) Math.hypot(lA, lB));

        float rA = a.y - h.y;
        float rB = h.x - a.x;
        float rC = a.x * h.y - h.x * a.y;
        mCurrentRightShadowWidth = Math.abs((rA * i.x + rB * i.y + rC) / (float) Math.hypot(rA, rB));
    }

    private PointF CalculateLineIntersectsPoint(PointF xLineStart, PointF xLineEnd, PointF yLineStart, PointF yLineEnd) {
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

    protected abstract void drawPreviousPageContent(Canvas canvas);

    protected abstract void drawCurrentPageContent(Canvas canvas);

    protected abstract void drawNextPageContent(Canvas canvas);

    protected abstract void onSizeMeasured();

    public void setCurrentBackgroundColor(@ColorInt int color) {
        mCurrentAreaPaint.setColor(color);
        invalidate();
    }

    public void setCurrentBackgroundColorRes(@ColorRes int colorId) {
        mCurrentAreaPaint.setColor(getContext().getResources().getColor(colorId));
        invalidate();
    }

    public void setCurrentBackgroundColor(String color) {
        mCurrentAreaPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public void setBgBackgroundColor(@ColorInt int color) {
        mBgAreaPaint.setColor(color);
        invalidate();
    }

    public void setBgBackgroundColorRes(@ColorRes int colorId) {
        mBgAreaPaint.setColor(getContext().getResources().getColor(colorId));
        invalidate();
    }

    public void setBgBackgroundColor(String color) {
        mBgAreaPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public void setNextBackgroundColor(@ColorInt int color) {
        mNextAreaPaint.setColor(color);
        invalidate();
    }

    public void setNextBackgroundColorRes(@ColorRes int colorId) {
        mNextAreaPaint.setColor(getContext().getResources().getColor(colorId));
        invalidate();
    }

    public void setNextBackgroundColor(String color) {
        mNextAreaPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public void setOnReadListener(OnReadListener listener) {
        this.mOnReadListener = listener;
    }


    public interface OnReadListener {
        void onNextStart();

        void onNextEnd();

        void onNextCancel();

        void onPreviousStart();

        void onPreviousEnd();

        void onPreviousCancel();

        void onMenu();
    }
}
