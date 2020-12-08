package com.xgzq.fullscene;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;

public class HiResSwitcher extends LinearLayout {
    public HiResSwitcher(Context context) {
        this(context, null);
    }

    public HiResSwitcher(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiResSwitcher(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        HiResViewSwitcher switcher = new HiResViewSwitcher(context);
        addView(switcher);
    }

    static class HiResViewSwitcher extends ViewSwitcher {

        public HiResViewSwitcher(Context context) {
            this(context, null);
        }

        public HiResViewSwitcher(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context, attrs);
        }

        private void init(Context context, AttributeSet attrs) {
            int w = Utils.dp2px(300);
            int h = Utils.dp2px(120);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(w, h);
            setBackgroundResource(R.color.colorAccent);
            setLayoutParams(layoutParams);
            setFactory(new HiResViewFactory(context));
        }

        @Override
        public void addView(View child) {
            super.addView(child);
        }

        @Override
        public View getNextView() {
            return super.getNextView();
        }
    }

    static class HiResViewFactory implements ViewSwitcher.ViewFactory {

        WeakReference<Context> mContextRef;
        String url;

        HiResViewFactory(Context context) {
            mContextRef = new WeakReference<>(context);
        }

        HiResViewFactory(Context context, String url) {
            mContextRef = new WeakReference<>(context);
            this.url = url;
        }

        @Override
        public View makeView() {
            if (mContextRef.get() != null) {
                ImageView iv = new ImageView(mContextRef.get());
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setLayoutParams(new FrameLayout.LayoutParams(Utils.dp2px(120), Utils.dp2px(120)));
                return iv;
            }
            return null;
        }
    }
}
