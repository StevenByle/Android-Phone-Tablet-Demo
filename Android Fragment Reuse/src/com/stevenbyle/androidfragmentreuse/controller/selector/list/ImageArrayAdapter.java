package com.stevenbyle.androidfragmentreuse.controller.selector.list;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stevenbyle.androidfragmentreuse.R;
import com.stevenbyle.androidfragmentreuse.model.ImageItem;
import com.stevenbyle.androidfragmentreuse.model.ImageUtils;
import com.stevenbyle.androidfragmentreuse.model.ThumbnailCache;

/**
 * Adapter to bind image items to the list view.
 * 
 * @author Steven Byle
 */
public class ImageArrayAdapter extends ArrayAdapter<ImageItem> {
	//private static final String TAG = ImageArrayAdapter.class.getSimpleName();

	private final int mLayoutResourceId;

	/**
	 * Static thumbnail cache to live through config changes (rotations)
	 */
	public static ThumbnailCache mThumbnailCache;

	/**
	 * @param context
	 * @param layoutResourceId
	 * @param imageItems
	 */
	public ImageArrayAdapter(Context context, int layoutResourceId, ImageItem[] imageItems) {
		super(context, layoutResourceId, imageItems);
		mLayoutResourceId = layoutResourceId;

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClassBytes = activityManager.getMemoryClass() * 1024 * 1024;
		if(mThumbnailCache == null) {
			mThumbnailCache = new ThumbnailCache(memoryClassBytes / 8);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Only load views if the convertView isn't being recycled, otherwise
		// just get the view holder
		final ImageItemViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(mLayoutResourceId, null);

			holder = new ImageItemViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.list_row_image_imageview);
			holder.titleText = (TextView) convertView.findViewById(R.id.list_row_image_textview_name);
			convertView.setTag(holder);
		}
		else {
			holder = (ImageItemViewHolder) convertView.getTag();
		}

		// Pull out the relevant data to bind to the views
		ImageItem imageItem = getItem(position);
		String title = imageItem.getTitle();
		int imageResId = imageItem.getImageResId();

		// Try looking in the memory cache first
		Bitmap cachedBitmap = mThumbnailCache.get((long) imageResId);
		Bitmap bitmapToSet = null;

		if (cachedBitmap != null) {
			bitmapToSet = cachedBitmap;
			//Log.d(TAG, "getView: position = " + position + " - cache hit");
		}
		// Otherwise, load from storage and put in the cache
		else {
			// Get a smaller version of the image to display in the list for less memory usage
			Resources res = getContext().getResources();
			int dimensInPixels = res.getDimensionPixelSize(R.dimen.list_row_image_item_dimensions);
			Bitmap sampledBitmap = ImageUtils.decodeSampledBitmapFromResource(res, imageResId,
					dimensInPixels, dimensInPixels);

			// Store loaded thumbnail in cache
			mThumbnailCache.put((long) imageResId, sampledBitmap);
			bitmapToSet = sampledBitmap;

			//Log.d(TAG, "getView: position = " + position + " - cache miss - sampledBitmap.getWidth() = " + sampledBitmap.getWidth()
			//		+ " sampledBitmap.getHeight() = " + sampledBitmap.getHeight());
		}

		// Update the views
		holder.titleText.setText(title);
		holder.imageView.setImageBitmap(bitmapToSet);

		return convertView;
	}

	private class ImageItemViewHolder {
		public ImageView imageView;
		public TextView titleText;
	}

}
