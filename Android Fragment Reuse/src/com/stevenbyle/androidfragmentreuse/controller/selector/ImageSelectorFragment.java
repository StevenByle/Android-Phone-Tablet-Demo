package com.stevenbyle.androidfragmentreuse.controller.selector;

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
import com.stevenbyle.androidfragmentreuse.controller.rotator.ImageRotatorFragment;
import com.stevenbyle.androidfragmentreuse.controller.selector.list.ImageListFragment;
import com.stevenbyle.androidfragmentreuse.controller.selector.pager.ImagePagerFragment;
import com.stevenbyle.androidfragmentreuse.model.ImageItem;
import com.stevenbyle.androidfragmentreuse.model.StaticImageData;

/**
 * Fragment that allows the user to select an image using various sub-fragments,
 * while keeping them in sync with each other.
 * 
 * @author Steven Byle
 */
public class ImageSelectorFragment extends Fragment implements OnImageSelectedListener {
	private static final String TAG = ImageSelectorFragment.class.getSimpleName();

	// State keys
	private static final String KEY_STATE_CUR_IMAGE_RESOURCE_ID = "KEY_STATE_CUR_IMAGE_RESOURCE_ID";
	private static final String KEY_STATE_CUR_IMAGE_POSITION = "KEY_STATE_CUR_IMAGE_POSITION";

	private Boolean mInitialCreate;
	private int mCurImageResourceId, mCurImagePosition;
	private ViewGroup mListLayout, mPagerLayout, mContainer;
	private OnImageSelectedListener mOnImageSelectedListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v(TAG, "onAttach");

		// Check if parent fragment (if there is one) implements the image
		// selection interface
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof OnImageSelectedListener) {
			mOnImageSelectedListener = (OnImageSelectedListener) parentFragment;
		}
		// Otherwise, check if parent activity implements the image
		// selection interface
		else if (activity != null && activity instanceof OnImageSelectedListener) {
			mOnImageSelectedListener = (OnImageSelectedListener) activity;
		}
		// If neither implements the image selection callback, warn that
		// selections are being missed
		else if (mOnImageSelectedListener == null) {
			Log.w(TAG, "onAttach: niether the parent fragment or parent activity implement OnImageSelectedListener, "
					+ "image selections will not be communicated to other components");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");

		// Restore state
		if (savedInstanceState != null) {
			mInitialCreate = false;

			mCurImageResourceId = savedInstanceState.getInt(KEY_STATE_CUR_IMAGE_RESOURCE_ID);
			mCurImagePosition = savedInstanceState.getInt(KEY_STATE_CUR_IMAGE_POSITION);
		}
		// Otherwise, default state
		else {
			mInitialCreate = true;

			// Default the resource id to the first image
			int defaultPosition = 0;
			mCurImageResourceId = StaticImageData.getImageItemArrayInstance()[defaultPosition].getImageResId();
			mCurImagePosition = defaultPosition;
		}

		// Set that this fragment has a menu
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");

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

		outState.putInt(KEY_STATE_CUR_IMAGE_RESOURCE_ID, mCurImageResourceId);
		outState.putInt(KEY_STATE_CUR_IMAGE_POSITION, mCurImagePosition);
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

				// This menu option is only provided to phone layouts, since
				// tablet layouts show the image rotator at all times

				// Get the parent activity's fragment manager
				FragmentManager fragmentManager = getFragmentManager();

				// Create the image rotator fragment and pass in arguments
				ImageRotatorFragment imageRotatorFragment = ImageRotatorFragment.newInstance(mCurImageResourceId);

				// Add the new fragment on top of this one, and add it to
				// the back stack
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(mContainer.getId(), imageRotatorFragment, ImageRotatorFragment.class.getName());
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				fragmentTransaction.addToBackStack(null);

				// Commit the transaction
				fragmentTransaction.commit();

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onImageSelected(ImageItem imageItem, int position) {

		// Only inform the other fragments if the selected position is new
		if (mCurImagePosition != position) {

			Log.d(TAG, "onImageSelected: title = " + imageItem.getTitle() + " position = " + position);

			// Keep track of the selected image
			mCurImageResourceId = imageItem.getImageResId();
			mCurImagePosition = position;

			// Get the fragment manager for this fragment's children
			FragmentManager fragmentManager = getChildFragmentManager();
			ImageListFragment imageListFragment = (ImageListFragment) fragmentManager.findFragmentByTag(ImageListFragment.class.getName());
			ImagePagerFragment imagePagerFragment = (ImagePagerFragment) fragmentManager.findFragmentByTag(ImagePagerFragment.class.getName());

			// If the fragments are in the current layout, have them select the
			// current image
			if (imageListFragment != null) {
				imageListFragment.setImageSelected(imageItem, position);
			}
			if (imagePagerFragment != null) {
				imagePagerFragment.setImageSelected(imageItem, position);
			}

			// Notify the parent listener that an image was selected
			if (mOnImageSelectedListener != null) {
				mOnImageSelectedListener.onImageSelected(imageItem, position);
			}

		}
	}
}
