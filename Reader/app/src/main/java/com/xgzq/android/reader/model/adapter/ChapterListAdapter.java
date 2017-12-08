package com.xgzq.android.reader.model.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xgzq.android.reader.R;
import com.xgzq.android.reader.model.Chapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class ChapterListAdapter extends BaseAdapter
{
    public static final String TAG = "Reader_ChapterListAdapter";

    private List<Chapter> mData;
    private LayoutInflater mInflater;

    public ChapterListAdapter(Context context, List<Chapter> data)
    {
        if (data != null)
        {
            this.mData = data;
        }
        else
        {
            this.mData = new ArrayList<>();
        }

        if (context != null)
        {
            mInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount()
    {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public String getItem(int position)
    {
        return mData != null ? mData.get(position).getTitle() : null;
    }

    @Override
    public long getItemId(int position)
    {
        return mData != null ? mData.get(position).getIndex() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            if (mInflater != null)
            {
                convertView = mInflater.inflate(R.layout.layout_item_chapter_list, null);
            }
            else
            {
                return null;
            }
            holder.tv = (TextView) convertView.findViewById(R.id.layout_item_chapter_list_tv_title);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder != null)
        {
            holder.tv.setText(getItem(position));
        }
        else
        {
            Log.e(TAG, "[getView] holder is null");
        } return convertView;
    }

    class ViewHolder
    {
        TextView tv;
    }
}
