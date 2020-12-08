package com.xgzq.fullscene;

public class Album {

    private String mAlbumName;
    private String mArtistName;
    private String mAlbumTimes;
    private String mAlbumCoverUrl;

    public Album(String albumName, String artistName, String albumTimes, String albumCoverUrl) {
        mAlbumName = albumName;
        mArtistName = artistName;
        mAlbumTimes = albumTimes;
        mAlbumCoverUrl = albumCoverUrl;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        mAlbumName = albumName;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public void setArtistName(String artistName) {
        mArtistName = artistName;
    }

    public String getAlbumTimes() {
        return mAlbumTimes;
    }

    public void setAlbumTimes(String albumTimes) {
        mAlbumTimes = albumTimes;
    }

    public String getAlbumCoverUrl() {
        return mAlbumCoverUrl;
    }

    public void setAlbumCoverUrl(String albumCoverUrl) {
        mAlbumCoverUrl = albumCoverUrl;
    }
}
