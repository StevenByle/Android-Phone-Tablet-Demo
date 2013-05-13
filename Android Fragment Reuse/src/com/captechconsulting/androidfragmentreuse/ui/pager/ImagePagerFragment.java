package com.captechconsulting.androidfragmentreuse.ui.pager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.captechconsulting.androidfragmentreuse.R;
import com.captechconsulting.androidfragmentreuse.model.StaticData;
import com.captechconsulting.androidfragmentreuse.ui.OnImageSelectedListener;

public class ImagePagerFragment extends Fragment implements OnPageChangeListener {
	private static final String TAG = ImagePagerFragment.class.getSimpleName();

	private ViewPager mViewPager;
	private ImagePagerAdapter mImagePagerAdapter;
	private OnImageSelectedListener mOnImageSelectedListener;

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView");

		// Inflate the fragment main view in the container provided
		View v = inflater.inflate(R.layout.fragment_image_pager, container, false);

		// Setup views
		mViewPager = (ViewPager) v.findViewById(R.id.fragment_image_pager_viewpager);
		mImagePagerAdapter = new ImagePagerAdapter(getActivity(), StaticData.getImageIds(), StaticData.getImageNames());
		mViewPager.setAdapter(mImagePagerAdapter);

		// Listen for page changes to update other views
		mViewPager.setOnPageChangeListener(this);

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
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		Log.d(TAG, "onPageSelected: " + position);

		// Inform our parent listener that an image was selected
		if (mOnImageSelectedListener != null) {
			int imageResourceId = StaticData.getImageIds()[position];
			mOnImageSelectedListener.onImageSelected(imageResourceId);
		}
	}

	/**
	 * Set the view pager to scroll to an image resource, if the image is in the pager.
	 * 
	 * @param imageResourceId
	 *            image resource to scroll to
	 */
	public void selectImage(int imageResourceId) {
		Log.d(TAG, "selectImage: imageResourceId = " + imageResourceId);

		// Our parent has notified that an image was selected, find its position
		int position = -1;
		int[] imageIds = StaticData.getImageIds();
		for (int i = 0; i < imageIds.length; i++) {
			if (imageIds[i] == imageResourceId) {
				position = i;
				break;
			}
		}

		// If we find the image's position, set our pager to that image
		if (position != -1) {
			Log.d(TAG, "selectImage: setting current item = " + position);

			// Move the view pager to the current image
			mViewPager.setCurrentItem(position, true);
		}
	}

}
