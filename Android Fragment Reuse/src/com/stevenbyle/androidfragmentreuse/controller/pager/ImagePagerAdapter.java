package com.stevenbyle.androidfragmentreuse.controller.pager;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stevenbyle.androidfragmentreuse.R;
import com.stevenbyle.androidfragmentreuse.model.ImageItem;

/**
 * Pager adapter that binds image items to a swiping pager.
 * 
 * @author Steven Byle
 */
public class ImagePagerAdapter extends PagerAdapter {

	private final ImageItem[] mImageItemArray;

	/**
	 * Constructor that takes an array of image items to display.
	 * 
	 * @param imageItemArray
	 */
	public ImagePagerAdapter(ImageItem[] imageItemArray) {
		mImageItemArray = imageItemArray;
	}

	@Override
	public int getCount() {
		return mImageItemArray.length;
	}

	@Override
	public Object instantiateItem(ViewGroup collection, int position) {

		Context context = collection.getContext();
		Resources res = context.getResources();
		int paddingInPixels = res.getDimensionPixelSize(R.dimen.fragment_image_pager_image_padding);

		// Create the image view and set the padding and bitmap
		ImageView imageView = new ImageView(context);
		imageView.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);
		imageView.setImageResource(mImageItemArray[position].getImageResId());

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
		return mImageItemArray[position].getTitle();
	}

}
