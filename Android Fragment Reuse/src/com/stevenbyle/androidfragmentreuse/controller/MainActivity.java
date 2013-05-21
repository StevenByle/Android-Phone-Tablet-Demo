package com.stevenbyle.androidfragmentreuse.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.stevenbyle.androidfragmentreuse.R;
import com.stevenbyle.androidfragmentreuse.controller.rotator.ImageRotatorFragment;
import com.stevenbyle.androidfragmentreuse.model.StaticData;

public class MainActivity extends FragmentActivity implements OnImageSelectedListener {
	private static final String TAG = MainActivity.class.getSimpleName();

	private ViewGroup mImageSelectorLayout, mImageRotatorLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");

		setContentView(R.layout.activity_main);

		// If this is the first creation of the activity, add fragments to it
		if (savedInstanceState == null) {

			// If our layout has a container for the image selector fragment,
			// add it
			mImageSelectorLayout = (ViewGroup) findViewById(R.id.activity_main_image_selector_container);
			if (mImageSelectorLayout != null) {
				Log.i(TAG, "onCreate: adding ImageSelectorFragment to MainActivity");

				// Add fragment to the activity's container layout
				ImageSelectorFragment imageSelectorFragment = new ImageSelectorFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mImageSelectorLayout.getId(), imageSelectorFragment, ImageSelectorFragment.class.getName());

				// Commit the transaction
				fragmentTransaction.commit();
			}

			// If our layout has a container for the image rotator fragment, add
			// it
			mImageRotatorLayout = (ViewGroup) findViewById(R.id.activity_main_image_rotate_container);
			if (mImageRotatorLayout != null) {
				Log.i(TAG, "onCreate: adding ImageRotatorFragment to MainActivity");

				// Add fragment to the activity's container layout
				ImageRotatorFragment imageRotatorFragment = ImageRotatorFragment.newInstance(StaticData.getImageResIds()[0]);
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mImageRotatorLayout.getId(), imageRotatorFragment, ImageRotatorFragment.class.getName());

				// Commit the transaction
				fragmentTransaction.commit();
			}
		}
		else {
			// The fragment manager will handle restoring them if we are being
			// restored from a saved state
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
	public void onImageSelected(int imageResourceId) {
		Log.d(TAG, "onImageSelected: imageResourceId = " + imageResourceId);

		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment imageRotatorFragment = fragmentManager.findFragmentByTag(ImageRotatorFragment.class.getName());

		// If our rotating fragment is in our current layout, update its
		// image
		if (imageRotatorFragment != null) {

			// Only interact with fragments that are resumed
			if (imageRotatorFragment.isResumed()) {
				((ImageRotatorFragment) imageRotatorFragment).setRotatingImageResourceId(imageResourceId);
			}
		}
	}

}
