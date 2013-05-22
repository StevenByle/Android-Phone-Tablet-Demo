package com.stevenbyle.androidfragmentreuse.controller.selector;

import com.stevenbyle.androidfragmentreuse.model.ImageItem;

/**
 * Interface to dispatch image selections.
 * 
 * @author Steven Byle
 */
public interface ImageSelector {

	/**
	 * Inform the listener that an image has been selected.
	 * 
	 * @param imageItem
	 * @param position
	 */
	public void setImageSelected(ImageItem imageItem, int position);
}
