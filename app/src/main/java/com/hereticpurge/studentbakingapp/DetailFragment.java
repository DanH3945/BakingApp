package com.hereticpurge.studentbakingapp;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.hereticpurge.studentbakingapp.model.Recipe;
import com.hereticpurge.studentbakingapp.model.RecipeController;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

public class DetailFragment extends Fragment {

    private Recipe mRecipe;

    private int mStepIndex;
    private int START_INDEX = -1;

    private RecipeController mController = RecipeController.getController();

    private TextView mShortDesc;
    private TextView mLongDesc;
    private ImageView mImageView;
    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipe_detail_fragment_layout, container, false);

        view.setOnTouchListener(new SimpleSwipeListener());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mShortDesc = view.findViewById(R.id.detail_text_short_description);
        mLongDesc = view.findViewById(R.id.detail_text_long_description);
        mImageView = view.findViewById(R.id.detail_image_view);
        mExoPlayerView = view.findViewById(R.id.detail_player_view);

        super.onViewCreated(view, savedInstanceState);
    }

    public void displayRecipe() {
        Timber.d("displayRecipe Called");
        if (mController.getSelected() != null) {
            mRecipe = mController.getSelected();
            Timber.d("Loaded Recipe: " + mRecipe.getRecipeTitle());
            mStepIndex = START_INDEX;
            nextStep();
        }
    }

    private void nextStep() {
        if (mRecipe != null && mStepIndex < (mRecipe.getRecipeSteps().size() - 1)) {
            showStep(mRecipe.getRecipeSteps().get(++mStepIndex));
            Timber.d("Now Showing Step #: " + mStepIndex);
        }
    }

    private void previousStep() {
        if (mRecipe != null && mStepIndex > 0) {
            showStep(mRecipe.getRecipeSteps().get(--mStepIndex));
            Timber.d("Now Showing Step #: " + mStepIndex);
        }
    }

    private void showStep(Recipe.RecipeStep recipeStep) {
        clearViews();

        mShortDesc.setText(recipeStep.getShortDescription());

        if (!mShortDesc.getText().equals(recipeStep.getDescription())) {
            mLongDesc.setText(recipeStep.getDescription());
        }

        Timber.d("Recipe video URL" + recipeStep.getVideoUrl());
        Timber.d("Recipe thumbnail URL" + recipeStep.getThumbnailUrl());

        String vidUrl = recipeStep.getVideoUrl();
        String thumbUrl = recipeStep.getThumbnailUrl();

        String fileExtension;

        if (!vidUrl.equals("")) {
            fileExtension = vidUrl.substring(vidUrl.lastIndexOf("."), vidUrl.length());
            loadMedia(vidUrl, fileExtension);

            Timber.d("Video Url Extension is: " + fileExtension);
        } else if (!thumbUrl.equals("")) {
            fileExtension = thumbUrl.substring(thumbUrl.lastIndexOf("."), thumbUrl.length());
            loadMedia(thumbUrl, fileExtension);

            Timber.d("Thumbnail Url Extension is: " + fileExtension);
        }
    }

    private void loadMedia(String url, String fileExtension) {

        switch (fileExtension) {
            case "":
                break;

            case ".mp4":
                Timber.d(".mp4 Detected");
                initExoPlayer(url);
                break;

            case ".bmp":
            case ".png":
            case ".gif":
            case ".jpg":
                Timber.d("Image detected");
                Picasso.with(getActivity().getApplicationContext()).load(url).into(mImageView);
                mImageView.setVisibility(View.VISIBLE);
                break;

            default:
                break;

        }
    }

    public void initExoPlayer(String url){

        mExoPlayerView.setVisibility(View.VISIBLE);

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                getActivity().getApplicationContext(),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        mExoPlayerView.setPlayer(mExoPlayer);

        Uri uri = Uri.parse(url);
        String userAgent = Util.getUserAgent(getActivity().getApplicationContext(), "StudentBakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(getActivity().getApplicationContext(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null);

        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    private void clearViews() {
        mShortDesc.setText("");
        mLongDesc.setText("");
        mImageView.setVisibility(View.GONE);

        mExoPlayerView.setVisibility(View.GONE);
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    class SimpleSwipeListener implements View.OnTouchListener {

        private float xStart = 0;
        private float xStop = 0;
        private float yStart = 0;
        private float yStop = 0;

        float SWIPE_X_MIN_DISTANCE = 200;
        float SWIPE_Y_MAX_DISTANCE = 500;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xStart = event.getX();
                    yStart = event.getY();
                    Timber.d("X POSITION: " + Float.toString(xStart));
                    Timber.d("Y POSITION: " + Float.toString(yStart));
                    break;

                case MotionEvent.ACTION_UP:
                    xStop = event.getX();
                    yStop = event.getY();

                    float xTrans = Math.abs(xStop - xStart);
                    float yTrans = Math.abs(yStop - yStart);

                    if (xTrans > SWIPE_X_MIN_DISTANCE && yTrans < SWIPE_Y_MAX_DISTANCE) {
                        if (xStart > xStop) {
                            Timber.d("Swipe from right to left");
                            nextStep();
                        } else {
                            Timber.d("Swipe from left to right");
                            previousStep();
                        }
                    }
                    Timber.d("----------  MOTION EVENT BREAK  ----------");
                    break;
            }
            return true;
        }
    }

    public boolean onBack(){
        if (mStepIndex < 1){
            clearViews();
            Timber.d("Ready to exit DetailFragment");
            return true;
        } else {
            previousStep();
            Timber.d("Not ready to exit DetailFragment");
            return false;
        }
    }
}
