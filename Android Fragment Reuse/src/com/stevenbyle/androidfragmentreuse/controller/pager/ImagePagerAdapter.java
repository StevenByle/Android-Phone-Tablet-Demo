package com.stevenbyle.androidfragmentreuse.controller.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

		ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(mImageIds[position]);
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
