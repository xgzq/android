package com.xgzq.android.sudoku.activity;

import com.xgzq.android.sudoku.R;
import com.xgzq.android.sudoku.interfaces.OnCompleteListener;
import com.xgzq.android.sudoku.interfaces.OnNumberItemClickListener;
import com.xgzq.android.sudoku.model.SudokuData;
import com.xgzq.android.sudoku.tools.ScreenUtil;
import com.xgzq.android.sudoku.view.Sudoku;
import com.xgzq.android.sudoku.view.SudokuNumberPanel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SudokuActivity extends Activity
		implements OnNumberItemClickListener, OnCompleteListener, android.view.View.OnClickListener
{
	private final String TAG = "Sudoku_SudokuActivity";

	private View mStatusBar;
	private Sudoku mSudoku;
	private SudokuData mData;
	private SudokuNumberPanel mNumber;

	private TextView mTimeTextView;
	private Button mPauseButton;
	private Button mStopButton;

	private int mTakeTime;
	private boolean isPause = false;
	private boolean isStop = false;

	public static final int MESSAGE_UPDATE_TIME = 1;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{

		public void handleMessage(android.os.Message msg)
		{
			int what = msg.what;
			if (what == MESSAGE_UPDATE_TIME)
			{
				mUpdateTimeRunnable.run();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sudoku);
		if (ScreenUtil.isSupportImmersionStatuBar())
		{
			ScreenUtil.setImmersionStatuBar(getWindow());
		}

		mStatusBar = findViewById(R.id.sukodu_status_bar);
		mStatusBar.getLayoutParams().height = ScreenUtil.getStatusBarHeight(this);
		mStatusBar.setBackgroundResource(R.color.sudoku_immersion_bg);

		mData = new SudokuData(10);

		mSudoku = (Sudoku) findViewById(R.id.sudoku_sudoku);
		mSudoku.setAllData(mData.getAllData());
		mSudoku.setAllHintable(mData.getHintPoints());
		mSudoku.setOnCompleteListener(this);

		mNumber = (SudokuNumberPanel) findViewById(R.id.sudoku_number_panel);
		mNumber.setOnItemClickListener(this);

		mTimeTextView = (TextView) findViewById(R.id.sudoku_tv_time);
		mPauseButton = (Button) findViewById(R.id.sudoku_btn_time_pause);
		mStopButton = (Button) findViewById(R.id.sudoku_btn_time_stop);

		mPauseButton.setOnClickListener(this);
		mStopButton.setOnClickListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mHandler.sendEmptyMessage(MESSAGE_UPDATE_TIME);
	}

	@Override
	public void onItemClick(int number, int type)
	{
		if (type == SudokuNumberPanel.TYPE_NUMBER)
		{
			if (mSudoku != null && mSudoku.getCurrentView() != null)
			{
				mSudoku.setCurrentCellData(number);
			}
			else
			{
				showSelectCellTip();
			}
		}
		else if (type == SudokuNumberPanel.TYPE_CLEAR)
		{
			if (mSudoku != null && mSudoku.getCurrentView() != null)
			{
				mSudoku.clearCurrentCellData();
			}
			else
			{
				showSelectCellTip();
			}
		}
		else if (type == SudokuNumberPanel.TYPE_CLEAR_ALL)
		{
			mSudoku.clearAllData();
			mTakeTime = 0;
			isPause = false;
			isStop = false;
			mPauseButton.setText(R.string.sudoku_pause);
			mHandler.removeCallbacks(mUpdateTimeRunnable);
			mHandler.sendEmptyMessage(MESSAGE_UPDATE_TIME);
		}
		else if (type == SudokuNumberPanel.TYPE_NEW_GAME)
		{
			getNewGame();
		}
	}

	private void showSelectCellTip()
	{
		Toast.makeText(this, R.string.sudoku_please_select_a_cell_first, Toast.LENGTH_SHORT).show();
	}

	private void getNewGame()
	{
		mSudoku.setAllData(mData.getNewData());
		mSudoku.setAllHintable(mData.getHintPoints());
		mSudoku.clearAllData();
		mTakeTime = 0;
		isPause = false;
		isStop = false;
		mPauseButton.setText(R.string.sudoku_pause);
		mHandler.removeCallbacks(mUpdateTimeRunnable);
		mHandler.sendEmptyMessage(MESSAGE_UPDATE_TIME);
	}

	private Runnable mUpdateTimeRunnable = new Runnable()
	{
		public void run()
		{
			if (mTimeTextView != null)
			{
				if (isStop)
				{
					mTimeTextView.setText(getTime(0));
					return;
				}
				if (!isPause)
				{
					mTimeTextView.setText(getTime(mTakeTime));
					mTakeTime++;
				}
				mHandler.postDelayed(this, 1000);
			}
		}
	};

	private String getTime(int time)
	{
		String result = "";
		int hours = time / 60;
		int seconds = time % 60;
		String h = String.valueOf(hours);
		String s = String.valueOf(seconds);
		if (h.length() == 1)
		{
			h = "0" + h;
		}
		if (s.length() == 1)
		{
			s = "0" + s;
		}
		result = h + ":" + s;
		return result;
	}

	@Override
	public void OnComplete(boolean isComplete)
	{
		if (isComplete)
		{
			final AlertDialog.Builder builder = new AlertDialog.Builder(SudokuActivity.this);
			builder.setTitle("提示");
			builder.setMessage("恭喜您完成游戏了！");
			builder.setCancelable(false);
			builder.setNegativeButton("退出", new OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Log.i(TAG, "[setOnCompleteListener] game over");
					Toast.makeText(SudokuActivity.this, "退出游戏！！", Toast.LENGTH_SHORT).show();
					Process.killProcess(Process.myPid());
				}
			});
			builder.setPositiveButton("开始下一局", new OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Log.i(TAG, "[setOnCompleteListener] strat next game");
					getNewGame();
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
		else
		{
			Toast.makeText(SudokuActivity.this, "很遗憾，您填写的不正确，请重新填写！", Toast.LENGTH_SHORT).show();
			mSudoku.clearAllData();
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.sudoku_btn_time_pause)
		{
			if (!isStop)
			{
				if (isPause)
				{
					isPause = false;
					mPauseButton.setText(R.string.sudoku_pause);
				}
				else
				{
					isPause = true;
					mPauseButton.setText(R.string.sudoku_continue);
				}
			}
		}
		else if (v.getId() == R.id.sudoku_btn_time_stop)
		{
			isStop = true;
			mTakeTime = 0;
		}
	}
}
