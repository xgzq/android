package com.xgzq.yu.reader.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.xgzq.yu.reader.R;
import com.xgzq.yu.reader.widget.BookPageView;
import com.xgzq.yu.reader.widget.abs.ABookPageView;

public class PageActivity extends AppCompatActivity {

    private static final String TAG = "XGZQ:PageActivity";

    private BookPageView mBookPageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        mBookPageView = findViewById(R.id.book_page);
        mBookPageView.setOnReadListener(new ABookPageView.OnReadListener() {
            @Override
            public void onNextStart() {
                Log.d(TAG, "onNextStart");
            }

            @Override
            public void onNextEnd() {
                Log.d(TAG, "onNextEnd");
            }

            @Override
            public void onNextCancel() {
                Log.d(TAG, "onNextCancel");
            }

            @Override
            public void onPreviousStart() {
                Log.d(TAG, "onPreviousStart");
            }

            @Override
            public void onPreviousEnd() {
                Log.d(TAG, "onPreviousEnd");
            }

            @Override
            public void onPreviousCancel() {
                Log.d(TAG, "onPreviousCancel");
            }

            @Override
            public void onMenu() {
                Log.d(TAG, "onMenu");
            }
        });
    }
}
