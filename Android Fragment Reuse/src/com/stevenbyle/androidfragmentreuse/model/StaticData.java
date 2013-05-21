package com.stevenbyle.androidfragmentreuse.model;

import java.util.ArrayList;
import java.util.List;

import com.stevenbyle.androidfragmentreuse.R;

public class StaticData {
	private static List<ImageItem> mImageItemList;
	private static ImageItem[] mImageItemArray;

	private static int[] imageResIds = {R.drawable.android,
		R.drawable.adobe_acrobat_7, R.drawable.adobe_bridge,
		R.drawable.adobe_designer, R.drawable.adobe_go_live,
		R.drawable.adobe_illustrator, R.drawable.adobe_image_ready,
		R.drawable.adobe_in_design, R.drawable.adobe_photoshop,
		R.drawable.adobe_stock_photos, R.drawable.adobe_suite_cs2,
		R.drawable.globe, R.drawable.hamburger, R.drawable.notepad,
		R.drawable.play, R.drawable.sun, R.drawable.tux, R.drawable.yawn};
	private static String[] imageTitles = {"Android Logo", "Adobe Acrobat 7",
		"Adobe Bridge", "Adobe Designer", "Adobe Go Live",
		"Adobe Illustrator", "Adobe Image Ready", "Adobe In Design",
		"Adobe Photoshop", "Adobe Stock Photos", "Adobe Suite CS2",
		"Globe", "Hamburger", "Note Pad", "Play", "Sun", "Linux Tux",
	"Yawn"};

	public static List<ImageItem> getImageItemListInstance() {
		if (mImageItemList == null) {
			mImageItemList = new ArrayList<ImageItem>();

			for (int i = 0; i < imageResIds.length; i++) {
				ImageItem imageItem = new ImageItem(imageTitles[i], imageResIds[i]);
				mImageItemList.add(imageItem);
			}
		}
		return mImageItemList;
	}

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

	/**
	 * @return the imageResIds
	 */
	public static int[] getImageResIds() {
		return imageResIds;
	}

	/**
	 * @return the imageTitles
	 */
	public static String[] getImageTitles() {
		return imageTitles;
	}

}
