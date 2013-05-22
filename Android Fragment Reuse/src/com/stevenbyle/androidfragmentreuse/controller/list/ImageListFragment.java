package com.stevenbyle.androidfragmentreuse.controller.list;

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
import com.stevenbyle.androidfragmentreuse.controller.OnImageSelectedListener;
import com.stevenbyle.androidfragmentreuse.model.ImageItem;
import com.stevenbyle.androidfragmentreuse.model.StaticData;

public class ImageListFragment extends Fragment implements OnItemClickListener {
	private static final String TAG = ImageListFragment.class.getSimpleName();

	private ListView mListView;
	private ImageItemArrayAdapter mImageItemArrayAdapter;
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
		Log.v(TAG, "onCreate");

		// Keep track if this is the initial creation of the fragment
		if (savedInstanceState == null) {
			mInitialCreate = true;
		}
		else {
			mInitialCreate = false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView");

		// Inflate the fragment main view in the container provided
		View v = inflater.inflate(R.layout.fragment_image_list, container, false);

		// Setup views
		mListView = (ListView) v.findViewById(R.id.fragment_image_list_listview);
		mListView.setOnItemClickListener(this);

		// Set an adapter to bind the iamge items to the list
		mImageItemArrayAdapter = new ImageItemArrayAdapter(getActivity(), R.layout.list_row_image_items, StaticData.getImageItemArrayInstance());
		mListView.setAdapter(mImageItemArrayAdapter);

		// Set list view to highlight when item is pressed
		mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

		// If first creation of fragment, select the first image
		if (mInitialCreate && mImageItemArrayAdapter.getCount() > 0) {
			mInitialCreate = false;

			// Highlight the selected row
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
			ImageItem imageItem = mImageItemArrayAdapter.getItem(position);
			mOnImageSelectedListener.onImageSelected(imageItem, position);
		}
	}

	/**
	 * Set the list to highlight an image resource, if the image is in the list.
	 * 
	 * @param position
	 *            list position to scroll to
	 */
	public void selectImage(int position) {
		Log.d(TAG, "selectImage: position = " + position);

		// If the selected position is valid, highlight that row in the list and
		// scroll to it
		if (position >= 0 && position < mImageItemArrayAdapter.getCount()) {

			// Highlight the selected row and scroll to it
			mListView.setItemChecked(position, true);
			mListView.smoothScrollToPosition(position);
		}
	}

}
