package com.stevenbyle.androidfragmentreuse.controller.list;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stevenbyle.androidfragmentreuse.R;
import com.stevenbyle.androidfragmentreuse.model.ImageItem;
import com.stevenbyle.androidfragmentreuse.model.ImageUtils;

public class ImageItemArrayAdapter extends ArrayAdapter<ImageItem> {
	private static final String TAG = ImageItemArrayAdapter.class.getSimpleName();

	private final int mLayoutResourceId;

	/**
	 * @param context
	 * @param layoutResourceId
	 * @param imageItems
	 */
	public ImageItemArrayAdapter(Context context, int layoutResourceId, ImageItem[] imageItems) {
		super(context, layoutResourceId, imageItems);
		mLayoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

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

		ImageItem imageItem = getItem(position);
		String title = imageItem.getTitle();
		int imageResId = imageItem.getImageResId();

		// Get a smaller version of the image to display in the list, less memory usage
		Resources res = getContext().getResources();
		int dimensInPixels = res.getDimensionPixelSize(R.dimen.list_row_image_item_dimensions);
		Bitmap sampledBitmap = ImageUtils.decodeSampledBitmapFromResource(getContext().getResources(),
				imageResId, dimensInPixels, dimensInPixels);


		Log.i(TAG, "getView: sampledBitmap.getWidth() = " + sampledBitmap.getWidth()
				+ " sampledBitmap.getHeight() = " + sampledBitmap.getHeight());


		holder.titleText.setText(title);
		holder.imageView.setImageBitmap(sampledBitmap);

		return convertView;
	}

	private class ImageItemViewHolder {
		public ImageView imageView;
		public TextView titleText;
	}

}
