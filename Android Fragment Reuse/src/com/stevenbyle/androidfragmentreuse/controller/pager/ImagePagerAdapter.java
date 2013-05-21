package com.stevenbyle.androidfragmentreuse.controller.pager;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stevenbyle.androidfragmentreuse.R;

public class ImagePagerAdapter extends PagerAdapter {

	private final Context mContext;
	private final int[] mImageIds;
	private final String[] mImageNames;

	public ImagePagerAdapter(Context context, int[] imageIds, String[] imageNames) {
		mContext = context;
		mImageIds = imageIds;
		mImageNames = imageNames;
	}

	@Override
	public int getCount() {
		return mImageIds.length;
	}

	@Override
	public Object instantiateItem(ViewGroup collection, int position) {

		// Create the image view and set the padding and bitmap
		ImageView imageView = new ImageView(mContext);
		Resources res = mContext.getResources();
		int paddingInPixels = res.getDimensionPixelSize(R.dimen.fragment_image_pager_image_padding);
		imageView.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);
		imageView.setImageResource(mImageIds[position]);

		// Add the image view to the collection
		collection.addView(imageView);

		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup collection, int position, Object view) {
		collection.removeView((ImageView) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mImageNames[position];
	}

}
