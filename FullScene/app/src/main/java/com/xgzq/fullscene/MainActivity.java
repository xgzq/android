package com.xgzq.fullscene;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ICallback<List<Album>>{

    private HiResCard mHiResCard;
    private HiResAnim mHiResAnim;
    private HiResPager mHiResPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHiResCard = findViewById(R.id.main_hi_res_card);

        DataCenter.getAlbumData(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void callback(List<Album> albums) {
        mHiResCard.setData(albums);
        List<String> urls = new ArrayList<>();
        for (Album album : albums) {
            urls.add(album.getAlbumCoverUrl());
        }
//        mHiResAnim.addImageUrl(urls);
        mHiResPager.addData(urls);
    }
}
