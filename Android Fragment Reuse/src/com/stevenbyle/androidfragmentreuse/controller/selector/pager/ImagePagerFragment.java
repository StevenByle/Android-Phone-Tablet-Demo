package com.stevenbyle.androidfragmentreuse.controller.selector.pager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stevenbyle.androidfragmentreuse.R;
import com.stevenbyle.androidfragmentreuse.controller.selector.ImageSelector;
import com.stevenbyle.androidfragmentreuse.controller.selector.OnImageSelectedListener;
import com.stevenbyle.androidfragmentreuse.model.ImageItem;
import com.stevenbyle.androidfragmentreuse.model.StaticImageData;

/**
 * Fragment to show images in a horizontally swiping pager, allowing the user to
 * select images.
 * 
 * @author Steven Byle
 */
public class ImagePagerFragment extends Fragment implements OnPageChangeListener, ImageSelector {
	private static final String TAG = ImagePagerFragment.class.getSimpleName();

	private ViewPager mViewPager;
	private ImagePagerAdapter mImagePagerAdapter;
	private OnImageSelectedListener mOnImageSelectedListener;
	private ImageItem[] mPagerImageItems;

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");

		// Inflate the fragment main view in the container provided
		View v = inflater.inflate(R.layout.fragment_image_pager, container, false);

		// Setup views
		mViewPager = (ViewPager) v.findViewById(R.id.fragment_image_pager_viewpager);
		mPagerImageItems = StaticImageData.getImageItemArrayInstance();
		mImagePagerAdapter = new ImagePagerAdapter(mPagerImageItems);
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
			mOnImageSelectedListener.onImageSelected(mPagerImageItems[position], position);
		}
	}

	@Override
	public void setImageSelected(ImageItem imageItem, int position) {

		if (isResumed()) {
			// If the selected position is valid, and different than what is
			// currently selected, move the pager to that image
			if (position >= 0 && position < mImagePagerAdapter.getCount()
					&& position != mViewPager.getCurrentItem()) {
				Log.d(TAG, "setImageSelected: title = " + imageItem.getTitle() + " position = " + position);

				// Move the view pager to the current image
				mViewPager.setCurrentItem(position, true);
			}
		}
	}

}
