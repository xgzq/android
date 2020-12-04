package com.xgzq.yu.reader.widget.ifs;

import android.view.MotionEvent;

import com.xgzq.yu.reader.model.enums.Position;

public interface IControl {

    boolean onTouchControl(MotionEvent event);

    Position calculateTouchPosition(MotionEvent event);

}
