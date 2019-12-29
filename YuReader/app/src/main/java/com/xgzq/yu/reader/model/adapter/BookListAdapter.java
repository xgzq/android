package com.xgzq.yu.reader.model.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xgzq.yu.reader.App;
import com.xgzq.yu.reader.R;
import com.xgzq.yu.reader.model.bean.Book;
import com.xgzq.yu.reader.model.enums.DisplayMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    private static final String TAG = "XGZQ:BookListAdapter";

    private List<Book> mBookList;
    private DisplayMode mDisplayMode;

    private Callback mCallback;
    
    public BookListAdapter(List<Book> bookList, DisplayMode model, Callback callback) {
        if (bookList == null) {
            this.mBookList = new ArrayList<>(20);
        } else {
            this.mBookList = bookList;
        }
        this.mDisplayMode = model;
        this.mCallback = callback;
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "BookViewHolder: " + mDisplayMode);
        return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, final int position) {
        final Book book = mBookList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(App.getApp(), book.getPath(), Toast.LENGTH_SHORT).show();
                if (mCallback != null) {
                    mCallback.onClick(v, book, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(App.getApp(), "长按:" + position, Toast.LENGTH_SHORT).show();
                if (mCallback != null) {
                    mCallback.onLongClick(v, book, position);
                }
                return true;
            }
        });

        if (!TextUtils.isEmpty(book.getCover())) {
            // TODO:异步从网络或者本地获取封面再设置到ImageView上
            // Glide.with(YuApplication.getInstance().getApplicationContext()).load(messageRecord.getUrl()).into(vh.image);
        }
        holder.author.setText(book.getAuthor());
        holder.desc.setText(book.getDesc());
        holder.name.setText(book.getPath().substring(book.getPath().lastIndexOf(File.separator) + 1));
        if (book.getPosition() <= 0 || book.getSize() <= 0) {
            holder.progress.setText("还未阅读");
        } else {
            float progress = book.getPosition() * 1.0f / book.getSize() * 100f;
            holder.progress.setText(progress + "%");
        }
    }


    public class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView name;
        TextView author;
        TextView desc;
        TextView progress;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.layout_book_item_img_cover);
            name = itemView.findViewById(R.id.layout_book_item_tv_name);
            author = itemView.findViewById(R.id.layout_book_item_tv_author);
            desc = itemView.findViewById(R.id.layout_book_item_tv_desc);
            progress = itemView.findViewById(R.id.layout_book_item_tv_progress);
        }
    }

    /**
     * item点击回调接口
     */
    public interface Callback {
        /**
         * 点击事件回调方法
         *
         * @param v        被点击的View
         * @param book     被点击的Item数据book
         * @param position 被点击的Item的下标
         */
        void onClick(View v, Book book, int position);

        /**
         * 长按事件回调方法
         *
         * @param v        被点击的View
         * @param book     被点击的Item数据book
         * @param position 被点击的Item的下标
         */
        void onLongClick(View v, Book book, int position);
    }
}
