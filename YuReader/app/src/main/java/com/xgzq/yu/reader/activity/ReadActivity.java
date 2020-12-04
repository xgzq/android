package com.xgzq.yu.reader.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.victor.loading.book.BookLoading;
import com.xgzq.yu.reader.R;
import com.xgzq.yu.reader.model.adapter.ChapterListAdapter;
import com.xgzq.yu.reader.model.bean.Book;
import com.xgzq.yu.reader.model.bean.Chapter;
import com.xgzq.yu.reader.model.model.BookModel;
import com.xgzq.yu.reader.model.model.ReadModel;
import com.xgzq.yu.reader.utils.DistanceUtil;
import com.xgzq.yu.reader.utils.Util;
import com.xgzq.yu.reader.widget.ReadView2;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ReadActivity extends AppCompatActivity {

    private static final String TAG = "XGZQ:ReadActivity";

    private static final int REQUEST_PERMISSION_CODE = 0x0000001;
    public static final float PERCENT_WIDTH_CHAPTER_RECYCLER_VIEW = 0.9f;
    private static final String[] Permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ReadView2 mReadView;
    private BookLoading mBookLoading;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mChapterRecyclerView;

    private Book mBook;
    private BookModel.ReadConfig mReadConfig;

    private ChapterListAdapter mChapterListAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;
    private List<Chapter> mChapterList;

    private ReadModel mReadModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_read);


        Intent intent = getIntent();
        if (intent != null) {
            mBook = intent.getParcelableExtra(BookListActivity.EXTRA_KEY_BOOK);
            mReadConfig = intent.getParcelableExtra(BookListActivity.EXTRA_KEY_CONFIG);
        } else {
            throw new IllegalArgumentException("intent is null, args is null!");
        }
        mReadModel = new ReadModel(mBook, mGetChapterListCallback);

        // 初始化View
        mReadView = findViewById(R.id.read_rv);
        mBookLoading = findViewById(R.id.read_lv);
        mDrawerLayout = findViewById(R.id.read_root_drawer_layout);
        mChapterRecyclerView = findViewById(R.id.read_chapter_list);
        mChapterRecyclerView.getLayoutParams().width = (int) (DistanceUtil.getScreenWidth() * PERCENT_WIDTH_CHAPTER_RECYCLER_VIEW);

        // 初始化列表数据
        mChapterList = new ArrayList<>(200);
        mChapterListAdapter = new ChapterListAdapter(mChapterList, mReadConfig, mChapterListClickListener);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mDividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.shape_list_item_divider_line));
        mChapterRecyclerView.addItemDecoration(mDividerItemDecoration);
        mChapterRecyclerView.setLayoutManager(mLinearLayoutManager);
        mChapterRecyclerView.setAdapter(mChapterListAdapter);

        // 初始化阅读参数
        mReadView.setReadConfig(mReadConfig);
        mReadView.setBook(mBook);
        mReadView.setReadCallback(mReadCallback);

        if (Util.isOver6() && (checkSelfPermission(Permissions[0]) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Permissions[1]) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(Permissions, REQUEST_PERMISSION_CODE);
        } else {
            mReadModel.getChapterList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "READ_EXTERNAL_STORAGE PERMISSION_GRANTED");
                mReadModel.getChapterList();
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE PERMISSION_DENIED");
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "WRITE_EXTERNAL_STORAGE PERMISSION_GRANTED");
            } else {
                Log.d(TAG, "WRITE_EXTERNAL_STORAGE PERMISSION_DENIED");
            }
        }
    }


    private ChapterListAdapter.OnClickListener mChapterListClickListener = new ChapterListAdapter.OnClickListener() {
        @Override
        public void onClick(View view, Chapter chapter, int position) {
            if (position == 0) {
                mReadView.setPreviousChapter(null);
            } else {
                mReadView.setPreviousChapter(mChapterList.get(position - 1));
            }
            if (position == mChapterList.size() - 1) {
                mReadView.setNextChapter(null);
            } else {
                mReadView.setNextChapter(mChapterList.get(position + 1));
            }
            mReadView.setChapter(chapter, true);
            mReadConfig.setChapterIndex(position);
            mDrawerLayout.closeDrawer(mChapterRecyclerView);
        }
    };

    private ReadModel.IChapterCallback mGetChapterListCallback = new ReadModel.IChapterCallback() {
        @Override
        public void onStart(Disposable disposable) {
            Log.d(TAG, "onStart");
            mBookLoading.start();
        }

        @Override
        public void onSuccess(List<Chapter> chapterList) {
            Log.d(TAG, "onSuccess: " + chapterList.size());
            // 不能把mChapterList直接赋值为chapterList，否则变量地址变了mChapterListAdapter.notifyDataSetChanged()不会起作用
            mChapterList.clear();
            mChapterList.addAll(chapterList);
        }

        @Override
        public void onFail() {
            Log.d(TAG, "onFail");
            Toast.makeText(ReadActivity.this, "获取章节失败！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete");
            mChapterListAdapter.notifyDataSetChanged();
            mReadView.setChapter(mChapterList.get(mReadConfig.getChapterIndex()), true);
        }

        @Override
        public void onError(Throwable throwable) {
            Log.d(TAG, "onError");
        }
    };

    /**
     * 阅读状态回调
     */
    private ReadView2.ReadCallback mReadCallback = new ReadView2.ReadCallback() {
        @Override
        public void onNextChapter() {
            if (mReadConfig.getChapterIndex() >= mChapterList.size() - 1) {
                Toast.makeText(ReadActivity.this, "已经是最后一章了！", Toast.LENGTH_SHORT).show();
                return;
            }
            mReadView.setPreviousChapter(mChapterList.get(mReadConfig.getChapterIndex()));
            final int nextChapterIndex = mReadConfig.getChapterIndex() + 1;
            mReadConfig.setChapterIndex(nextChapterIndex);
            final Chapter chapter = mChapterList.get(nextChapterIndex);
            Log.d(TAG, "nextChapterIndex: " + nextChapterIndex + ", chapter: " + chapter.getTitle());
            mReadView.setChapter(chapter, true);
            if (nextChapterIndex + 1 <= mChapterList.size() - 1) {
                mReadView.setNextChapter(mChapterList.get(nextChapterIndex + 1));
            } else {
                mReadView.setNextChapter(null);
            }
        }

        @Override
        public void onPreviousChapter() {
            if (mReadConfig.getChapterIndex() <= 0) {
                Toast.makeText(ReadActivity.this, "已经是第一章了！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mReadConfig.getChapterIndex() == 0) {
                mReadView.setPreviousChapter(null);
            }
            int previousChapterIndex = mReadConfig.getChapterIndex() - 1;
            mReadConfig.setChapterIndex(previousChapterIndex);
            mReadView.setChapter(mChapterList.get(previousChapterIndex), false);
            mReadView.setNextChapter(mChapterList.get(previousChapterIndex + 1));
        }

        @Override
        public void onStartLoading() {
            mBookLoading.setVisibility(View.VISIBLE);
            mBookLoading.start();
        }

        @Override
        public void onStopLoading() {
            mBookLoading.stop();
            mBookLoading.setVisibility(View.INVISIBLE);
        }
    };


    public void subFontSize(View view) {
        mReadView.subFontSize();
    }

    public void addFontSize(View view) {
        mReadView.addFontSize();
    }

    public void subLineSpacing(View view) {
        mReadView.subLineSpacing();
    }

    public void addLineSpacing(View view) {
        mReadView.addLineSpacing();
    }

    public void subPaddingX(View view) {
        mReadView.subPaddingX();
    }

    public void addPaddingX(View view) {
        mReadView.addPaddingX();
    }

    public void subPaddingY(View view) {
        mReadView.subPaddingY();
    }

    public void addPaddingY(View view) {
        mReadView.addPaddingY();
    }

    public void nextPage(View view) {
        mReadView.nextPage();
    }

    public void previousPage(View view) {
        mReadView.previousPage();
    }
}
