package com.xgzq.fullscene;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HiResPager extends LinearLayout {

    private ViewPager mViewPager;
    private HiResPagerAdapter mAdapter;
    private List<String> mDataList;

    public HiResPager(Context context) {
        this(context, null);
    }

    public HiResPager(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiResPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mDataList = new ArrayList<>();
        mAdapter = new HiResPagerAdapter(mDataList, context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.widget_hi_res_pager, this, true);
        mViewPager = inflate.findViewById(R.id.widget_hi_res_pager_root);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageMargin(Utils.dp2px(-20));
    }

    public void addData(List<String> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);
        mAdapter.notifyDataSetChanged();
    }

    static class HiResPagerAdapter extends PagerAdapter {

        WeakReference<List<String>> mDataListRef;
        WeakReference<Context> mContextRef;

        HiResPagerAdapter(List<String> list, Context context) {
            mDataListRef = new WeakReference<>(list);
            mContextRef = new WeakReference<>(context);
        }

        @Override
        public int getCount() {
            if (mDataListRef.get() != null) {
                return mDataListRef.get().size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        int[] widths = new int[]{100, 120, 100, 80, 60};

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if (mContextRef.get() != null) {
                View view = View.inflate(mContextRef.get(), R.layout.item_hi_res_pager, null);
                ImageView imageView = view.findViewById(R.id.item_hi_res_pager_img);
                LinearLayout.LayoutParams lp = null;
                if (position == getCount() - 2) {
                    lp = new LinearLayout.LayoutParams(Utils.dp2px(120), Utils.dp2px(120));
                } else if (position == getCount() - 3) {
                    lp = new LinearLayout.LayoutParams(Utils.dp2px(100), Utils.dp2px(100));
                    lp.rightMargin = Utils.dp2px(-40);
                } else if (position == getCount() - 4) {
                    lp = new LinearLayout.LayoutParams(Utils.dp2px(80), Utils.dp2px(80));
                    lp.rightMargin = Utils.dp2px(-56);
                } else {
                    lp = new LinearLayout.LayoutParams(Utils.dp2px(40), Utils.dp2px(40));
                }
                imageView.setLayoutParams(lp);
                if (mDataListRef.get() != null) {
                    String url = mDataListRef.get().get(getCount() - 1 - position);
                    Glide.with(mContextRef.get()).load(url).into(imageView);
                    container.addView(view);
                    return view;
                } else {
                    imageView.setImageResource(R.mipmap.placeholder);
                }
            }
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public float getPageWidth(int position) {
            return 0.4f;
        }
    }
}
