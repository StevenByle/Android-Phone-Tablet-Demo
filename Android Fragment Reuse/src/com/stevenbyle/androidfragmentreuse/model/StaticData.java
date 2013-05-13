package com.stevenbyle.androidfragmentreuse.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stevenbyle.androidfragmentreuse.R;

public class StaticData {
	public static String COL_NAME_IMAGEVIEW = "col_name_imageview";
	public static String COL_NAME_TEXTVIEW = "col_name_textview";

	private static List<Map<String, Comparable>> mMapList;
	private static int[] imageIds = {R.drawable.android,
		R.drawable.adobe_acrobat_7, R.drawable.adobe_bridge,
		R.drawable.adobe_designer, R.drawable.adobe_go_live,
		R.drawable.adobe_illustrator, R.drawable.adobe_image_ready,
		R.drawable.adobe_in_design, R.drawable.adobe_photoshop,
		R.drawable.adobe_stock_photos, R.drawable.adobe_suite_cs2,
		R.drawable.globe, R.drawable.hamburger, R.drawable.notepad,
		R.drawable.play, R.drawable.sun, R.drawable.tux, R.drawable.yawn};
	private static String[] imageNames = {"Android Logo", "Adobe Acrobat 7",
		"Adobe Bridge", "Adobe Designer", "Adobe Go Live",
		"Adobe Illustrator", "Adobe Image Ready", "Adobe In Design",
		"Adobe Photoshop", "Adobe Stock Photos", "Adobe Suite CS2",
		"Globe", "Hamburger", "Note Pad", "Play", "Sun", "Linux Tux",
	"Yawn"};

	public static List<Map<String, Comparable>> getMapListInstance() {
		if (mMapList == null) {
			mMapList = getMapList();
		}
		return mMapList;
	}

	private static List<Map<String, Comparable>> getMapList() {
		List<Map<String, Comparable>> mapList = new ArrayList<Map<String, Comparable>>();

		for (int i = 0; i < imageIds.length; i++) {
			Map<String, Comparable> map = new HashMap<String, Comparable>();
			map.put(COL_NAME_IMAGEVIEW, imageIds[i]);
			map.put(COL_NAME_TEXTVIEW, imageNames[i]);
			mapList.add(map);
		}

		return mapList;
	}

	/**
	 * @return the imageIds
	 */
	public static int[] getImageIds() {
		return imageIds;
	}

	/**
	 * @return the imageNames
	 */
	public static String[] getImageNames() {
		return imageNames;
	}

}
