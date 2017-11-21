package com.xgzq.android.miguplayer.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.xgzq.android.miguplayer.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;

public class HandlerTestActivity extends Activity
{
	private static final String TAG = "HandlerTestActivity";

	private ThreadLocal<String> mThreadLocal;
	private ThreadLocal<Point> mPointThreadLocal;

	private Button mStubButton;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_view_test);

		mStubButton = (Button) findViewById(R.id.stub_test_btn);

		mThreadLocal = new ThreadLocal<String>();
		mPointThreadLocal = new ThreadLocal<Point>();

		mThreadLocal.set("MainThread-----");
		Log.i(TAG, "[MainThread->getString] " + mThreadLocal.get());

		mPointThreadLocal.set(new Point(111, 111));
		Log.i(TAG, "[MainThread->getPoint] " + mPointThreadLocal.get());

		new Thread("Thread#1")
		{
			@Override
			public void run()
			{
				mThreadLocal.set("#1-Thread++++++++");
				Log.i(TAG, "[Thread#1->getString] " + mThreadLocal.get());
				Looper.prepare();
				Handler handler = new Handler()
				{
					public void handleMessage(Message msg)
					{
						if (msg.what == 1)
						{
							mStubButton.setText("Thread-1-Modify");
							getLooper().quit();
						}
					}
				};
				handler.sendEmptyMessage(1);
				Looper.loop();
				mPointThreadLocal.set(new Point(222, 222));
				Log.i(TAG, "[Thread#1->getPoint] " + mPointThreadLocal.get());
			}
		}.start();

		new Thread("Thread#2")
		{
			public void run()
			{
				mThreadLocal.set("#2-Thread========");
				Log.i(TAG, "[Thread#2->get] " + mThreadLocal.get());
				
				Handler handler = new Handler()
				{
					public void handleMessage(Message msg)
					{
						if (msg.what == 1)
						{
							mStubButton.setText("Thread-2-Modify");
						}
					}
				};
				handler.sendEmptyMessage(1);

				mPointThreadLocal.set(new Point(333, 333));
				Log.i(TAG, "[Thread#2->getPoint] " + mPointThreadLocal.get());
			}
		}.start();

		/*new AsyncTask<Void, Void, Void>()
		{

			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				Log.i(TAG, "AsyncTask --> onPreExecute --> " + Thread.currentThread().getId());
			}

			@Override
			protected Void doInBackground(Void... params)
			{
				publishProgress();
				Log.i(TAG, "AsyncTask --> doInBackground --> " + Thread.currentThread().getId());
				return null;
			}

			@Override
			protected void onProgressUpdate(Void... values)
			{
				super.onProgressUpdate(values);
				Log.i(TAG, "AsyncTask --> onProgressUpdate --> " + Thread.currentThread().getId());
			}

			@Override
			protected void onPostExecute(Void result)
			{
				super.onPostExecute(result);
				Log.i(TAG, "AsyncTask --> onPostExecute --> " + Thread.currentThread().getId());
			}

			@Override
			protected void onCancelled()
			{

			}

			@Override
			protected void onCancelled(Void result)
			{

			}
		}.execute();*/

//		new InnerAsyncTask("Task1").execute();
//		new InnerAsyncTask("Task2").execute();
//		new InnerAsyncTask("Task3").execute();
//		new InnerAsyncTask("Task4").execute();
//		new InnerAsyncTask("Task5").execute();
//		new InnerAsyncTask1("Task6").execute();
//		new InnerAsyncTask1("Task7").execute();
//		new InnerAsyncTask1("Task8").execute();
//		new InnerAsyncTask1("Task9").execute();

		// if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		// {
		// new InnerAsyncTask("Task1").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
		// "");
		// new InnerAsyncTask("Task2").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
		// "");
		// new InnerAsyncTask("Task3").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
		// "");
		// new InnerAsyncTask("Task4").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
		// "");
		// new InnerAsyncTask("Task5").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
		// "");
		// new
		// InnerAsyncTask1("Task6").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
		// "");
		// new
		// InnerAsyncTask1("Task7").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
		// "");
		// new
		// InnerAsyncTask1("Task8").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
		// "");
		// new
		// InnerAsyncTask1("Task9").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
		// "");
		// }

		HandlerThread handlerThread = new HandlerThread("HandlerThread_#1")
		{
			@Override
			public void run()
			{
				super.run();
				Log.i(TAG, "[handlerThread->name] " + getName());
				mStubButton.setText("HandlerThread_#1_Set");
			}
		};
		handlerThread.start();
		Log.i(TAG, "[handlerThread.quit()] ");
		handlerThread.quit();
		
	}

	class InnerAsyncTask extends AsyncTask<String, Void, String>
	{

		private String mName = "InnerAsyncTask";

		public InnerAsyncTask(String name)
		{
			this.mName = name;
		}

		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				Thread.sleep(3000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			return mName;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Log.i(TAG, result + " execute finish at " + df.format(new Date()));
		}
	}

	class InnerAsyncTask1 extends AsyncTask<String, Void, String>
	{

		private String mName = "InnerAsyncTask1";

		public InnerAsyncTask1(String name)
		{
			this.mName = name;
		}

		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			return mName;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Log.i(TAG, result + "--1-- execute finish at " + df.format(new Date()));
		}
	}
}
