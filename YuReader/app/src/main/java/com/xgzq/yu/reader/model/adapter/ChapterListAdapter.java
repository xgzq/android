package com.xgzq.yu.reader.model.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xgzq.yu.reader.App;
import com.xgzq.yu.reader.R;
import com.xgzq.yu.reader.model.bean.Chapter;
import com.xgzq.yu.reader.model.model.BookModel;
import com.xgzq.yu.reader.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ChapterViewHolder> {

    public static final int SIZE_CHAPTER = 200;

    private List<Chapter> mChapterList;
    private OnClickListener mOnClickListener;
    private BookModel.ReadConfig mReadConfig;

    public ChapterListAdapter(List<Chapter> chapterList, BookModel.ReadConfig readConfig) {
        this(chapterList, readConfig, null);
    }

    public ChapterListAdapter(List<Chapter> chapterList, BookModel.ReadConfig readConfig, OnClickListener onClickListener) {
        if (chapterList == null) {
            mChapterList = new ArrayList<>(SIZE_CHAPTER);
        } else {
            mChapterList = chapterList;
        }
        if (readConfig == null) {
            throw new NullPointerException("readConfig is null!");
        }
        mOnClickListener = onClickListener;
        mReadConfig = readConfig;
    }


    @Override
    public int getItemCount() {
        return mChapterList.size();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chapter_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, final int position) {
        final Chapter chapter = mChapterList.get(position);
        final boolean read = mReadConfig.getChapterIndex() >= chapter.getStart();
        holder.index.setText(StringUtil.leftShift(String.valueOf(chapter.getIndex()), 5));
        holder.title.setText(chapter.getTitle());
        holder.title.setTextColor(App.getApp().getResources().getColor(read ? R.color.chapter_read : R.color.chapter_unread));
        holder.info.setText(read ? "已读" : "未读");
        holder.info.setTextColor(App.getApp().getResources().getColor(read ? R.color.chapter_read : R.color.chapter_unread));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v, chapter, position);
                }
            }
        });
    }


    public class ChapterViewHolder extends RecyclerView.ViewHolder {

        private TextView index;
        private TextView title;
        private TextView info;
        private ImageView icon;


        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);

            index = itemView.findViewById(R.id.layout_chapter_item_tv_index);
            title = itemView.findViewById(R.id.layout_chapter_item_tv_title);
            info = itemView.findViewById(R.id.layout_chapter_item_tv_info);
            icon = itemView.findViewById(R.id.layout_chapter_item_img_icon);
        }
    }

    public interface OnClickListener {
        void onClick(View view, Chapter chapter, int position);
    }
}
