package com.xgzq.yu.reader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.xgzq.yu.reader.R;
import com.xgzq.yu.reader.model.adapter.BookListAdapter;
import com.xgzq.yu.reader.model.bean.Book;
import com.xgzq.yu.reader.model.enums.DisplayMode;
import com.xgzq.yu.reader.model.model.BookModel;

public class BookListActivity extends AppCompatActivity implements BookModel.Callback {

    public static final String TAG = "XGZQ:BookListActivity";

    public static final String EXTRA_KEY_BOOK = "book";
    public static final String EXTRA_KEY_CONFIG = "config";


    private RecyclerView mBookRecyclerList;
    private BookListAdapter mBookAdapter;
    private BookModel mBookListModel;

    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBookListModel = new BookModel(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_book_list_show_type:
                // 文字为平铺模式时，此时界面展示为列表模式；点击后切换为平铺模式
                if (item.getTitle().toString().equals(getString(R.string.show_type_grid))) {
                    item.setTitle(R.string.show_type_list);
                    mBookRecyclerList.setLayoutManager(mStaggeredGridLayoutManager);
                    mBookListModel.setDisplayMode(DisplayMode.GRID);
                } else if (item.getTitle().toString().equals(getString(R.string.show_type_list))) {
                    // 文字为列表模式时，此时界面展示为平铺模式；点击后切换为列表模式
                    item.setTitle(R.string.show_type_grid);
                    mBookRecyclerList.setLayoutManager(mLinearLayoutManager);
                    mBookListModel.setDisplayMode(DisplayMode.LIST);
                }
                mBookRecyclerList.setAdapter(mBookAdapter);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 在BookModel读取完数据后的回调方法：此时数据已经准备完毕（包括书籍列表和阅读配置）
     * @param bookModelData
     */
    @Override
    public void onBookInitialized(BookModel.BookModelData bookModelData) {
        mBookRecyclerList = findViewById(R.id.books_recycler_list);
        Log.d(TAG, "onBookInitialized: " + mBookListModel.getList());
        mBookAdapter = new BookListAdapter(mBookListModel.getList(), bookModelData.getReadConfig().getDisplayMode(), mItemClickCallback);

        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mBookRecyclerList.setAdapter(mBookAdapter);
        if (bookModelData.getReadConfig().getDisplayMode().equals(DisplayMode.LIST)) {
            mBookRecyclerList.setLayoutManager(mLinearLayoutManager);
        } else if (bookModelData.getReadConfig().getDisplayMode().equals(DisplayMode.GRID)) {
            mBookRecyclerList.setLayoutManager(mStaggeredGridLayoutManager);
        }
        Log.d(TAG, bookModelData.getBooks().toString());
    }

    private BookListAdapter.Callback mItemClickCallback = new BookListAdapter.Callback() {
        @Override
        public void onClick(View v, Book book, int position) {
            Log.d(TAG, "onClick => Book: " + book.toString() + " , position: " + position);
            Intent readActivity = new Intent(BookListActivity.this, NewReadActivity.class);
//            Intent readActivity = new Intent(BookListActivity.this, ReadActivity.class);
            Log.d(TAG, "book: " + book.toString());
            Log.d(TAG, "bookConfig: " + mBookListModel.getBookConfig().toString());
            readActivity.putExtra(EXTRA_KEY_BOOK, book);
            readActivity.putExtra(EXTRA_KEY_CONFIG, mBookListModel.getBookConfig());
            startActivity(readActivity);
        }

        @Override
        public void onLongClick(View v, Book book, int position) {
            Log.d(TAG, "onLongClick => Book: " + book.toString() + " , position: " + position);
        }
    };
}
