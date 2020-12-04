package com.xgzq.yu.reader.model.util;

import java.util.LinkedList;
import java.util.List;

public class CachePool<T> {

    private LinkedList<T> mList;

    private int mBeforeSize;
    private int mAfterSize;

    public CachePool(int beforeSize, int afterSize) {
        mList = new LinkedList<>();

        mBeforeSize = beforeSize;
        mAfterSize = afterSize;
    }

    public synchronized T getCurrent() {
        if (mList.size() <= mBeforeSize) {
            throw new IllegalStateException("cache pool size is less than beforeSize");
        }
        return mList.get(mBeforeSize);
    }

    /**
     * 设置所有
     *
     * @param list
     */
    public synchronized void setBefore(List<T> list) {
        if (list.size() != mBeforeSize) {
            throw new IllegalStateException("list size must be beforeSize");
        }
        mList.addAll(0, list);
    }

    public synchronized void setCurrent(T t) {
        if (mList.size() != mBeforeSize) {
            throw new IllegalStateException("must be first set before");
        }
        mList.add(mBeforeSize, t);
    }

    public synchronized void setAfter(List<T> list) {
        if (list.size() != mAfterSize) {
            throw new IllegalStateException("list size must be afterSize");
        }
        if (mList.size() != mBeforeSize + 1) {
            throw new IllegalStateException("must be first set before and current");
        }
        mList.addAll(mBeforeSize + 1, list);
    }

    public synchronized void addFirst(T t) {
        mList.addFirst(t);
    }

    public synchronized void addLast(T t) {
        mList.addLast(t);
    }

    public synchronized void add(int index, T t) {
        mList.add(index, t);
    }

    /**
     * 往后移动一个元素，并返回当前元素
     * 需要在后面往最后一个位置添加一个元素
     * @return
     */
    public synchronized T next() {
        if(mList.isEmpty()){
            throw new IllegalStateException("list is empty!");
        }
        mList.removeFirst();
        return getCurrent();
    }

    /**
     * 往前移动一个元素，并返回当前元素
     * 需要在后面往第一个位置添加一个元素
     * @return
     */
    public synchronized T previous() {
        if(mList.isEmpty()){
            throw new IllegalStateException("list is empty!");
        }
        mList.removeLast();
        return mList.get(mBeforeSize - 1);
    }

    /**
     * 往后移动一位，删除第一个元素，并加入一个新的元素到最后的位置
     * 返回当前位置元素
     *
     * @param t 新插入的元素
     * @return 当前元素
     */
    public synchronized T next(T t) {
        if(mList.isEmpty()){
            throw new IllegalStateException("list is empty!");
        }
        mList.removeFirst();
        mList.addLast(t);
        return getCurrent();
    }

    /**
     * 往前移动一位，删除最后一个元素，并加入一个新的元素到第一个位置
     *
     * @param t 新插入的元素
     * @return 当前元素
     */
    public synchronized T previous(T t) {
        if(mList.isEmpty()){
            throw new IllegalStateException("list is empty!");
        }
        mList.removeLast();
        mList.addFirst(t);
        return getCurrent();
    }
}
