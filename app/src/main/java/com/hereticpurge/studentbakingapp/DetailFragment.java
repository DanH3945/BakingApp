package com.hereticpurge.studentbakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.hereticpurge.studentbakingapp.model.Recipe;
import com.hereticpurge.studentbakingapp.model.RecipeController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import timber.log.Timber;

public class DetailFragment extends Fragment {

    private static final String RECIPE_BROADCAST_INTENT = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String RECIPE_BROADCAST_INGREDIENT_STRING = "recipeBroadcastIngredientString";
    public static final String RECIPE_BROADCAST_INDEX_ID = "recipeBroadcastID";

    private static final String BUNDLE_STEP_ID = "bundledStepIndex";
    private static final String BUNDLE_PLAY_POSITION_ID = "bundledPlayPosition";

    private Recipe mRecipe;

    private int mStepIndex;
    private static final int START_INDEX = -1;

    private final RecipeController mController = RecipeController.getController();

    private TextView mShortDesc;
    private TextView mLongDesc;
    private ImageView mImageView;

    // PlayerView is the new preferred way to implement an ExoPlayer view.  The old way uses
    // SimpleExoPlayerView which is now deprecated.
    private PlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;

    private long mPlayPosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipe_detail_fragment_layout, container, false);

        view.setOnTouchListener(new SimpleSwipeListener());

        // Ugly fix for the scroll view not pushing click events to the main detail fragment view.
        // It'll do for now but needs improved if doing an actual release version.
        ScrollView scrollview = view.findViewById(R.id.detail_scroll_view);
        scrollview.setOnTouchListener(new SimpleSwipeListener());

        FloatingActionButton broadcastFab = view.findViewById(R.id.broadcast_fab);
        broadcastFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broadcastRecipe();
            }
        });

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

    public Bundle getState() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_STEP_ID, mStepIndex);
        bundle.putLong(BUNDLE_PLAY_POSITION_ID, mExoPlayer.getCurrentPosition());
        return bundle;
    }

    public void displayRecipe(@Nullable Bundle bundle) {
        Timber.d("displayRecipe Called");
        mStepIndex = START_INDEX;

        if (mController.getSelected() != null) {
            mRecipe = mController.getSelected();
        }

        if (bundle != null) {
            Timber.d("Non Null bundle detected setting index and play position");
            mStepIndex = bundle.getInt(BUNDLE_STEP_ID);
            mPlayPosition = bundle.getLong(BUNDLE_PLAY_POSITION_ID);
            showStep(mRecipe.getRecipeSteps().get(mStepIndex));
        } else {
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

        // make sure the views are cleared and all video assets are unassigned before loading a new
        // step.
        clearViews();

        // setting the short description of the step
        mShortDesc.setText(recipeStep.getShortDescription());

        // If this is the first step display the ingredient list in the long description area.
        // otherwise display the description for the specific step.
        if (mStepIndex > 0) {
            mLongDesc.setText(recipeStep.getDescription());
        } else {
            mShortDesc.setText(mRecipe.getRecipeTitle());
            mLongDesc.setText(getFormattedIngredientList());
        }

        Timber.d("Recipe video URL" + recipeStep.getVideoUrl());
        Timber.d("Recipe thumbnail URL" + recipeStep.getThumbnailUrl());

        String vidUrl = recipeStep.getVideoUrl();
        String thumbUrl = recipeStep.getThumbnailUrl();

        String fileExtension;

        // determining the file extension to send to the media loader method.  if no media
        // is detected through the urls it defaults to the cupcake image instead of calling
        // loadMedia.
        if (!vidUrl.equals("")) {
            fileExtension = vidUrl.substring(vidUrl.lastIndexOf("."), vidUrl.length());
            loadMedia(vidUrl, fileExtension);

            Timber.d("Video Url Extension is: " + fileExtension);
        } else if (!thumbUrl.equals("")) {
            fileExtension = thumbUrl.substring(thumbUrl.lastIndexOf("."), thumbUrl.length());
            loadMedia(thumbUrl, fileExtension);

            Timber.d("Thumbnail Url Extension is: " + fileExtension);
        } else {
            mImageView.setImageResource(R.mipmap.cupcake);
            mExoPlayerView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
        }
    }

    private void loadMedia(String url, String fileExtension) {

        // switch with fall through to allow loading the correct media assets based on file extension
        // if an unknown extension is seen it just pops a toast.
        switch (fileExtension) {
            case "":
                break;

            case ".mp4":
                // exoplayer handles all the background loading and buffering for video assets.
                Timber.d(".mp4 Detected");
                mImageView.setVisibility(View.GONE);
                mExoPlayerView.setVisibility(View.VISIBLE);
                initExoPlayer(url);
                break;

            case ".bmp":
            case ".png":
            case ".gif":
            case ".jpg":
                // picasso handles all the background asset loading for images.
                Timber.d("Image detected");
                Picasso.with(getActivity().getApplicationContext()).load(url).into(mImageView);
                mExoPlayerView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                break;

            default:
                Toast.makeText(getActivity().getApplicationContext(), R.string.unknown_file_type, Toast.LENGTH_LONG).show();
                break;

        }
    }

    private void broadcastRecipe() {

        // Building and sending an intent to be grabbed by the widget for displaying the
        // ingredients.
        Intent intent = new Intent();
        intent.setAction(RECIPE_BROADCAST_INTENT);
        intent.putExtra(RECIPE_BROADCAST_INGREDIENT_STRING, getFormattedIngredientList());
        intent.putExtra(RECIPE_BROADCAST_INDEX_ID, mController.getSelectedIndex());
        Timber.d("Broadcast sent with index of: " + mController.getSelectedIndex());
        getActivity().sendBroadcast(intent);
        Toast.makeText(getActivity().getApplicationContext(), R.string.ingredients_sent_text, Toast.LENGTH_LONG).show();
    }

    private String getFormattedIngredientList() {

        // Building a formatted string of ingredients to be displayed.
        StringBuilder stringBuilder = new StringBuilder()
                .append(mRecipe.getRecipeTitle() + " Ingredients: ")
                .append(System.getProperty("line.separator"))
                .append(System.getProperty("line.separator"));

        ArrayList<Recipe.RecipeIngredient> ingredientArray = mRecipe.getRecipeIngredients();

        if (mRecipe != null) {
            for (int i = 0; i < mRecipe.getRecipeIngredients().size(); i++) {
                Recipe.RecipeIngredient recipeIngredient = ingredientArray.get(i);

                String ingredientName = recipeIngredient.getIngredient();
                String ingredientQuantity = recipeIngredient.getQuantity();
                String ingredientMeasure = recipeIngredient.getMeasure();

                stringBuilder.append(ingredientName + " ")
                        .append(ingredientQuantity + " ")
                        .append(ingredientMeasure + " ")
                        .append(System.getProperty("line.separator"));
            }
        }
        return stringBuilder.toString();
    }

    private void initExoPlayer(String url) {

        // This is the old implementation suggested in the class.  This implementation is now
        // deprecated in favor of the method below the comment.
        /*
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                getActivity().getApplicationContext(),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        */

        // Continuing with the new suggested implementation

        // ExoPlayer initialization
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity().getApplicationContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        mExoPlayerView.setPlayer(mExoPlayer);

        Uri uri = Uri.parse(url);
        String userAgent = Util.getUserAgent(getActivity().getApplicationContext(), "StudentBakingApp");

        // This is another implementation suggested by the classes.  However this style is deprecated in favor
        // of the below Factory implementation

        /*
        MediaSource mediaSource = new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(getActivity().getApplicationContext(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null);
        */

        // New correct way to implement ExoPlayer
        ExtractorMediaSource.Factory mediaFactory = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(
                getActivity().getApplicationContext(),
                userAgent
        ));
        MediaSource mediaSource = mediaFactory.createMediaSource(uri);
        // Continue normal implementation of the ExoPlayer unchanged from class suggestions.

        // Setting the size and fill of the video player so it fills the correct portion of the screen.
        mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        mExoPlayer.prepare(mediaSource);

        // Checks whether this is the first step before deciding to immedietely play the video or
        // not.  When starting the program from the widget it quickly got annoying having the video
        // start right away.  Candidate for a preference option.

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !getResources().getBoolean(R.bool.isTablet)) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mExoPlayerView.getLayoutParams();
            layoutParams.width = layoutParams.MATCH_PARENT;
            layoutParams.height = layoutParams.MATCH_PARENT;
        }

        mExoPlayer.seekTo(mPlayPosition);
        if (mStepIndex != 0) {
            mExoPlayer.setPlayWhenReady(true);
        } else {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    private void clearViews() {

        // Clears the views and releases the exoplayer if there is one.
        mShortDesc.setText("");
        mLongDesc.setText("");
        mImageView.setVisibility(View.GONE);

        mExoPlayerView.setVisibility(View.INVISIBLE);
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public boolean onBack() {

        // this is called from MainActivity when the back button is pressed to determine if
        // the detail fragment should be reversed one step or if the app should default to
        // the system back button default.
        if (mStepIndex < 1) {
            clearViews();
            Timber.d("Ready to exit DetailFragment");
            return true;
        } else {
            previousStep();
            Timber.d("Not ready to exit DetailFragment");
            return false;
        }
    }

    @Override
    public void onPause() {

        // stores the current position of the playing video if the app is paused.
        if (mExoPlayer != null) {
            mPlayPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.setPlayWhenReady(false);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        // restores video play position when resumed.
        super.onResume();
        if (mExoPlayer != null) {
            mExoPlayer.seekTo(mPlayPosition);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    class SimpleSwipeListener implements View.OnTouchListener {

        // I wanted to try implementing my own swipe listener instead of using an existing one as
        // a demo.

        // start by creating variable to hold position information of clicks.
        private float xStart = 0;
        private float xStop = 0;
        private float yStart = 0;
        private float yStop = 0;

        // the following 2 variables are used to prevent the listener from registering swipe events
        // when the user may not mean to swipe.  Random screen touches etc.

        // the min distance a user must swipe across the screen for this listener to register it as
        // a swipe.
        final float SWIPE_X_MIN_DISTANCE = 200;

        // the max y distance (up / down) a user is allowed to press before the event won't be
        // recognized.
        final float SWIPE_Y_MAX_DISTANCE = 500;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ScrollView scrollView = getView().findViewById(R.id.detail_scroll_view);
            scrollView.onTouchEvent(event);
            Timber.d("Caught motion event.");
            // send the click to the view so that other methods (accessibility stuff) can see
            // that an event occured and where.
            // v.performClick();

            // basic math stuff for working on an x,y grid.
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // store the starting position
                    xStart = event.getX();
                    yStart = event.getY();
                    Timber.d("X POSITION: " + Float.toString(xStart));
                    Timber.d("Y POSITION: " + Float.toString(yStart));
                    break;

                case MotionEvent.ACTION_UP:
                    // store the stop position
                    xStop = event.getX();
                    yStop = event.getY();

                    // getting the absolute values to determine swipe length.
                    float xTrans = Math.abs(xStop - xStart);
                    float yTrans = Math.abs(yStop - yStart);

                    // making sure the motion was intentional by ensuring motion distances.
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
            // tell the system that the event was handled.
            return true;
        }
    }
}
