package com.xgzq.android.miguplayer.tools;

import java.io.FileDescriptor;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class BitmapUtil
{

	public static class ImageResize
	{
		private static final String TAG = "Migu_ImageResize";

		public ImageResize()
		{

		}

		public Bitmap decodeSampledBitmapFormResources(Resources res, int resId, int width, int height)
		{
			Log.i(TAG, "[decodeSampledBitmapFormResources]");
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			BitmapFactory.decodeResource(res, resId, options);
			options.inSampleSize = calculateInSampleSize(options, width, height);

			options.inJustDecodeBounds = false;

			return BitmapFactory.decodeResource(res, resId, options);
		}

		public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int width, int height)
		{
			Log.i(TAG, "[decodeSampledBitmapFromFileDescriptor]");
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			BitmapFactory.decodeFileDescriptor(fd, null, options);
			options.inSampleSize = calculateInSampleSize(options, width, height);

			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFileDescriptor(fd, null, options);
		}

		private int calculateInSampleSize(Options options, int width, int height)
		{
			if (width == 0 || height == 0)
			{
				return 1;
			}
			final int w = options.outWidth;
			final int h = options.outHeight;
			int inSampleSize = 1;
			if (w > width || h > height)
			{
				final int halfWidth = w / 2;
				final int halfHeight = h / 2;
				
				while((halfHeight / inSampleSize) >= height && (halfWidth / inSampleSize) >= width)
				{
					inSampleSize *= 2;
				}
			}
			Log.i(TAG, "[calculateInSampleSize] inSampleSize is " + inSampleSize);
			return inSampleSize;
		}
	}
}
