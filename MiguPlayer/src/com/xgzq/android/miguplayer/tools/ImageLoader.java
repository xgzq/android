package com.xgzq.android.miguplayer.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.xgzq.android.miguplayer.R;
import com.xgzq.android.miguplayer.tools.BitmapUtil.ImageResize;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class ImageLoader
{

	private static final String TAG = "Migu_ImageLoader";

	/**
	 * Message.what常量：通知图片获取完成，请加载到视图中
	 */
	public static final int MESSAGE_SHOW_IMAGE = 0x0001;

	/**
	 * CPU数量
	 */
	public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

	/**
	 * THREAD_POOL_EXECUTOR 线程池的核心线程数：CPU_COUNT + 1
	 * 
	 * @see CPU_COUNT
	 */
	public static final int CORE_THREAD_COUNT = CPU_COUNT + 1;

	/**
	 * THREAD_POOL_EXECUTOR 最大线程数：CPU_COUNT * 2 + 1
	 * 
	 * @see CPU_COUNT
	 */
	public static final int MAX_THREAD_COUNT = CPU_COUNT * 2 + 1;

	/**
	 * THREAD_POOL_EXECUTOR 线程池线程最大闲置时间
	 */
	public static final long KEEP_ALIVE = 10L;

	/**
	 * 最大内存缓存
	 */
	public static final long MAX_MEMORY_CACHE_SIZE = Runtime.getRuntime().maxMemory() / 8;

	/**
	 * 最大磁盘缓存
	 */
	public static final long MAX_DISK_CACHE_SIZE = 1024 * 1024 * 50;

	public static final int DISK_CACHE_INDEX = 0;
	
	/**
	 * ImageView的TAG的KEY
	 */
	public static final int KEY_TAG_URL = R.id.listview_item_iv;

	private static final int IO_BUFFED_SIZE = 1024 * 8;

	/**
	 * 线程工厂
	 */
	private static final ThreadFactory sThreadFactory = new ThreadFactory()
	{
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r)
		{
			return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
		}
	};

	/**
	 * 线程池
	 */
	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_THREAD_COUNT, MAX_THREAD_COUNT,
			KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), sThreadFactory);

	/**
	 * 通过MainLooper创建的Handler，用来更新UI
	 */
	private Handler mMainHandler = new Handler(Looper.getMainLooper())
	{
		public void handleMessage(Message msg)
		{
			int what = msg.what;
			switch (what)
			{
			case MESSAGE_SHOW_IMAGE:
				Object obj = msg.obj;
				if (obj != null && obj instanceof LoadImageResult)
				{
					LoadImageResult result = (LoadImageResult) obj;
					ImageView iv = result.iv;
					String url = result.url;
					Bitmap bitmap = result.bitmap;
					if (url != null && iv != null && url.equals(iv.getTag(KEY_TAG_URL)) && bitmap != null)
					{
						iv.setImageBitmap(bitmap);
					}
					else
					{
						Log.e(TAG, "[handleMessage][MESSAGE_SHOW_IMAGE] url has change,ignore this load");
					}
				}
				break;

			default:
				break;
			}
		}
	};

	private Context mContext;

	/**
	 * 缩放Image的类
	 */
	private ImageResize mImageResize = new ImageResize();

	/**
	 * 内存缓存类
	 */
	private LruCache<String, Bitmap> mMemoryCache;

	/**
	 * 磁盘缓存类
	 */
	private DiskLruCache mDiskLruCache;

	private ImageLoader(Context ctx)
	{
		Log.i(TAG, "[ImageLoader]");
		this.mContext = ctx.getApplicationContext();
		this.mMemoryCache = new MemoryLruCache();
		File diskCacheDir = getDiskCacheDir("imgs");
		if (getUsableSpace(diskCacheDir) >= MAX_DISK_CACHE_SIZE)
		{
			try
			{
				mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, MAX_DISK_CACHE_SIZE);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Log.w(TAG, "[ImageLoader] free space is not enough");
		}
	}

	public static ImageLoader build(Context ctx)
	{
		Log.i(TAG, "[build]");
		return new ImageLoader(ctx);
	}

	private void addBitmapToMemoryCache(String key, Bitmap bitmap)
	{
		Log.i(TAG, "[addBitmapToMemoryCache]");
		if (mMemoryCache != null && getBitmapFromMemoryCache(key) == null)
		{
			mMemoryCache.put(key, bitmap);
		}
	}

	private Bitmap getBitmapFromMemoryCache(String key)
	{
		Log.i(TAG, "[getBitmapFromMemoryCache]");
		if (mMemoryCache != null)
		{
			return mMemoryCache.get(key);
		}
		return null;
	}

	/**
	 * @param iv
	 *            需要展示图片的ImageView
	 * @param url
	 *            图片的URL
	 */
	public void loadImage(ImageView iv, String url)
	{
		Log.i(TAG, "[loadImage] 2");
		loadImage(iv, url, 0, 0);
	}

	/**
	 * @param iv
	 * @param url
	 * @param width
	 * @param height
	 */
	public void loadImage(ImageView iv, String url, int width, int height)
	{
		Log.i(TAG, "[loadImage] 4");
		if(iv != null)
		{
			iv.setTag(KEY_TAG_URL, url);
			THREAD_POOL_EXECUTOR.execute(new LoadBitmapRunnable(iv, url, width, height));
		}
		else
		{
			Log.e(TAG, "[loadImage] ImageView is null");
		}
	}

	/**
	 * 存放到内存缓存中去的时候就已经做过压缩了，因为最开始是不存在此图片的缓存，从网络获取后根据参数对图片进行压缩，然后存放到内存缓存中
	 * 
	 * @param uri
	 * @return
	 */
	private Bitmap loadBitmapFromMemoryCache(String uri)
	{
		Log.i(TAG, "[loadBitmapFromMemoryCache]");
		final String key = MD5Util.getMd5(uri);
		Bitmap bitmap = getBitmapFromMemoryCache(key);
		Log.i(TAG, "[loadBitmapFromMemoryCache] bitmap is " + bitmap);
		return bitmap;
	}

	/**
	 * 存放到磁盘缓存中去的时候就已经做过压缩了，因为最开始是不存在此图片的缓存，从网络获取后根据参数对图片进行压缩，然后存放到磁盘缓存中
	 * 
	 * @param uri
	 * @return
	 */
	private Bitmap loadBitmapFromDiskCache(String uri, int width, int height)
	{
		Log.i(TAG, "[loadBitmapFromDiskCache]");
		if (mDiskLruCache == null)
		{
			Log.e(TAG, "[loadBitmapFromDiskCache] mDiskLruCache is null");
			return null;
		}
		Bitmap bitmap = null;
		String key = MD5Util.getMd5(uri);
		try
		{
			DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
			if (snapshot != null)
			{
				FileInputStream fis = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
				FileDescriptor fd = fis.getFD();
				bitmap = mImageResize.decodeSampledBitmapFromFileDescriptor(fd, width, height);
				if (bitmap != null)
				{
					addBitmapToMemoryCache(key, bitmap);
				}
				else
				{
					Log.e(TAG, "[loadBitmapFromDiskCache] bitmap is null");
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		Log.i(TAG, "[loadBitmapFromDiskCache] bitmap is " + bitmap);
		return bitmap;
	}

	private Bitmap loadBitmapFromHttpAndSaveToDiskCache(String uri, int width, int height)
	{
		Log.i(TAG, "[loadBitmapFromHttpAndSaveToDiskCache]");
		if (mDiskLruCache == null)
		{
			Log.e(TAG, "[loadBitmapFormHttpAndSaveToDiskCache] mDiskLruCache is null");
			return null;
		}
		String key = MD5Util.getMd5(uri);
		try
		{
			DiskLruCache.Editor editor = mDiskLruCache.edit(key);
			if (editor != null)
			{
				OutputStream os = editor.newOutputStream(DISK_CACHE_INDEX);
				if(isDownloadOutputStreamSuccessFromHttp(uri,os))
				{
					editor.commit();
				}
				else
				{
					editor.abort();
				}
				mDiskLruCache.flush();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{

		}
		return loadBitmapFromDiskCache(uri, width, height);
	}

	/**
	 * 从网络获取数据，并根据给定的宽高对图片进行压缩，并保存到内存缓存和磁盘缓存中
	 * 
	 * @param uri
	 * @param width
	 * @param height
	 * @return
	 */
	private Bitmap loadBitmapFromHttp(String uri, int width, int height)
	{
		Log.i(TAG, "[loadBitmapFromHttp]");
		Bitmap bitmap = null;
		HttpURLConnection conn = null;
		BufferedInputStream bis = null;
		try
		{
			final URL url = new URL(uri);
			conn = (HttpURLConnection) url.openConnection();
			bis = new BufferedInputStream(conn.getInputStream(), IO_BUFFED_SIZE);
			bitmap = BitmapFactory.decodeStream(bis);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (conn != null)
			{
				conn.disconnect();
			}
			try
			{
				if (bis != null)
				{
					bis.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return bitmap;
	}

	private boolean isDownloadOutputStreamSuccessFromHttp(String uri, OutputStream os)
	{
		Log.i(TAG, "[isDownloadOutputStreamSuccessFromHttp]");
		HttpURLConnection conn = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try
		{
			final URL url = new URL(uri);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(14000);
			bis = new BufferedInputStream(conn.getInputStream(), IO_BUFFED_SIZE);
			bos = new BufferedOutputStream(os, IO_BUFFED_SIZE);
			int len = 0;
			while ((len = bis.read()) != -1)
			{
				bos.write(len);
			}
			return true;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (conn != null)
			{
				conn.disconnect();
			}
			try
			{
				if (bis != null)
				{
					bis.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				if (bos != null)
				{
					bos.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	private File getDiskCacheDir(String dirName)
	{
		Log.i(TAG, "[getDiskCacheDir]");
		File diskCacheDir = null;
		String cacheDir;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			cacheDir = mContext.getExternalCacheDir().getPath();
			Log.i(TAG, "[getDiskCacheDir] cache is in external cache dir");
		}
		else
		{
			cacheDir = mContext.getCacheDir().getPath();
			Log.i(TAG, "[getDiskCacheDir] cache is in internal cache dir");
		}
		diskCacheDir = new File(cacheDir, dirName);
		if (!diskCacheDir.exists())
		{
			diskCacheDir.mkdirs();
		}
		Log.i(TAG, "[getDiskCacheDir] diskCacheDir is " + diskCacheDir.getPath());
		return diskCacheDir;
	}

	@SuppressWarnings("deprecation")
	private long getUsableSpace(File path)
	{
		Log.i(TAG, "[getUsableSpace]");
		long size = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		{
			size = path.getUsableSpace();
		}
		else
		{
			final StatFs stats = new StatFs(path.getPath());
			size = stats.getBlockSize() * stats.getAvailableBlocks();
		}
		Log.i(TAG, "[getUsableSpace] size is " + size);
		return size;
	}

	class MemoryLruCache extends LruCache<String, Bitmap>
	{
		public MemoryLruCache()
		{
			super((int) MAX_MEMORY_CACHE_SIZE);
		}

		@Override
		protected int sizeOf(String key, Bitmap bitmap)
		{
			int size = bitmap.getRowBytes() * bitmap.getHeight();
			
			Log.i(TAG, "[sizeOf] max memory is " + Runtime.getRuntime().maxMemory());
			Log.i(TAG, "[sizeOf] max size is " + MAX_MEMORY_CACHE_SIZE);
			Log.i(TAG, "[sizeOf] size is " + size);
			return size;
		}
	}

	class LoadBitmapRunnable implements Runnable
	{
		private ImageView iv;
		private String url;
		private int width;
		private int height;

		public LoadBitmapRunnable(ImageView iv, String url, int width, int height)
		{
			this.iv = iv;
			this.url = url;
			this.width = width;
			this.height = height;
		}

		@Override
		public void run()
		{
			Log.i(TAG, "[LoadBitmapRunnable][run] current thread id is " + Thread.currentThread());
			Bitmap bitmap = null;
			bitmap = loadBitmapFromMemoryCache(url);
			if (bitmap == null)
			{
				bitmap = loadBitmapFromDiskCache(url, width, height);
				if (bitmap == null)
				{
					bitmap = loadBitmapFromHttpAndSaveToDiskCache(url, width, height);
					if(bitmap == null)
					{
						bitmap = loadBitmapFromHttp(url, width, height);
					}
				}
			}
			if (bitmap != null)
			{
				LoadImageResult result = new LoadImageResult(iv, url, bitmap);
				mMainHandler.obtainMessage(MESSAGE_SHOW_IMAGE, result).sendToTarget();
			}
			else
			{
				Log.e(TAG, "[LoadBitmapRunnable][run] load bitmap failed.url is " + url);
			}
		}
	}

	class LoadImageResult
	{
		public ImageView iv;
		public String url;
		public Bitmap bitmap;

		public LoadImageResult(ImageView iv, String url, Bitmap bitmap)
		{
			this.iv = iv;
			this.url = url;
			this.bitmap = bitmap;
		}
	}
}
