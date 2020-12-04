package com.xgzq.yu.reader.widget.ifs;

import android.graphics.Canvas;
import android.graphics.Color;

public class TextDrawer implements IDrawer {

    public TextDrawer() {

    }

    @Override
    public void onDrawContent(Canvas canvas) {
        canvas.drawColor(Color.YELLOW);
    }
}
