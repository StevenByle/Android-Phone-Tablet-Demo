package com.stevenbyle.androidfragmentreuse.controller.rotator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.stevenbyle.androidfragmentreuse.R;
import com.stevenbyle.androidfragmentreuse.model.StaticData;

public class ImageRotatorFragment extends Fragment {
	private static final String TAG = ImageRotatorFragment.class.getSimpleName();
	private static final String FULL_CLASSPATH = ImageRotatorFragment.class.getName();

	// Passed in argument keys
	private static final String KEY_ARG_IMAGE_RES_ID = FULL_CLASSPATH + ".KEY_ARG_IMAGE_RES_ID";

	// State keys
	private static final String KEY_STATE_IMAGE_RES_ID = "KEY_STATE_IMAGE_RES_ID";
	private static final String KEY_STATE_SCALE_X_PERCENT = "KEY_STATE_SCALE_X_PERCENT";
	private static final String KEY_STATE_SCALE_Y_PERCENT = "KEY_STATE_SCALE_Y_PERCENT";

	private ImageView mImageView;
	private int mImageResourceId;
	private Boolean mInitialCreate;
	private double mScaleXPercent, mScaleYPercent;

	public ImageRotatorFragment() {
		super();
		Log.v(TAG, "ImageRotatorFragment()");
	}

	public static ImageRotatorFragment newInstance(int imageResourceId) {
		Log.v(TAG, "newInstance: imageResourceId = " + imageResourceId);

		// Craete a new fragment instance
		ImageRotatorFragment imageRotatorFragment = new ImageRotatorFragment();

		// Get arguments passed in, if any
		Bundle args = imageRotatorFragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		args.putInt(KEY_ARG_IMAGE_RES_ID, imageResourceId);
		imageRotatorFragment.setArguments(args);

		return imageRotatorFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v(TAG, "onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");

		// Set incoming parameters
		Bundle args = getArguments();
		if (args != null) {
			mImageResourceId = args.getInt(KEY_ARG_IMAGE_RES_ID, StaticData.getImageResIds()[0]);
		}
		else {
			// Default image resource to the first image
			mImageResourceId = StaticData.getImageResIds()[0];
		}

		Log.i(TAG, "onCreate: mImageResourceId = " + mImageResourceId);

		// Restore state, overwriting any passed in parameters
		if (savedInstanceState != null) {
			mInitialCreate = false;
			mImageResourceId = savedInstanceState.getInt(KEY_STATE_IMAGE_RES_ID);
			mScaleXPercent = savedInstanceState.getDouble(KEY_STATE_SCALE_X_PERCENT);
			mScaleYPercent = savedInstanceState.getDouble(KEY_STATE_SCALE_Y_PERCENT);
		}
		else {
			mInitialCreate = true;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView");

		// Inflate the fragment layout into the container provided
		final View v = inflater.inflate(R.layout.fragment_image_rotator, container, false);

		// Setup views
		mImageView = (ImageView) v.findViewById(R.id.fragment_image_rotator_imageview_rotate);
		mImageView.setImageResource(mImageResourceId);

		final View rotatingView = mImageView;

		final SeekBar seekBarTransX = (SeekBar) v.findViewById(R.id.fragment_image_rotator_seekbar_translation_x);
		seekBarTransX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mScaleXPercent = (double) progress / seekBar.getMax();
				rotatingView.setTranslationX(progress);
			}
		});

		final SeekBar seekBarTransY = (SeekBar) v.findViewById(R.id.fragment_image_rotator_seekbar_translation_y);
		seekBarTransY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mScaleYPercent = (double) progress / seekBar.getMax();
				rotatingView.setTranslationY(progress);
			}
		});

		// Set the scale seekbars based on the available space to move
		final ViewTreeObserver viewTreeObserver = v.getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					LinearLayout seekBarLinearLayout = (LinearLayout) v.findViewById(R.id.fragment_image_rotator_seekbar_root_linear_layout);
					ImageView rotatingImage = (ImageView) v.findViewById(R.id.fragment_image_rotator_imageview_rotate);

					int availableWidth = getView().getWidth() - rotatingImage.getWidth();
					int availableHeight = v.getHeight() - seekBarLinearLayout.getHeight() - rotatingImage.getHeight();

					seekBarTransX.setMax(availableWidth);
					seekBarTransY.setMax(availableHeight);

					// If first run, center the image in the available space
					if (mInitialCreate) {
						mInitialCreate = false;
						seekBarTransX.setProgress(availableWidth / 2);
						seekBarTransY.setProgress(availableHeight / 2);
					}
					// Otherwise, restore the image based on the position's
					// percentage
					else {
						mScaleXPercent = savedInstanceState.getDouble(KEY_STATE_SCALE_X_PERCENT);
						mScaleYPercent = savedInstanceState.getDouble(KEY_STATE_SCALE_Y_PERCENT);
						seekBarTransX.setProgress((int) Math.round(mScaleXPercent * availableWidth));
						seekBarTransY.setProgress((int) Math.round(mScaleYPercent * availableHeight));
					}
				}
			});
		}

		// Allow the user to scale 0x to 5x
		SeekBar seekBar;
		seekBar = (SeekBar) v.findViewById(R.id.fragment_image_rotator_seekbar_scale_x);
		seekBar.setMax(50);
		seekBar.setProgress(10);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				rotatingView.setScaleX(progress / 10f);
			}
		});

		// Allow the user to scale 0x to 5x
		seekBar = (SeekBar) v.findViewById(R.id.fragment_image_rotator_seekbar_scale_y);
		seekBar.setMax(50);
		seekBar.setProgress(10);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				rotatingView.setScaleY(progress / 10f);
			}
		});

		// Allow the user to rotate 0 to 360 degress
		seekBar = (SeekBar) v.findViewById(R.id.fragment_image_rotator_seekbar_rotation_x);
		seekBar.setMax(360);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				rotatingView.setRotationX(progress);
			}
		});

		// Allow the user to rotate 0 to 360 degress
		seekBar = (SeekBar) v.findViewById(R.id.fragment_image_rotator_seekbar_rotation_y);
		seekBar.setMax(360);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				rotatingView.setRotationY(progress);
			}
		});

		// Allow the user to rotate 0 to 360 degress
		seekBar = (SeekBar) v.findViewById(R.id.fragment_image_rotator_seekbar_rotation_z);
		seekBar.setMax(360);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				rotatingView.setRotation(progress);
			}
		});

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

		outState.putInt(KEY_STATE_IMAGE_RES_ID, mImageResourceId);
		outState.putDouble(KEY_STATE_SCALE_X_PERCENT, mScaleXPercent);
		outState.putDouble(KEY_STATE_SCALE_Y_PERCENT, mScaleYPercent);
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

	/**
	 * Method to change the rotating view from a parent fragment or activity.
	 * 
	 * @param imageResourceId
	 */
	public void setRotatingImageResourceId(int imageResourceId) {
		Log.i(TAG, "setRotatingImageResourceId: imageResourceId = " + imageResourceId);
		mImageResourceId = imageResourceId;
		mImageView.setImageResource(mImageResourceId);
	}
}
