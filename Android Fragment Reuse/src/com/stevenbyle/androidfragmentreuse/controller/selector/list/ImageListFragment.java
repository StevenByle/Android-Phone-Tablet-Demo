package com.stevenbyle.androidfragmentreuse.controller.selector.list;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.stevenbyle.androidfragmentreuse.R;
import com.stevenbyle.androidfragmentreuse.controller.selector.ImageSelector;
import com.stevenbyle.androidfragmentreuse.controller.selector.OnImageSelectedListener;
import com.stevenbyle.androidfragmentreuse.model.ImageItem;
import com.stevenbyle.androidfragmentreuse.model.StaticImageData;

/**
 * Fragment to show the images in a list and allow selections.
 * 
 * @author Steven Byle
 */
public class ImageListFragment extends Fragment implements OnItemClickListener, ImageSelector {
	private static final String TAG = ImageListFragment.class.getSimpleName();

	private ListView mListView;
	private ImageArrayAdapter mImageArrayAdapter;
	private OnImageSelectedListener mOnImageSelectedListener;
	private Boolean mInitialCreate;

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

		// Track if this is the initial creation of the fragment
		if (savedInstanceState != null) {
			mInitialCreate = false;
		}
		else {
			mInitialCreate = true;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");

		// Inflate the fragment main view in the container provided
		View v = inflater.inflate(R.layout.fragment_image_list, container, false);

		// Setup views
		mListView = (ListView) v.findViewById(R.id.fragment_image_list_listview);
		mListView.setOnItemClickListener(this);

		// Set an adapter to bind the image items to the list
		mImageArrayAdapter = new ImageArrayAdapter(getActivity(),
				R.layout.list_row_image_items, StaticImageData.getImageItemArrayInstance());
		mListView.setAdapter(mImageArrayAdapter);

		// Set list view to highlight when an item is pressed
		mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

		// If first creation of fragment, select the first item
		if (mInitialCreate && mImageArrayAdapter.getCount() > 0) {

			// Default the selection to the first item
			mListView.setItemChecked(0, true);
		}

		// Track that onCreateView has been called at least once since the
		// initial onCreate
		if (mInitialCreate) {
			mInitialCreate = false;
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d(TAG, "onItemClick: " + "pos: " + position + " id: " + id);

		// Highlight the selected row
		mListView.setItemChecked(position, true);

		// Inform our parent listener that an image was selected
		if (mOnImageSelectedListener != null) {
			ImageItem imageItem = mImageArrayAdapter.getItem(position);
			mOnImageSelectedListener.onImageSelected(imageItem, position);
		}
	}

	@Override
	public void setImageSelected(ImageItem imageItem, int position) {
		Log.d(TAG, "setImageSelected: title = " + imageItem.getTitle() + " position = " + position);

		if (isResumed()) {
			// If the selected position is valid, and different than what is
			// currently selected, highlight that row in the list and
			// scroll to it
			if (position >= 0 && position < mImageArrayAdapter.getCount()
					&& position != mListView.getCheckedItemPosition()) {

				// Highlight the selected row and scroll to it
				mListView.setItemChecked(position, true);
				mListView.smoothScrollToPosition(position);
			}
		}
	}

}
