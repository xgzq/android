package com.xgzq.yu.reader.model.model;


import android.util.Log;

import com.xgzq.yu.reader.model.bean.Book;
import com.xgzq.yu.reader.model.bean.Chapter;
import com.xgzq.yu.reader.model.db.utils.ChapterSqlUtil;
import com.xgzq.yu.reader.utils.ChapterUtil;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReadModel {

    public interface IChapterCallback {

        /**
         * 开始获取：运行在调用者的线程中
         *
         * @param disposable
         */
        void onStart(Disposable disposable);

        /**
         * 出错失败：运行于UI线程
         *
         * @param throwable
         */
        void onError(Throwable throwable);

        /**
         * 成功：运行于UI线程
         *
         * @param chapters
         */
        void onSuccess(List<Chapter> chapters);

        /**
         * 失败：运行于UI线程
         */
        void onFail();

        /**
         * 完成：运行于UI线程
         * onSuccess, onFail, onError最终都会调用到onComplete
         */
        void onComplete();
    }

    public static final String TAG = "XGZQ:ReadModel";

    private IChapterCallback mChapterCallback;
    private Book mBook;

    public ReadModel(Book book, IChapterCallback chapterCallback) {
        if (book == null) {
            throw new NullPointerException("ReadModel => book is null!");
        }
        this.mBook = book;
        this.mChapterCallback = chapterCallback;
    }

    public void setChapterCallback(IChapterCallback chapterCallback) {
        this.mChapterCallback = chapterCallback;
    }

    /**
     * 先从数据库中获取，如果没有则分章，并保存到数据库
     */
    public void getChapterList() {
        // 从数据库获取章节列表
        Observable<List<Chapter>> databaseObservable = Observable.create(new ObservableOnSubscribe<List<Chapter>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Chapter>> emitter) throws Exception {
                Log.d(TAG, "databaseObservable => subscribe");
                List<Chapter> chapterList = ChapterSqlUtil.getChapterList(mBook.getId());
                if (chapterList != null) {
                    emitter.onNext(chapterList);
                } else {
                    emitter.onComplete();
                }
            }
        });

        // 从全部内容中通过正则分章节
        Observable<List<Chapter>> regularObservable = Observable.create(new ObservableOnSubscribe<List<Chapter>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Chapter>> emitter) throws Exception {
                Log.d(TAG, "regularObservable => subscribe");
                List<Chapter> chapterList = ChapterUtil.getChapterList(mBook.getPath());
                if (chapterList != null) {
                    emitter.onNext(chapterList);
                } else {
                    emitter.onComplete();
                }
            }
        });

        // firstElement里面的onSuccess和onComplete只能调用一个，数据都为null的时候调用onComplete，有数据时调用onNext => onSuccess
        // 暂定正常获取到数据（不管是从数据库还是重新分章）时调用onSuccess返回真实数据，当都没有获取到章节数据是调用onFail
        Observable
                .concat(databaseObservable, regularObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .firstElement()
                .subscribe(new MaybeObserver<List<Chapter>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        if (mChapterCallback != null) {
                            mChapterCallback.onStart(disposable);
                        }
                    }

                    @Override
                    public void onSuccess(List<Chapter> chapterList) {
                        if (mChapterCallback != null) {
                            mChapterCallback.onSuccess(chapterList);
                            mChapterCallback.onComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mChapterCallback != null) {
                            mChapterCallback.onError(e);
                            mChapterCallback.onComplete();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mChapterCallback != null) {
                            mChapterCallback.onFail();
                            mChapterCallback.onComplete();
                        }
                    }
                });
    }
}
