package com.xgzq.yu.reader.utils;

import android.util.Log;

import com.xgzq.yu.reader.model.bean.Chapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChapterUtil {


    private static final String TAG = "XGZQ:ChapterUtil";
    public static final String MATCH_CHAPTER_TEXT = "第[0123456789零一二三四五六七八九十百千万①②③④⑤⑥⑦⑧⑨⑩壹贰叁肆伍陆柒捌玖拾佰仟萬]{0,9}[章节回部篇][ ]*[\\S]+";

    public static List<Chapter> getChapterList(String path) {
        if (path == null) {
            throw new NullPointerException("getChapterList => path is null!");
        }
        return getChapterList(new File(path));
    }

    public static List<Chapter> getChapterList(File file) {
        if (file == null) {
            throw new NullPointerException("getChapterList => file is null!");
        }
        String content = FileUtil.readFile(file);
        List<Chapter> mChapters = new ArrayList<>();
        Log.i(TAG, "getChapters => content length is " + content.length());
        Pattern p = Pattern.compile(MATCH_CHAPTER_TEXT);
        Matcher m = p.matcher(content);
        int start = -1;
        int end = -1;
        int index = 0;
        Chapter chapter = null;
        while (m.find()) {
            // 如果上一个字符不是(10)换行符则不添加章节
            if (m.start() > 0 && (int) content.charAt(m.start() - 1) != 10) {
                continue;
            }
            chapter = new Chapter();

            // 上一章的内容的结束位置
            end = m.start();

            // 索引，第index章，从0开始
            chapter.setIndex(index);

            chapter.setTitle(m.group().trim());
//            Log.d(TAG, m.group().trim());

            // 设置这一章（包括标题）的开始下标
            chapter.setStart(m.start());

            // start != -1
            // 说明不是第一次循环，有上一章的内容，需要获取出来。由于当前章节内容的结束的位置只能在下一章节中获取到（所以需要放到end更新后，start更新前）
            // end表示这章标题的下标位置，也就是上一章内容的结束位置；start还未更新时表示上一章标题的结束位置，也就是上一章内容的开始位置；更新后表示本章标题的结束位置，也就是本章内容的开始位置
            if (start != -1) {
                // 给上一章的内容赋值
                mChapters.get(mChapters.size() - 1).setContent(content.substring(start, end));
                // 设置上一章的结束的位置
                mChapters.get(mChapters.size() - 1).setEnd(end);
            }

            // 这一章的内容的开始的位置
            start = m.end();

            // 判断是否有重复章节，如果有就不添加到队列里面
            if (!mChapters.isEmpty() && mChapters.get(mChapters.size() - 1).getTitle().contains(m.group().trim())) {
                continue;
            }
            mChapters.add(chapter);
            index++;
        }
        if (mChapters.size() > 0) {
            // 设置最后一次的内容
            mChapters.get(mChapters.size() - 1).setContent(content.substring(start));
            // 设置最后一次的end
            mChapters.get(mChapters.size() - 1).setEnd(content.length());
        }
        return mChapters;
    }
}
