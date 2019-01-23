package com.brockgast.android.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.brockgast.android.bakingapp.DetailActivity;
import com.brockgast.android.bakingapp.R;
import com.brockgast.android.bakingapp.model.RecipeSteps;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecipeInstructionsFragment extends Fragment {

    private ArrayList<RecipeSteps> recipeStepsArrayList = new ArrayList<>();

    private String mDescriptionText;
    private SimpleExoPlayer mExoPlayer;
    private int mCurrentPosition;
    private long mLastPlayerPosition = 0;
    private Boolean mLastPlayerState = false;
    long lastPlayPosition;
    Boolean lastPlayState;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.exoplayer)
    PlayerView mPlayer;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.tv_no_video)
    TextView mNoVideo;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recipe_step_description)
    TextView mDescription;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.back_step_button)
    Button mPrevious;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.forward_step_button)
    Button mNext;

    public RecipeInstructionsFragment() {
        // Required empty public constructor
    }

    /**
     * @param inflater of type LayoutInflater
     * @param container of type ViewGroup
     * @param savedInstanceState of type Bundle
     * @return view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_recipe_instructions, container, false);
        ButterKnife.bind(this, view);

        recipeStepsArrayList = ((DetailActivity) Objects.requireNonNull(getActivity())).getmRecipeStepsArrayList();
        DetailActivity mParentActivity = (DetailActivity) getActivity();
        mCurrentPosition = mParentActivity.getmCurrentPosition();

        mDescriptionText = recipeStepsArrayList.get(mCurrentPosition).getDescription();
        mDescription.setText(mDescriptionText);

        playVideo(getVideo());
        updateView();

        return view;
    }

    /**
     * Sets onClick for back button using ButterKnife
     * Goes to previous step for making recipe
     */
    @OnClick(R.id.back_step_button)
    public void onClickBackStep() {
        if (mCurrentPosition > 0)
            mCurrentPosition--;
        updateView();
        resetPlayerState();
        stepChange(mCurrentPosition);
    }

    /**
     * Sets onClick for forward button using ButterKnife
     * Changes to next step for making recipe
     */
    @OnClick(R.id.forward_step_button)
    public void onClickNextStep() {
        if (mCurrentPosition < recipeStepsArrayList.size() - 1)
            mCurrentPosition++;
        updateView();
        resetPlayerState();
        stepChange(mCurrentPosition);
    }

    /**
     * Gets video URL for current step
     * @return recipeStepsArrayList.get(mCurrentPosition).getVideoUrl()
     */
    private String getVideo() {
        return recipeStepsArrayList.get(mCurrentPosition).getVideoUrl();
    }

    /**
     * Plays video for current step if available.
     * Displays message if no video for current step.
     * @param videoUrl of type String
     */
    private void playVideo(String videoUrl) {
         if (mExoPlayer == null) {
            mPlayer.setVisibility(View.VISIBLE);
            mNoVideo.setVisibility(View.INVISIBLE);
            Uri videoUri = Uri.parse(videoUrl);

            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory trackSelection = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(trackSelection);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mPlayer.setPlayer(mExoPlayer);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getContext()),
                    Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);
            mExoPlayer.prepare(mediaSource);
        }
        if (TextUtils.isEmpty(videoUrl)) {
            mPlayer.setVisibility(View.INVISIBLE);
            mNoVideo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Updates view in fragment_recipe_instructions in next button or previous button is clicked
     * @param position of type int
     */
    public void stepChange(int position) {
        mCurrentPosition = position;

        releaseExoPlayer();
        playVideo(getVideo());

        mExoPlayer.seekTo(mLastPlayerPosition);
        mExoPlayer.setPlayWhenReady(mLastPlayerState);

        if (mCurrentPosition < recipeStepsArrayList.size() && mCurrentPosition >= 0) {
            mDescriptionText = recipeStepsArrayList.get(mCurrentPosition).getDescription();
            mDescription.setText(mDescriptionText);
        }
        updateView();
    }

    /**
     * Sets the visibility of buttons based on current step position.
     */
    private void updateView() {
        if (mCurrentPosition == 0) {
            mPrevious.setVisibility(View.INVISIBLE);
            mNext.setVisibility(View.VISIBLE);
        } else if (mCurrentPosition == recipeStepsArrayList.size() - 1) {
            mPrevious.setVisibility(View.VISIBLE);
            mNext.setVisibility(View.INVISIBLE);
        } else {
            mPrevious.setVisibility(View.VISIBLE);
            mNext.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mExoPlayer == null && Util.SDK_INT > 23) { // For calling playVideo if API higher than 23
            playVideo(getVideo());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null) { // For calling playVideo if API lower than 24
            playVideo(getVideo());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) { // Only release player here if API is lower than 24
            releaseExoPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) { // Only release player here if API is higher than 23
            releaseExoPlayer();
        }
    }

    /**
     * Releases ExoPlayer and sets to null so that if user
     * navigates away from app, it doesn't break player.
     */
    private void releaseExoPlayer() {
        if (mExoPlayer != null) {
            lastPlayPosition = mExoPlayer.getCurrentPosition();
            lastPlayState = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void resetPlayerState() {
        mLastPlayerPosition = 0;
        mLastPlayerState = false;
    }

    /**
     * Saves player position during configuration changes such as rotation.
     * @param outState of type Bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.position), mCurrentPosition);
        if (mExoPlayer != null) {
            outState.putLong(getString(R.string.play_position), mExoPlayer.getCurrentPosition());
            outState.putBoolean(getString(R.string.play_state), mExoPlayer.getPlayWhenReady());
        } else {
            outState.putLong(getString(R.string.play_position), lastPlayPosition);
            outState.putBoolean(getString(R.string.play_state), lastPlayState);
        }
    }

    /**
     * Captures and resumes player position during configuration changes such as rotation.
     * @param savedInstanceState of type Bundle
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(getString(R.string.position));
            mLastPlayerPosition = savedInstanceState.getLong(getString(R.string.play_position));
            mLastPlayerState = savedInstanceState.getBoolean(getString(R.string.play_state));
            stepChange(mCurrentPosition);
        }
    }
}
