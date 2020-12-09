package com.xgzq.fullscene;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder> {

    private WeakReference<Context> mContextRef;
    private List<Album> mMusicList;
    private LayoutInflater mLayoutInflater;

    public PlayListAdapter(Context context, List<Album> musicList) {
        mContextRef = new WeakReference<>(context);
        mMusicList = musicList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayListViewHolder(mLayoutInflater.inflate(R.layout.item_play_list_hi_res, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListViewHolder holder, int position) {
        Album album = mMusicList.get(position);
        holder.indexText.setText(String.valueOf(position + 1));
        holder.songNameText.setText(album.getAlbumName());
        holder.artistNameText.setText(album.getArtistName());
        Glide.with(mContextRef.get()).load(album.getAlbumCoverUrl()).into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return mMusicList != null ? mMusicList.size() : 0;
    }

    static class PlayListViewHolder extends RecyclerView.ViewHolder {

        TextView indexText;
        TextView songNameText;
        TextView artistNameText;
        ImageView coverImage;

        PlayListViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.item_play_list_image_cover);
            indexText = itemView.findViewById(R.id.item_play_list_text_index);
            songNameText = itemView.findViewById(R.id.item_play_list_text_song_name);
            artistNameText = itemView.findViewById(R.id.item_play_list_text_artist_name);
        }
    }

    static class PlayListDivider extends RecyclerView.ItemDecoration {

        private Paint mDividerPaint;
        private Context mContext;
        private float dividerHeight;

        PlayListDivider(Context context) {
            mDividerPaint = new Paint();
            mDividerPaint.setColor(Color.RED);
            mContext = context;
            dividerHeight = context.getResources().getDimension(R.dimen.divider_height);
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDraw(c, parent, state);
            int childCount = parent.getChildCount();
            int right = parent.getWidth() - parent.getPaddingRight();
            for (int i = 0; i < childCount - 1; i++) {
                View view = parent.getChildAt(i);
                View songName = view.findViewById(R.id.item_play_list_ll_song_name);
                float top = view.getBottom();
                float bottom = view.getBottom() + dividerHeight;
                c.drawRect(songName.getLeft(), top, right, bottom, mDividerPaint);
            }
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = (int) dividerHeight;
        }
    }
}
