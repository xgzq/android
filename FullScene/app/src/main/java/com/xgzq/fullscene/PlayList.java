package com.xgzq.fullscene;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;

public class PlayList extends RecyclerView {
    public PlayList(@NonNull Context context) {
        this(context, null);
    }

    public PlayList(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    static class PlayListAdapter extends RecyclerView.Adapter<ViewHolder> {

        private WeakReference<List<Album>> mAlbumListRef;
        private ItemType mItemType;

        public PlayListAdapter(List<Album> albums) {
            mAlbumListRef = new WeakReference<>(albums);
        }

        public PlayListAdapter(List<Album> albums, ItemType type) {
            mAlbumListRef = new WeakReference<>(albums);
            mItemType = type;
        }

        public void setItemType(ItemType itemType) {
            mItemType = itemType;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (mItemType == ItemType.NORMAL) {
                return new NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_play_list_normal, parent));
            }
            if (mItemType == ItemType.COVER) {
                return new CoverViewHolder(LayoutInflater.from(context).inflate(R.layout.item_play_list_cover, parent));
            }
            if (mItemType == ItemType.HI_RES) {
                return new HiResViewHolder(LayoutInflater.from(context).inflate(R.layout.item_play_list_hi_res, parent));
            }
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_play_list, parent));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (mAlbumListRef.get() != null) {
                Album album = mAlbumListRef.get().get(position);
                if (album != null) {
                    final Context context = holder.mSongNameTextView.getContext();
                    holder.mSongNameTextView.setText(album.getAlbumName());
                    holder.mArtistNameTextView.setText(album.getArtistName());
                    if (mItemType == ItemType.NORMAL) {
                        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
                        normalViewHolder.mIndexTextView.setText(String.valueOf(position + 1));
                        // 在歌曲名右边设置VIP图标
                        Drawable[] songNameDrawables = normalViewHolder.mSongNameTextView.getCompoundDrawables();
                        songNameDrawables[2] = context.getDrawable(R.mipmap.placeholder);
                        // 在歌手名左边设置音质图标
                        Drawable[] artistNameDrawables = normalViewHolder.mArtistNameTextView.getCompoundDrawables();
                        artistNameDrawables[0] = context.getDrawable(R.mipmap.placeholder);
                    } else if (mItemType == ItemType.COVER) {
                        CoverViewHolder coverViewHolder = (CoverViewHolder) holder;
                        Glide.with(coverViewHolder.mCoverImageView).load(album.getAlbumCoverUrl()).into(coverViewHolder.mCoverImageView);
                        // 在歌曲名右边设置VIP图标
                        Drawable[] songNameDrawables = coverViewHolder.mSongNameTextView.getCompoundDrawables();
                        songNameDrawables[2] = context.getDrawable(R.mipmap.placeholder);
                    } else if(mItemType == ItemType.HI_RES) {
                        HiResViewHolder hiResViewHolder = (HiResViewHolder) holder;
                        hiResViewHolder.mIndexTextView.setText(String.valueOf(position + 1));
                        Glide.with(hiResViewHolder.mCoverImageView).load(album.getAlbumCoverUrl()).into(hiResViewHolder.mCoverImageView);
                        // TODO hiResIcon hiResVipIcon
                        hiResViewHolder.mHiResImageView.setImageResource(R.mipmap.placeholder);
                        hiResViewHolder.mHiResVipImageView.setImageResource(R.mipmap.placeholder);
                        hiResViewHolder.mFlacTextView.setText(R.string.app_name);
                        hiResViewHolder.mHzTextView.setText(R.string.app_name);
                        hiResViewHolder.mBitTextView.setText(R.string.app_name);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            return mAlbumListRef.get() != null ? mAlbumListRef.get().size() : 0;
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mSongNameTextView;
        TextView mArtistNameTextView;
        ImageView mPlayStatusImageView;

        ViewHolder(@NonNull View root) {
            super(root);
            mSongNameTextView = root.findViewById(R.id.item_play_list_text_song_name);
            mArtistNameTextView = root.findViewById(R.id.item_play_list_text_artist_name);
            mPlayStatusImageView = root.findViewById(R.id.item_play_list_image_play_status);
        }
    }

    /**
     * 比基础列表Item多下标和更多按钮
     */
    static class NormalViewHolder extends ViewHolder {

        TextView mIndexTextView;
        //        ImageView mVipImageView; // vip图标存放到mSongNameTextView的drawableRight里面
//        ImageView mQualityImageView; // 音质图标存放到mArtistNameTextView的drawableLeft里面
        ImageView mMoreImageView;

        NormalViewHolder(@NonNull View root) {
            super(root);
            mIndexTextView = root.findViewById(R.id.item_play_list_text_index);
            mSongNameTextView = root.findViewById(R.id.item_play_list_text_song_name);
            mArtistNameTextView = root.findViewById(R.id.item_play_list_text_artist_name);
            mPlayStatusImageView = root.findViewById(R.id.item_play_list_image_play_status);
            mMoreImageView = root.findViewById(R.id.item_play_list_image_more);
        }
    }

    /**
     * 比基础列表Item多封面图标
     */
    static class CoverViewHolder extends ViewHolder {

        ImageView mCoverImageView;
//        ImageView mVipImageView; // vip图标存放到mSongNameTextView的drawableRight里面

        CoverViewHolder(@NonNull View root) {
            super(root);
            mCoverImageView = root.findViewById(R.id.item_play_list_image_cover);
            mSongNameTextView = root.findViewById(R.id.item_play_list_text_song_name);
            mArtistNameTextView = root.findViewById(R.id.item_play_list_text_artist_name);
            mPlayStatusImageView = root.findViewById(R.id.item_play_list_image_play_status);
        }
    }

    static class HiResViewHolder extends ViewHolder {

        ImageView mCoverImageView;
        TextView mIndexTextView;
        ImageView mHiResImageView;
        ImageView mHiResVipImageView;
        TextView mFlacTextView;
        TextView mHzTextView;
        TextView mBitTextView;

        HiResViewHolder(@NonNull View root) {
            super(root);
            mCoverImageView = root.findViewById(R.id.item_play_list_image_cover);
            mIndexTextView = root.findViewById(R.id.item_play_list_text_index);
            mSongNameTextView = root.findViewById(R.id.item_play_list_text_song_name);
            mArtistNameTextView = root.findViewById(R.id.item_play_list_text_artist_name);
            mPlayStatusImageView = root.findViewById(R.id.item_play_list_image_play_status);
            mHiResImageView = root.findViewById(R.id.item_play_list_image_hi_res);
            mHiResVipImageView = root.findViewById(R.id.item_play_list_image_hi_res_vip_type);
            mFlacTextView = root.findViewById(R.id.item_play_list_text_flac);
            mHzTextView = root.findViewById(R.id.item_play_list_text_hz);
            mBitTextView = root.findViewById(R.id.item_play_list_text_bit);
        }
    }

    enum ItemType {
        NORMAL, COVER, HI_RES
    }

}
