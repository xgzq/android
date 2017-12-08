package com.xgzq.android.reader.model;

public class Chapter
{
    private int mIndex;
    private String mTitle;
    private String mContent;
    private int mStart;
    private int mEnd;

    public Chapter()
    {
    }

    public Chapter(int mIndex, String mTitle, String mContent, int mStart, int mEnd)
    {
        this.mIndex = mIndex;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mStart = mStart;
        this.mEnd = mEnd;
    }

    public int getIndex()
    {
        return mIndex;
    }

    public void setIndex(int mIndex)
    {
        this.mIndex = mIndex;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String mTitle)
    {
        this.mTitle = mTitle;
    }

    public String getContent()
    {
        return mContent;
    }

    public void setContent(String mContent)
    {
        this.mContent = mContent;
    }

    public int getStart()
    {
        return mStart;
    }

    public void setStart(int mStart)
    {
        this.mStart = mStart;
    }

    public int getEnd()
    {
        return mEnd;
    }

    public void setEnd(int mEnd)
    {
        this.mEnd = mEnd;
    }

    @Override public String toString()
    {
        return "Chapter [mIndex=" + mIndex + ", mTitle=" + mTitle + ", mContent=" + mContent + "]";
    }

}
