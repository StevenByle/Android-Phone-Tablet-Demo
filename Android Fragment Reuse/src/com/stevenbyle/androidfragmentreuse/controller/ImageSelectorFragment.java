package com.stevenbyle.androidfragmentreuse.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.stevenbyle.androidfragmentreuse.R;
import com.stevenbyle.androidfragmentreuse.controller.list.ImageListFragment;
import com.stevenbyle.androidfragmentreuse.controller.pager.ImagePagerFragment;
import com.stevenbyle.androidfragmentreuse.controller.rotator.ImageRotatorFragment;
import com.stevenbyle.androidfragmentreuse.model.StaticData;

public class ImageSelectorFragment extends Fragment implements OnImageSelectedListener {
	private static final String TAG = ImageSelectorFragment.class.getSimpleName();

	// State keys
	private static final String KEY_STATE_IMAGE_RESOURCE_ID = "KEY_STATE_IMAGE_RESOURCE_ID";

	private ViewGroup mListLayout, mPagerLayout;
	private OnImageSelectedListener mOnImageSelectedListener;
	private Boolean mInitialCreate;
	private int mImageResourceId;
	private ViewGroup mContainer;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v(TAG, "onAttach");

		// Check if parent fragment implements our image
		// selected callback
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null) {
			try {
				mOnImageSelectedListener = (OnImageSelectedListener) parentFragment;
			}
			catch (ClassCastException e) {
			}
		}

		// Otherwise check if parent activity implements our image
		// selected callback
		if (mOnImageSelectedListener == null && activity != null) {
			try {
				mOnImageSelectedListener = (OnImageSelectedListener) activity;
			}
			catch (ClassCastException e) {
			}
		}

		// If neither implements the image selected callback, warn that
		// selections are being missed
		if (mOnImageSelectedListener == null) {
			Log.w(TAG, "onAttach: niether the parent fragment or parent activity implement OnImageSelectedListener, image selections will not be handled");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");

		if (savedInstanceState != null) {
			mInitialCreate = false;

			// Restore our state
			mImageResourceId = savedInstanceState.getInt(KEY_STATE_IMAGE_RESOURCE_ID);
		}
		else {
			mInitialCreate = true;

			// Default the resource id to the first image
			mImageResourceId = StaticData.getImageResIds()[0];
		}

		// Set that this fragment has a menu
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView");
		Log.d(TAG, "onCreateView: mInitialCreate = " + mInitialCreate);

		// Inflate the fragment main view in the container
		View v = inflater.inflate(R.layout.fragment_image_selector, container, false);
		mContainer = container;

		// If this is the first creation of the fragment, add child fragments
		if (mInitialCreate) {
			mInitialCreate = false;

			// Prepare a transaction to add fragments to this fragment
			FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

			// Add the list fragment to this fragment's layout
			mListLayout = (ViewGroup) v.findViewById(R.id.fragment_image_selector_list_container);
			if (mListLayout != null) {
				Log.i(TAG, "onCreate: adding ImageListFragment to ImageSelectorFragment");

				// Add the fragment to the this fragment's container layout
				ImageListFragment imageListFragment = new ImageListFragment();
				fragmentTransaction.replace(mListLayout.getId(), imageListFragment, ImageListFragment.class.getName());
			}

			// Add the pager fragment to this fragment's layout
			mPagerLayout = (ViewGroup) v.findViewById(R.id.fragment_image_selector_pager_container);
			if (mPagerLayout != null) {
				Log.i(TAG, "onCreate: adding ImagePagerFragment to ImageSelectorFragment");

				// Add the fragment to the this fragment's container layout
				ImagePagerFragment imagePagerFragment = new ImagePagerFragment();
				fragmentTransaction.replace(mPagerLayout.getId(), imagePagerFragment, ImagePagerFragment.class.getName());
			}

			// Commit the transaction
			fragmentTransaction.commit();

		}

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.v(TAG, "onViewCreated");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.v(TAG, "onActivityCreated");
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		Log.v(TAG, "onViewStateRestored");
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

		outState.putInt(KEY_STATE_IMAGE_RESOURCE_ID, mImageResourceId);
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.v(TAG, "onDestroyView");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		Log.v(TAG, "onCreateOptionsMenu");

		// Inflate the action bar menu
		inflater.inflate(R.menu.fragment_image_selector, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected");

		switch (item.getItemId()) {
			case R.id.menu_rotate:
				Log.i(TAG, "onOptionsItemSelected: rotate menu item selected");

				// Get the parent activity's fragment manager
				FragmentManager fragmentManager = getFragmentManager();
				ImageRotatorFragment imageRotatorFragment = (ImageRotatorFragment) fragmentManager.findFragmentByTag(ImageRotatorFragment.class.getName());

				// If our rotating fragment is in our current layout, update its
				// image
				if (imageRotatorFragment != null) {

					// Only interact with views of a fragment if it is resumed
					if (imageRotatorFragment.isResumed()) {
						imageRotatorFragment.setRotatingImageResourceId(mImageResourceId);
					}
				}
				// Otherwise, add the image rotator fragment on top of the image
				// selector fragment and create a stack
				else {

					// Create the image rotator fragment and pass in arguments
					imageRotatorFragment = ImageRotatorFragment.newInstance(mImageResourceId);

					// Add the new fragment on top of this one, and add it to
					// the back stack
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(mContainer.getId(), imageRotatorFragment, ImageRotatorFragment.class.getName());
					fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					fragmentTransaction.addToBackStack(null);

					// Commit the transaction
					fragmentTransaction.commit();
				}

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onImageSelected(int imageResourceId) {
		Log.d(TAG, "onImageSelected: imageResourceId = " + imageResourceId);

		// Keep track of the selected image
		mImageResourceId = imageResourceId;

		// Get this fragment's fragment manager
		FragmentManager fragmentManager = getChildFragmentManager();
		ImageListFragment imageListFragment = (ImageListFragment) fragmentManager.findFragmentByTag(ImageListFragment.class.getName());
		ImagePagerFragment imagePagerFragment = (ImagePagerFragment) fragmentManager.findFragmentByTag(ImagePagerFragment.class.getName());

		// If the fragments are in our layout, have them select the current
		// image
		if (imageListFragment != null && imageListFragment.isResumed()) {
			imageListFragment.selectImage(imageResourceId);
		}
		if (imagePagerFragment != null && imagePagerFragment.isResumed()) {
			imagePagerFragment.selectImage(imageResourceId);
		}

		// Notify our parent listener that an image was selected
		if (mOnImageSelectedListener != null) {
			mOnImageSelectedListener.onImageSelected(imageResourceId);
		}
	}
}
