package com.xgzq.yu.reader.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.xgzq.yu.reader.model.enums.Position;
import com.xgzq.yu.reader.utils.DistanceUtil;
import com.xgzq.yu.reader.widget.abs.AbsNewReadView;
import com.xgzq.yu.reader.widget.ifs.TextDrawer;
import com.xgzq.yu.reader.widget.ifs.TurnAction;
import com.xgzq.yu.reader.widget.ifs.TurnControl;

public class NewReadView extends AbsNewReadView {

    private TurnControl mControl;

    public NewReadView(Context context) {
        this(context, null);
    }

    public NewReadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NewReadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TextDrawer drawer = new TextDrawer();
        TurnAction action = new TurnAction();
        mControl = new TurnControl(context, action);
        setControl(mControl);
        setDrawer(drawer);
    }

}
