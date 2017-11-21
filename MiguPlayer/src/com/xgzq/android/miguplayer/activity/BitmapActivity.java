package com.xgzq.android.miguplayer.activity;

import java.util.ArrayList;
import java.util.List;

import com.xgzq.android.miguplayer.R;
import com.xgzq.android.miguplayer.tools.ImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

@SuppressLint("NewApi")
public class BitmapActivity extends Activity implements OnScrollListener
{
	private final String TAG = "Migu_BitmapActivity";
	private final int mImgSize = 21;
	private String mBaseUrl = "http://10.10.16.155:8080/res/imgs/img";

	private ListView mListView;
	private InnerAdapter mAdapter;

	private List<String> mUrlList;
	
	private ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bitmap);

		mListView = (ListView) findViewById(R.id.activity_bitmap_listview);
		mAdapter = new InnerAdapter();
		mListView.setOnScrollListener(this);
		
		mImageLoader = ImageLoader.build(this);

		mUrlList = new ArrayList<String>();
		for (int i = 1; i <= mImgSize; i++)
		{
			mUrlList.add(mBaseUrl + i + ".jpg");
		}

		mListView.setAdapter(mAdapter);

	}

	class InnerAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mUrlList != null ? mUrlList.size() : 0;
		}

		@Override
		public String getItem(int position)
		{
			return mUrlList != null ? mUrlList.get(position) : null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder = null;
			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(BitmapActivity.this).inflate(R.layout.layout_listview_item, parent,false);
				holder.mImageView = (ImageView) convertView.findViewById(R.id.listview_item_iv);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			ImageView iv = holder.mImageView;
			final String uri = (String) iv.getTag();
			final String url = getItem(position);
			if(!url.equals(uri))
			{
				Log.i(TAG, "[getView] url = " + url);
				//TODO 设置默认图片
			}
			if(isListViewIdle)
			{
				iv.setTag(url);
				mImageLoader.loadImage(iv, url, 0, 0);
			}

			return convertView;
		}

		
		class ViewHolder
		{
			ImageView mImageView;
		}
	}

	private boolean isListViewIdle = true;
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE)
		{
			isListViewIdle = true;
			mAdapter.notifyDataSetChanged();
		}
		else
		{
			isListViewIdle = false;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		
	}
}
