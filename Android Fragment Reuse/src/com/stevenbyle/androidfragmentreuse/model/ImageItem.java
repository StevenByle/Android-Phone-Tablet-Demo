package com.stevenbyle.androidfragmentreuse.model;

public class ImageItem {

	private String title;
	private int imageResId;

	/**
	 * @param title
	 * @param imageResId
	 */
	public ImageItem(String title, int imageResId) {
		super();
		this.title = title;
		this.imageResId = imageResId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the imageResId
	 */
	public int getImageResId() {
		return imageResId;
	}

	/**
	 * @param imageResId
	 *            the imageResId to set
	 */
	public void setImageResId(int imageResId) {
		this.imageResId = imageResId;
	}

}
