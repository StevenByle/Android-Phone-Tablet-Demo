package com.stevenbyle.androidfragmentreuse.controller.rotator;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
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
import com.stevenbyle.androidfragmentreuse.controller.ImageSelector;
import com.stevenbyle.androidfragmentreuse.model.ImageItem;
import com.stevenbyle.androidfragmentreuse.model.StaticData;

/**
 * Fragment to display a selected image and allow the user to adjust and
 * transform it using slider controls.
 * 
 * @author Steven Byle
 */
public class ImageRotatorFragment extends Fragment implements ImageSelector {
	private static final String TAG = ImageRotatorFragment.class.getSimpleName();

	// Passed in argument keys
	private static final String FULL_CLASSPATH = ImageRotatorFragment.class.getName();
	private static final String KEY_ARG_IMAGE_RES_ID = FULL_CLASSPATH + ".KEY_ARG_IMAGE_RES_ID";

	// State keys
	private static final String KEY_STATE_IMAGE_RES_ID = "KEY_STATE_IMAGE_RES_ID";
	private static final String KEY_STATE_TRANS_X_PERCENT = "KEY_STATE_TRANS_X_PERCENT";
	private static final String KEY_STATE_TRANS_Y_PERCENT = "KEY_STATE_TRANS_Y_PERCENT";

	private ImageView mImageView;
	private int mImageResourceId;
	private Boolean mInitialCreate;
	private double mTransXPercent, mTransYPercent;

	/**
	 * Convenience method for creating an ImageRotatorFragment without needing
	 * to know any bundle keys
	 * 
	 * @param imageResourceId
	 *            default image resource to show
	 * @return a new instance of ImageRotatorFragment with args set in the bundle
	 */
	public static ImageRotatorFragment newInstance(int imageResourceId) {
		Log.v(TAG, "newInstance: imageResourceId = " + imageResourceId);

		// Create a new fragment instance
		ImageRotatorFragment newImageRotatorFragment = new ImageRotatorFragment();

		// Get arguments passed in, if any
		Bundle args = newImageRotatorFragment.getArguments();
		if (args == null) {
			args = new Bundle();
		}
		// Add parameters to the argument bundle
		args.putInt(KEY_ARG_IMAGE_RES_ID, imageResourceId);
		newImageRotatorFragment.setArguments(args);

		return newImageRotatorFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v(TAG, "onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate: savedInstanceState " + (savedInstanceState == null ? "==" : "!=") + " null");

		// Set incoming parameters
		Bundle args = getArguments();
		if (args != null) {
			mImageResourceId = args.getInt(KEY_ARG_IMAGE_RES_ID, -1);
		}
		// Otherwise, default parameters
		else {
			// Default image resource to the first image
			mImageResourceId = StaticData.getImageItemArrayInstance()[0].getImageResId();
		}

		// Restore state, overwriting any passed in parameters
		if (savedInstanceState != null) {
			mInitialCreate = false;

			mImageResourceId = savedInstanceState.getInt(KEY_STATE_IMAGE_RES_ID);
			mTransXPercent = savedInstanceState.getDouble(KEY_STATE_TRANS_X_PERCENT);
			mTransYPercent = savedInstanceState.getDouble(KEY_STATE_TRANS_Y_PERCENT);
		}
		// Otherwise, default state
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

		// Allow the user to translate (move) the image
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
				mTransXPercent = (double) progress / seekBar.getMax();
				rotatingView.setTranslationX(progress);
			}
		});

		// Allow the user to translate (move) the image
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
				mTransYPercent = (double) progress / seekBar.getMax();
				rotatingView.setTranslationY(progress);
			}
		});

		// Set the scale seek bars based on the available space to move, must
		// wait until the views are drawn before dimensions can be computed
		final ViewTreeObserver viewTreeObserver = v.getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

				@SuppressWarnings("deprecation")
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				@Override
				public void onGlobalLayout() {
					// Remove the listener
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
					else {
						v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
					LinearLayout seekBarLinearLayout = (LinearLayout) v.findViewById(R.id.fragment_image_rotator_seekbar_root_linear_layout);
					ImageView rotatingImage = (ImageView) v.findViewById(R.id.fragment_image_rotator_imageview_rotate);

					// Compute the amount of space the image can be moved (origin at top,left)
					int availableWidth = getView().getWidth() - rotatingImage.getWidth();
					int availableHeight = v.getHeight() - seekBarLinearLayout.getHeight() - rotatingImage.getHeight();

					// Set the seek bars to have a max of the available space
					seekBarTransX.setMax(availableWidth);
					seekBarTransY.setMax(availableHeight);

					// If provided, restore the slider's translation based on
					// the positional percentage
					if (savedInstanceState != null) {
						mTransXPercent = savedInstanceState.getDouble(KEY_STATE_TRANS_X_PERCENT);
						mTransYPercent = savedInstanceState.getDouble(KEY_STATE_TRANS_Y_PERCENT);
					}
					// Otherwise, if first creation, center the image
					else if (mInitialCreate) {
						mInitialCreate = false;

						// Default the translation to the center of the screen
						mTransXPercent = 0.50;
						mTransYPercent = 0.50;
					}

					Log.w(TAG, "mTransXPercent = " + mTransXPercent
							+ " mTransYPercent = " + mTransYPercent);

					// Restore the slider's translation based on the last known percentage
					seekBarTransX.setProgress((int) Math.round(mTransXPercent * seekBarTransX.getMax()));
					seekBarTransY.setProgress((int) Math.round(mTransYPercent * seekBarTransY.getMax()));
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
		outState.putDouble(KEY_STATE_TRANS_X_PERCENT, mTransXPercent);
		outState.putDouble(KEY_STATE_TRANS_Y_PERCENT, mTransYPercent);
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
	public void setImageSelected(ImageItem imageItem, int position) {
		Log.d(TAG, "setImageSelected: title = " + imageItem.getTitle() + " position = " + position);

		if (isResumed()) {
			mImageResourceId = imageItem.getImageResId();
			mImageView.setImageResource(mImageResourceId);
		}
	}
}
