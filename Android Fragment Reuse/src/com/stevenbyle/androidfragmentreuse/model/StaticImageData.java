package com.stevenbyle.androidfragmentreuse.model;

import com.stevenbyle.androidfragmentreuse.R;

public class StaticImageData {
	private static ImageItem[] mImageItemArray;

	private static int[] imageResIds = {R.drawable.android, R.drawable.globe,
		R.drawable.hamburger, R.drawable.notepad, R.drawable.play,
		R.drawable.sun, R.drawable.tux, R.drawable.yawn};
	private static String[] imageTitles = {"Android", "Globe",
		"Burger", "Note Pad", "Play", "Sun", "Tux", "Yahh!"};

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
