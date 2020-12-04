package com.xgzq.yu.reader.widget.ifs;

import android.view.MotionEvent;

public interface IAction {

    void toNextPage(MotionEvent start);

    void toPrevPage(MotionEvent start);

    void onMoving(MotionEvent start, MotionEvent current);

    void onTouchStart(MotionEvent start);
}
