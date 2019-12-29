package com.xgzq.yu.reader.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    private long id;

    private String cover;
    private String name;
    private String author;
    private String desc;
    private String path;
    private long size;
    private String icon;
    private long position;

    public Book(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", cover='" + cover + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", desc='" + desc + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", icon='" + icon + '\'' +
                ", position=" + position +
                '}';
    }

    private Book(Parcel in) {
        id = in.readLong();
        cover = in.readString();
        name = in.readString();
        author = in.readString();
        desc = in.readString();
        path = in.readString();
        size = in.readLong();
        icon = in.readString();
        position = in.readLong();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(cover);
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(desc);
        dest.writeString(path);
        dest.writeLong(size);
        dest.writeString(icon);
        dest.writeLong(position);
    }
}
