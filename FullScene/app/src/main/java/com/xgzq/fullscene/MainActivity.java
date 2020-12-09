package com.xgzq.fullscene;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ICallback<List<Album>> {

    private HiResCard mHiResCard;
    private HiResAnim mHiResAnim;
    private RecyclerView mPlayListRecyclerView;
    private List<Album> mAlbumList;
    private PlayListAdapter mPlayListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHiResCard = findViewById(R.id.main_hi_res_card);
        mPlayListRecyclerView = findViewById(R.id.main_play_list);
        mAlbumList = new ArrayList<>();
        mPlayListAdapter = new PlayListAdapter(this, mAlbumList);
        mPlayListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPlayListRecyclerView.addItemDecoration(new PlayListAdapter.PlayListDivider(this));
        mPlayListRecyclerView.setAdapter(mPlayListAdapter);

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
        mAlbumList.addAll(albums);
        mPlayListAdapter.notifyDataSetChanged();
    }
}
