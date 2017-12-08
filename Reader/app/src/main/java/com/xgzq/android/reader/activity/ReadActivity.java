package com.xgzq.android.reader.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.xgzq.android.reader.R;
import com.xgzq.android.reader.model.Chapter;
import com.xgzq.android.reader.model.adapter.ChapterListAdapter;
import com.xgzq.android.reader.tools.ChapterUtil;
import com.xgzq.android.reader.tools.FileUtil;

import java.util.List;

public class ReadActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener
{

    public static final String TAG = "Reader_ReadActivity";
    private ListView mChapterListView;
    private Button mConfirmButton;
    private TextView mContentTextView;

    private ChapterListAdapter mListAdapter;
    private List<Chapter> mChapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        mChapterListView = (ListView) findViewById(R.id.activity_read_lv_list);
        mConfirmButton = (Button) findViewById(R.id.activity_read_btn_confirm);
        mContentTextView = (TextView) findViewById(R.id.activity_read_tv_content);

        mConfirmButton.setOnClickListener(this);
        mChapterListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Log.i(TAG,"[onClick]");
        mConfirmButton.setEnabled(false);
        new AsyncTask<Void, Void, List<Chapter>>()
        {
            @Override
            protected List<Chapter> doInBackground(Void... params)
            {
                Log.i(TAG,"[doInBackground]");
                String content = FileUtil.getFileContent("file:///android_asset/test.txt");
                List<Chapter> result = ChapterUtil.getChapters(content);
                return result;
            }

            @Override
            protected void onPostExecute(List<Chapter> chapters)
            {
                if(chapters != null && chapters.size() >= 0)
                {
                    Log.i(TAG,"[onPostExecute]");
                    mChapterList = chapters;
                    mListAdapter = new ChapterListAdapter(ReadActivity.this,mChapterList);
                    mChapterListView.setAdapter(mListAdapter);
                    mContentTextView.setText(mChapterList.get(0).getContent());
                }
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(mChapterList != null && position < mChapterList.size() - 1)
        {
            Log.i(TAG,"[onItemClick]");
            mContentTextView.setText(mChapterList.get(position).getContent());
        }
    }
}
