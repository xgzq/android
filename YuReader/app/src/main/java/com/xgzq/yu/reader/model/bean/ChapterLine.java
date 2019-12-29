package com.xgzq.yu.reader.model.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ChapterLine {

    public final class TextLine {
        private float offsetY;
        private String content;

        public TextLine(float offsetY, String content) {
            this.offsetY = offsetY;
            this.content = content;
        }

        public float getOffsetY() {
            return this.offsetY;
        }

        public String getContent() {
            return this.content;
        }

        @Override
        public String toString() {
            return "TextLine{" +
                    "offsetY=" + offsetY +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    private HashMap<Integer, ArrayList<TextLine>> mChapterContentMap;

    public ChapterLine() {
        // 初始容量为分页数，一般在5到20左右
        mChapterContentMap = new HashMap<>(20);
    }

    public void addTextLine(int pageOffset, TextLine textLine) {
        if (textLine == null) {
            throw new NullPointerException("text line is null!");
        }
        if (mChapterContentMap.containsKey(pageOffset)) {
            ArrayList<TextLine> textLines = mChapterContentMap.get(pageOffset);
            if (textLines == null) {
                // 初始容量为每页的行数，大概20到30
                textLines = new ArrayList<>(30);
            }
            textLines.add(textLine);
        } else {
            ArrayList<TextLine> textLines = new ArrayList<>(30);
            textLines.add(textLine);
            mChapterContentMap.put(pageOffset, textLines);
        }
    }

    public void addTextLine(int pageOffset, float offsetY, String content) {
        addTextLine(pageOffset, new TextLine(offsetY, content));
    }

    public void clearPage(int pageOffset) {
        if (mChapterContentMap.containsKey(pageOffset)) {
            mChapterContentMap.remove(pageOffset);
        }
    }

    public void clearAll() {
        mChapterContentMap.clear();
    }

    public int pageCount() {
        return mChapterContentMap != null ? mChapterContentMap.size() : -1;
    }

    public int lineCount(Integer pageOffset) {
        if (mChapterContentMap.containsKey(pageOffset)) {
            return mChapterContentMap.get(pageOffset).size();
        }
        return -1;
    }

    public Set<Integer> keys() {
        return mChapterContentMap.keySet();
    }

    public ArrayList<TextLine> get(Integer pageOffset) {
        return mChapterContentMap.containsKey(pageOffset) ? mChapterContentMap.get(pageOffset) : null;
    }

    public TextLine getLine(Integer pageOffset, int lineOffset) {
        if (mChapterContentMap.containsKey(pageOffset)) {
            ArrayList<TextLine> textLines = mChapterContentMap.get(pageOffset);
            if (textLines != null && lineOffset < textLines.size()) {
                return textLines.get(lineOffset);
            }
            return null;
        }
        return null;
    }

    public void replaceAll(HashMap<? extends Integer, ? extends ArrayList<TextLine>> map) {
        this.mChapterContentMap.clear();
        this.mChapterContentMap.putAll(map);
    }
}
