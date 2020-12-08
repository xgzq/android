package com.xgzq.fullscene;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HiResCard extends LinearLayout implements ValueAnimator.AnimatorPauseListener,
        ValueAnimator.AnimatorListener, ValueAnimator.AnimatorUpdateListener,
        HiResAnim.IChangedListener, HiResAnim.IOnColorComputedListener {

    private static final String TAG = "HiResCard";

    private HiResAnim mHiResAnim;
    private List<Album> mAlbumList;
    private SparseArray<Object[]> mColorArray;
    private Object[] mDefaultColor = new Object[]{Color.parseColor("#A0A0A0"), Color.parseColor("#000000"), Color.parseColor("#99000000")};
    private int mCurrentIndex;

    private ConstraintLayout mRoot;
    private TextView mPrevAlbumNameText, mCurrAlbumNameText, mNextAlbumNameText;
    private TextView mPrevArtistNameText, mCurrArtistNameText, mNextArtistNameText;
    private TextView mPrevAlbumTimesText, mCurrAlbumTimesText, mNextAlbumTimesText;
    private View mView1, mView2, mView3;
    private ValueAnimator mColorAnimator;

    public HiResCard(Context context) {
        this(context, null);
    }

    public HiResCard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiResCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public HiResCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mAlbumList = new ArrayList<>();
        mColorArray = new SparseArray<>(5);

        mColorAnimator = new ValueAnimator();
        mColorAnimator.setDuration(1000);
        mColorAnimator.setEvaluator(new ArgbEvaluator());
        mColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mRoot != null) {
                    mRoot.setBackgroundColor((Integer) animation.getAnimatedValue());
                }
            }
        });

        View root = LayoutInflater.from(context).inflate(R.layout.widget_hi_res_card, this, true);
        mRoot = root.findViewById(R.id.hi_res_card_root);
        mHiResAnim = root.findViewById(R.id.hi_res_card_anim);
        mPrevAlbumNameText = findViewById(R.id.hi_res_card_album_name_previous);
        mCurrAlbumNameText = findViewById(R.id.hi_res_card_album_name_current);
        mNextAlbumNameText = findViewById(R.id.hi_res_card_album_name_next);

        mPrevArtistNameText = findViewById(R.id.hi_res_card_artist_name_previous);
        mCurrArtistNameText = findViewById(R.id.hi_res_card_artist_name_current);
        mNextArtistNameText = findViewById(R.id.hi_res_card_artist_name_next);

        mPrevAlbumTimesText = findViewById(R.id.hi_res_card_album_text_times_previous);
        mCurrAlbumTimesText = findViewById(R.id.hi_res_card_album_text_times_current);
        mNextAlbumTimesText = findViewById(R.id.hi_res_card_album_text_times_next);

        mView1 = findViewById(R.id.hi_res_card_v1);
        mView2 = findViewById(R.id.hi_res_card_v2);
        mView3 = findViewById(R.id.hi_res_card_v3);

        mHiResAnim.setAnimatorListener(this);
        mHiResAnim.setAnimatorPauseListener(this);
        mHiResAnim.setAnimatorUpdateListener(this);
        mHiResAnim.setChangedListener(this);
        mHiResAnim.setOnColorComplutedListener(this);
    }

    public void setData(List<Album> albums) {
        mAlbumList.clear();
        mAlbumList.addAll(albums);
        List<String> urls = new ArrayList<>();
        for (Album album : mAlbumList) {
            urls.add(album.getAlbumCoverUrl());
        }
        mHiResAnim.addImageUrl(urls);
    }

    public void changeAlbumInfo() {
        final Album prevAlbum = mAlbumList.get(getPrevious(mCurrentIndex));
        final Album nextAlbum = mAlbumList.get(getNext(mCurrentIndex));
        final Album currAlbum = mAlbumList.get(mCurrentIndex);
        mPrevAlbumNameText.setText(prevAlbum.getAlbumName());
        mCurrAlbumNameText.setText(currAlbum.getAlbumName());
        mNextAlbumNameText.setText(nextAlbum.getAlbumName());

        mPrevArtistNameText.setText(prevAlbum.getArtistName());
        mCurrArtistNameText.setText(currAlbum.getArtistName());
        mNextArtistNameText.setText(nextAlbum.getArtistName());

        mPrevAlbumTimesText.setText(prevAlbum.getAlbumTimes());
        mCurrAlbumTimesText.setText(currAlbum.getAlbumTimes());
        mNextAlbumTimesText.setText(nextAlbum.getAlbumTimes());

        changeAlbumColor();
    }

    public void updateAlbumColor(boolean isReset, boolean isToPrevious, float fraction) {
        float prevAlpha = isReset ? 0 : isToPrevious ? fraction : 0;
        float nextAlpha = isReset ? 0 : isToPrevious ? 0 : fraction;
        mPrevAlbumNameText.setAlpha(prevAlpha);
        mCurrAlbumNameText.setAlpha(isReset ? 1 : 1 - fraction);
        mNextAlbumNameText.setAlpha(nextAlpha);

        mPrevArtistNameText.setAlpha(prevAlpha);
        mCurrArtistNameText.setAlpha(isReset ? 1 : 1 - fraction);
        mNextArtistNameText.setAlpha(nextAlpha);

        mPrevAlbumTimesText.setAlpha(prevAlpha);
        mCurrAlbumTimesText.setAlpha(isReset ? 1 : 1 - fraction);
        mNextAlbumTimesText.setAlpha(nextAlpha);
    }

    private int getPrevious(int i) {
        return i == 0 ? mAlbumList.size() - 1 : i - 1;
    }

    private int getNext(int i) {
        return i == mAlbumList.size() - 1 ? 0 : i + 1;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        Object[] prevColors = mColorArray.get(getPrevious(mCurrentIndex), mDefaultColor);
        Object[] currColors = mColorArray.get(mCurrentIndex, mDefaultColor);
        Log.i(TAG, "" + (Integer) currColors[0] + " to " + (Integer) prevColors[0]);
        mColorAnimator.setIntValues((Integer) currColors[0], (Integer) prevColors[0]);
        mColorAnimator.start();
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        updateAlbumColor(true, true, 0);
        changeAlbumInfo();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationPause(Animator animation) {

    }

    @Override
    public void onAnimationResume(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float fraction = animation.getAnimatedFraction();
        updateAlbumColor(false, true, fraction);
    }

    @Override
    public void onChanged(int fromIndex, int toIndex) {
        mCurrentIndex = toIndex;
    }

    @Override
    public void onComputed(int index, Object... objects) {
        Log.i(TAG, "onComputed => " + index + ": " + Arrays.toString(objects));
        final int bgColor = (Integer) objects[2];
        final float[] hsl = (float[]) objects[3];
        final float[] hsv = new float[]{hsl[0], 60, 23};
        final int albumNameTextColor = Color.HSVToColor(255, hsv);
        final int artistNameTextColor = Color.HSVToColor(153, hsv);
        mColorArray.append(index, new Object[]{bgColor, albumNameTextColor, artistNameTextColor});
        changeAlbumColor();
    }

    private void changeAlbumColor() {
        Object[] prevColors = mColorArray.get(getPrevious(mCurrentIndex), mDefaultColor);
        if (prevColors != null) {
            int albumNameTextColor = (Integer) prevColors[1];
            int artistNameTextColor = (Integer) prevColors[2];
            mPrevAlbumNameText.setTextColor(albumNameTextColor);
            mPrevArtistNameText.setTextColor(artistNameTextColor);
        }
        Object[] currColors = mColorArray.get(mCurrentIndex, mDefaultColor);
        if (currColors != null) {
            int bgColor = (Integer) currColors[0];
            int albumNameTextColor = (Integer) currColors[1];
            int artistNameTextColor = (Integer) currColors[2];
            mRoot.setBackgroundColor(bgColor);
            mCurrAlbumNameText.setTextColor(albumNameTextColor);
            mCurrArtistNameText.setTextColor(artistNameTextColor);
        }
        Object[] nextColors = mColorArray.get(getNext(mCurrentIndex), mDefaultColor);
        if (nextColors != null) {
            int albumNameTextColor = (Integer) nextColors[1];
            int artistNameTextColor = (Integer) nextColors[2];
            mNextAlbumNameText.setTextColor(albumNameTextColor);
            mNextArtistNameText.setTextColor(artistNameTextColor);
        }
    }
}
