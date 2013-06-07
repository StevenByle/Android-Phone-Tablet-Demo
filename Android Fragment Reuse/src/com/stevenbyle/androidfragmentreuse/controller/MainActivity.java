package com.stevenbyle.androidfragmentreuse.controller;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.stevenbyle.androidfragmentreuse.R;
import com.stevenbyle.androidfragmentreuse.controller.rotator.ImageRotatorFragment;
import com.stevenbyle.androidfragmentreuse.controller.selector.ImageSelectorFragment;
import com.stevenbyle.androidfragmentreuse.controller.selector.OnImageSelectedListener;
import com.stevenbyle.androidfragmentreuse.model.ImageItem;
import com.stevenbyle.androidfragmentreuse.model.StaticImageData;

/**
 * Main activity to host the entire application.
 * 
 * @author Steven Byle
 */
public class MainActivity extends FragmentActivity implements OnImageSelectedListener {
	private static final String TAG = MainActivity.class.getSimpleName();

	private ViewGroup mImageSelectorLayout, mImageRotatorLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");

		setContentView(R.layout.activity_main);

		// Restore state
		if (savedInstanceState != null) {
			// The fragment manager will handle restoring them if we are being
			// restored from a saved state
		}
		// If this is the first creation of the activity, add fragments to it
		else {

			// If our layout has a container for the image selector fragment,
			// add it
			mImageSelectorLayout = (ViewGroup) findViewById(R.id.activity_main_image_selector_container);
			if (mImageSelectorLayout != null) {
				Log.i(TAG, "onCreate: adding ImageSelectorFragment to MainActivity");

				// Add image selector fragment to the activity's container layout
				ImageSelectorFragment imageSelectorFragment = new ImageSelectorFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mImageSelectorLayout.getId(), imageSelectorFragment,
						ImageSelectorFragment.class.getName());

				// Commit the transaction
				fragmentTransaction.commit();
			}

			// If our layout has a container for the image rotator fragment, add
			// it
			mImageRotatorLayout = (ViewGroup) findViewById(R.id.activity_main_image_rotate_container);
			if (mImageRotatorLayout != null) {
				Log.i(TAG, "onCreate: adding ImageRotatorFragment to MainActivity");

				// Add image rotator fragment to the activity's container layout
				ImageRotatorFragment imageRotatorFragment = ImageRotatorFragment.newInstance(
						StaticImageData.getImageItemArrayInstance()[0].getImageResId());
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mImageRotatorLayout.getId(), imageRotatorFragment,
						ImageRotatorFragment.class.getName());

				// Commit the transaction
				fragmentTransaction.commit();
			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.v(TAG, "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.v(TAG, "onPause");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.v(TAG, "onSaveInstanceState");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	@Override
	public void onImageSelected(ImageItem imageItem, int position) {
		Log.d(TAG, "onImageSelected: title = " + imageItem.getTitle() + " position = " + position);

		FragmentManager fragmentManager = getSupportFragmentManager();
		ImageRotatorFragment imageRotatorFragment = (ImageRotatorFragment) fragmentManager.findFragmentByTag(
				ImageRotatorFragment.class.getName());

		// If the rotating fragment is in the current layout, update its
		// selected image
		if (imageRotatorFragment != null) {
			imageRotatorFragment.setImageSelected(imageItem, position);
		}
	}

}
