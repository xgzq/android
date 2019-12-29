package com.xgzq.yu.reader.utils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {

    public static final ComputeBackgroundTransformer sComputeBackgroundTransformer;

    static {
        sComputeBackgroundTransformer = new ComputeBackgroundTransformer();
    }

    public static class ComputeBackgroundTransformer implements ObservableTransformer {

        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
        }
    }
}
