package com.xgzq.fullscene;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class HiResAnim extends LinearLayout {

    private static final String TAG = "HiResAnim";
    private ImageView mLeftLeftImage, mLeftImage, mCenterImage, mRightImage, mRightRightImage;

    private List<String> mUrlList;
    private List<Bitmap> mBitmapList;

    private ValueAnimator mValueAnimator;
    private float mDensity;
    private int mLeftWidth, mCenterWidth, mRightWidth, mLeftHeight, mCenterHeight, mRightHeight;
    private int mLeftLeftWidth, mLeftLeftHeight, mRightRightWidth, mRightRightHeight;
    private int mCurrentIndex;

    public HiResAnim(Context context) {
        this(context, null);
    }

    public HiResAnim(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiResAnim(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public HiResAnim(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mUrlList = new ArrayList<>();
        mBitmapList = new ArrayList<>();
        mDensity = context.getResources().getDisplayMetrics().density;
        View view = LayoutInflater.from(context).inflate(R.layout.widget_hi_res_anim, this, true);

        mLeftLeftImage = view.findViewById(R.id.widget_hi_res_anim_img_left_left);
        mLeftImage = view.findViewById(R.id.widget_hi_res_anim_img_left);
        mCenterImage = view.findViewById(R.id.widget_hi_res_anim_img_center);
        mRightImage = view.findViewById(R.id.widget_hi_res_anim_img_right);
        mRightRightImage = view.findViewById(R.id.widget_hi_res_anim_img_right_right);

        mValueAnimator = new ValueAnimator();
        mValueAnimator.setIntValues(0, 100);
        mValueAnimator.setDuration(1000);
        mValueAnimator.setStartDelay(2000);

        mLeftLeftWidth = mLeftLeftImage.getLayoutParams().width;
        mLeftLeftHeight = mLeftLeftImage.getLayoutParams().height;
        mLeftWidth = mLeftImage.getLayoutParams().width;
        mLeftHeight = mLeftImage.getLayoutParams().height;
        mCenterWidth = mCenterImage.getLayoutParams().width;
        mCenterHeight = mCenterImage.getLayoutParams().height;
        mRightWidth = mRightImage.getLayoutParams().width;
        mRightHeight = mRightImage.getLayoutParams().height;
        mRightRightWidth = mRightRightImage.getLayoutParams().width;
        mRightRightHeight = mRightRightImage.getLayoutParams().height;

        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                Log.i(TAG, "onAnimationUpdate: " + fraction);
                animLeftLeftImage(fraction);
                animLeftImage(fraction);
                animCenterImage(fraction);
                animRightImage(fraction);
                animRightRightImage(fraction);
            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.i(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.i(TAG, "onAnimationEnd");

                resetImage();
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mBitmapList.size() - 1;
                } else {
                    mCurrentIndex--;
                }

//                mLeftImage.setImageBitmap(mBitmapList.get(1));
//                mCenterImage.setImageBitmap(mBitmapList.get(2));
                updateView();
                mValueAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.i(TAG, "onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.i(TAG, "onAnimationRepeat");
            }
        });
        mValueAnimator.addPauseListener(new Animator.AnimatorPauseListener() {
            @Override
            public void onAnimationPause(Animator animation) {
                Log.i(TAG, "onAnimationPause: " + mValueAnimator.getCurrentPlayTime());
            }

            @Override
            public void onAnimationResume(Animator animation) {
                Log.i(TAG, "onAnimationResume");
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void addImageUrl(List<String> urls) {
        mUrlList.addAll(urls);
        requestImage();
    }

    private void requestImage() {
        float density = getResources().getDisplayMetrics().density;
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCorners((int) (32 * density)))
                .placeholder(R.mipmap.placeholder);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.placeholder);
        for (int i = 0; i < mUrlList.size(); i++) {
            final int index = i;
            mBitmapList.add(bitmap);
            RequestBuilder<Bitmap> builder = Glide.with(getContext()).asBitmap().load(mUrlList.get(i)).apply(options).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    mBitmapList.set(index, resource);
                    updateView();
                    return false;
                }
            });
            builder.submit();
        }
        mValueAnimator.start();
    }

    private int getPrevious(int cur) {
        if (cur == 0) {
            return mBitmapList.size() - 1;
        }
        return cur - 1;
    }

    private int getNext(int cur) {
        if (cur == mBitmapList.size() - 1) {
            return 0;
        }
        return cur + 1;
    }

    private void updateView() {
        int previousIndex = getPrevious(mCurrentIndex);
        int previous2Index = getPrevious(previousIndex);
        int previous3Index = getPrevious(previous2Index);
        mLeftLeftImage.setImageBitmap(mBitmapList.get(previous3Index));
        mLeftImage.setImageBitmap(mBitmapList.get(previous2Index));
        mCenterImage.setImageBitmap(mBitmapList.get(previousIndex));
        mRightImage.setImageBitmap(mBitmapList.get(mCurrentIndex));
        mRightRightImage.setImageBitmap(mBitmapList.get(getNext(mCurrentIndex)));
    }

    private void animLeftLeftImage(float fraction) {
        mLeftLeftImage.getLayoutParams().width = (int) (mLeftLeftWidth + fraction * 20 * mDensity);
        mLeftLeftImage.getLayoutParams().height = (int) (mLeftLeftHeight + fraction * 20 * mDensity);
        mLeftLeftImage.setTranslationX(fraction * 68 * mDensity);
        mLeftLeftImage.requestLayout();
    }

    private void animLeftImage(float fraction) {
        mLeftImage.getLayoutParams().width = (int) (mLeftWidth + fraction * 20 * mDensity);
        mLeftImage.getLayoutParams().height = (int) (mLeftHeight + fraction * 20 * mDensity);
        mLeftImage.setTranslationX(fraction * 64 * mDensity);
        mLeftImage.requestLayout();
    }

    private void animCenterImage(float fraction) {
        mCenterImage.getLayoutParams().width = (int) (mCenterWidth + fraction * 20 * mDensity);
        mCenterImage.getLayoutParams().height = (int) (mCenterHeight + fraction * 20 * mDensity);
        mCenterImage.setTranslationX(fraction * 80 * mDensity);
        mCenterImage.requestLayout();
    }

    private void animRightImage(float fraction) {
        mRightImage.getLayoutParams().width = (int) (mRightWidth - fraction * 20 * mDensity);
        mRightImage.getLayoutParams().height = (int) (mRightHeight - fraction * 20 * mDensity);
        mRightImage.setTranslationX(fraction * 90 * mDensity);
        mRightImage.setTranslationY(fraction * 10 * mDensity);
        mRightImage.setAlpha(1 - fraction);
        mRightImage.requestLayout();
    }

    private void animRightRightImage(float fraction) {
        mRightRightImage.getLayoutParams().width = (int) (mRightWidth - fraction * 40 * mDensity);
        mRightRightImage.getLayoutParams().height = (int) (mRightHeight - fraction * 40 * mDensity);
        mRightRightImage.setTranslationX(-fraction * 312 * mDensity);
        mRightRightImage.setTranslationY(fraction * 10 * mDensity);
        mRightRightImage.requestLayout();
    }

    private void resetImage(){
        mLeftLeftImage.getLayoutParams().width = mLeftLeftWidth;
        mLeftLeftImage.getLayoutParams().height = mLeftLeftHeight;
        mLeftLeftImage.setTranslationX(0);

        mLeftImage.getLayoutParams().width = mLeftWidth;
        mLeftImage.getLayoutParams().height = mLeftHeight;
        mLeftImage.setTranslationX(0);

        mCenterImage.getLayoutParams().width = mCenterWidth;
        mCenterImage.getLayoutParams().height = mCenterHeight;
        mCenterImage.setTranslationX(0);

        mRightImage.getLayoutParams().width = mRightWidth;
        mRightImage.getLayoutParams().height = mRightHeight;
        mRightImage.setTranslationX(0);
        mRightImage.setTranslationY(0);
        mRightImage.setAlpha(1f);

        mRightRightImage.getLayoutParams().width = mRightRightWidth;
        mRightRightImage.getLayoutParams().height = mRightRightHeight;
        mRightRightImage.setTranslationX(0);
        mRightRightImage.setTranslationY(0);

//        mLeftLeftImage.requestLayout();
//        mLeftImage.requestLayout();
//        mCenterImage.requestLayout();
//        mRightImage.requestLayout();
//        mRightRightImage.requestLayout();
    }

}
