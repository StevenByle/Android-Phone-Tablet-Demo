package com.stevenbyle.androidfragmentreuse.controller.selector;

import com.stevenbyle.androidfragmentreuse.model.ImageItem;

/**
 * Listener interface to communicate image selections.
 * 
 * @author Steven Byle
 */
public interface OnImageSelectedListener {

	/**
	 * Inform the listener that an image has been selected.
	 * 
	 * @param imageItem
	 * @param position
	 */
	public void onImageSelected(ImageItem imageItem, int position);
}
