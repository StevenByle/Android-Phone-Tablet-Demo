package com.stevenbyle.androidfragmentreuse.model;

import com.stevenbyle.androidfragmentreuse.R;

/**
 * Static data to display, simply for example purposes.
 * 
 * @author Steven Byle
 */
public class StaticImageData {
	private static ImageItem[] mImageItemArray;

	private static int[] imageResIds = {R.drawable.android,
		R.drawable.hamburger, R.drawable.globe, R.drawable.notepad,
		R.drawable.play, R.drawable.sun, R.drawable.tux, R.drawable.yawn};
	private static String[] imageTitles = {"Android", "Burger", "Globe",
		"Note Pad", "Play", "Sun", "Tux", "Yahh!"};

	/**
	 * Default image to show
	 */
	public static final int DEFAULT_IMAGE_INDEX = 0;

	/**
	 * @return static instance of the image item array
	 */
	public static ImageItem[] getImageItemArrayInstance() {
		if (mImageItemArray == null) {
			mImageItemArray = new ImageItem[imageResIds.length];

			for (int i = 0; i < mImageItemArray.length; i++) {
				ImageItem imageItem = new ImageItem(imageTitles[i], imageResIds[i]);
				mImageItemArray[i] = imageItem;
			}
		}
		return mImageItemArray;
	}



}
