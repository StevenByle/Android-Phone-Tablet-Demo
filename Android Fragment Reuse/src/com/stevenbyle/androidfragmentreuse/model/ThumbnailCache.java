package com.stevenbyle.androidfragmentreuse.model;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

/**
 * Least recently used cache implementation for holding thumbnail images in
 * memory instead of loading from storage.
 * 
 * @author Steven Byle
 */
public class ThumbnailCache extends LruCache<Long, Bitmap> {

	/**
	 * @param maxSizeBytes
	 */
	public ThumbnailCache(int maxSizeBytes) {
		super(maxSizeBytes);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	protected int sizeOf(Long key, Bitmap value) {
		// Return size of in-memory Bitmap, counted against maxSizeBytes
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return value.getByteCount();
		}
		// Pre HC-MR1
		else {
			return value.getRowBytes() * value.getHeight();
		}
	}
}
