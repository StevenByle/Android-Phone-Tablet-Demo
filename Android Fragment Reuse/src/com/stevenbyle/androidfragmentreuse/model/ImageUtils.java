package com.stevenbyle.androidfragmentreuse.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Image utility class for image manipulations.
 * 
 * 
 * @author Steven Byle
 */
public class ImageUtils {
	private static final String TAG = ImageUtils.class.getSimpleName();

	/**
	 * Load a bitmap equal to or larger than requested dimensions, loading the
	 * smallest version into memory.
	 * 
	 * @param resources
	 * @param resourceId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources resources, int resourceId, int reqWidth, int reqHeight) {

		// First decode check the raw image dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resourceId, options);

		// Calculate the factor to scale down by depending on the desired height
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inScaled = false;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bm = BitmapFactory.decodeResource(resources, resourceId, options);
		return bm;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int imageHeight = options.outHeight;
		final int imageWidth = options.outWidth;

		// Calculate the factor to scale down by depending on the desired height
		int inSampleSize = 1;
		if (imageHeight > reqHeight || imageWidth > reqWidth) {

			final int heightRatio = imageHeight / reqHeight;
			final int widthRatio = imageWidth / reqWidth;

			// Choose the smallest factor to scale down by, so the scaled image is always slightly larger than needed
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		Log.i(TAG, "calculateInSampleSize: imageHeight = " + imageHeight + " imageWidth = " + imageWidth
				+ " reqWidth = " + reqWidth + " reqHeight = " + reqHeight
				+ " inSampleSize = " + inSampleSize);

		return inSampleSize;
	}
}
