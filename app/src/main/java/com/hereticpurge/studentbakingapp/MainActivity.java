package com.hereticpurge.studentbakingapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hereticpurge.studentbakingapp.IdlingResource.SimpleIdlingResource;
import com.hereticpurge.studentbakingapp.model.RecipeController;
import com.hereticpurge.studentbakingapp.utilities.JsonUtils;
import com.hereticpurge.studentbakingapp.utilities.NetworkUtils;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements VolleyResponseListener {

    private static final String VOLLEY_INITIAL_QUERY_TAG = "VolleyInitQuery";
    private static final String VOLLEY_FROM_WIDGET_QUERY = "VolleyWidgetQuery";

    private static final String DETAIL_BUNDLE_ID = "detailBundle";

    private boolean isTablet;

    private DetailFragment mDetailFragment;
    private RecipeListFragment mRecipeListFragment;
    private RecipeController mController;

    private Bundle mSavedInstanceState;
    private Bundle mDetailBundle;

    @Nullable private SimpleIdlingResource mSimpleIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);

        mController = RecipeController.getController();
        mController.clear();

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if (BuildConfig.DEBUG) {
            // debug tree logging
            Timber.plant(new Timber.DebugTree());
        } else {
            // release tree logging
            Timber.plant(new TimberReleaseTree());
        }

        setContentView(R.layout.activity_main);

        mRecipeListFragment = new RecipeListFragment();
        mDetailFragment = new DetailFragment();

        // checking to see if the widget started the app.  if not use default.
        if (getIntent().hasExtra(DetailFragment.RECIPE_BROADCAST_INDEX_ID)){

            Timber.d("Setting selected Index from start intent with index: "
                    + getIntent().getIntExtra(DetailFragment.RECIPE_BROADCAST_INDEX_ID, -1));

            mController.setSelectedIndex(getIntent().getIntExtra(DetailFragment.RECIPE_BROADCAST_INDEX_ID, -1));
            NetworkUtils.queryRecipeJson(VOLLEY_FROM_WIDGET_QUERY, this, this);
        } else {
            NetworkUtils.queryRecipeJson(VOLLEY_INITIAL_QUERY_TAG, this, this);
        }
    }

    @Override
    public void onVolleyResponse(String jsonString, String requestTag) {

        // switch statement to handle incoming responses in case more need to be added later.
        switch (requestTag){
            case VOLLEY_INITIAL_QUERY_TAG:
                // if the app was loaded by the user clicking the icon on their home screen
                JsonUtils.populateRecipesFromJson(jsonString);
                initUI();
                if(mSimpleIdlingResource != null){
                    getFragmentManager().executePendingTransactions();
                    mSimpleIdlingResource.setIdleState(true);
                }
                break;
            case VOLLEY_FROM_WIDGET_QUERY:
                // if the app started from the widget the correct recipe is loaded from its first
                // step
                JsonUtils.populateRecipesFromJson(jsonString);
                initUI();
                recipeSelected();
                break;
            default:
                // this should never happen to a user.
                Toast.makeText(this, R.string.volley_response_error, Toast.LENGTH_LONG).show();
                Timber.d("OnVolleyResponse: Volley Response Switch Failure");
        }

    }

    @Override
    public void onVolleyErrorResponse(VolleyError volleyError, String requestTag) {
        // Called if there was a network error resulting in no web connectivity.
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
        Timber.d("OnVolleyErrorResponse: Volley Network Error");
    }

    public void initUI(){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, mRecipeListFragment);
        transaction.commit();

        if (isTablet){
            Timber.d("Tablet detected.  Loading tablet layout.");
            FragmentTransaction detailTransaction = getFragmentManager().beginTransaction();
            detailTransaction.replace(R.id.recipe_detail_fragment_container, mDetailFragment);
            detailTransaction.commit();

            if (mController.getSelected() != null){
                recipeSelected();
            }
        }

        mDetailBundle = null;
        // if the configuration changed (screen rotation, etc) this will grab the saved state and
        // call recipe selected to redisplay the recipe step.
        try{
            mDetailBundle = mSavedInstanceState.getBundle(DETAIL_BUNDLE_ID);
            recipeSelected();
        } catch (NullPointerException e){
            Timber.d("Null bundle detected.  Skipping");
        }
    }

    public void recipeSelected(){
        if (!isTablet){
            FragmentTransaction switchDetailTransaction = getFragmentManager().beginTransaction();
            switchDetailTransaction.replace(R.id.main_fragment_container, mDetailFragment);
            switchDetailTransaction.addToBackStack(null);
            switchDetailTransaction.commit();
            Timber.d("Initial recipe Selected");
        }

        getFragmentManager().executePendingTransactions();
        mDetailFragment.displayRecipe(mDetailBundle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            // save the detail fragment state when necessary.
            outState.putBundle(DETAIL_BUNDLE_ID, mDetailFragment.getState());
        } catch (NullPointerException e){
            // this happens if the screen is rotated too quickly before the exo player
            // instances are properly setup in which case it null pointer is ignored and
            // the app continues as normal.
            Timber.d("Failed to get state.  App will revert to default");
        }
        super.onSaveInstanceState(outState);
    }

    // Tried to catch incoming broadcast intents here.  Didn't work.  Move along.  =)
    /*@Override
    protected void onNewIntent(Intent intent) {
        int selectedIndex = intent.getIntExtra(DetailFragment.RECIPE_BROADCAST_INDEX_ID, -1);
        Timber.d("Received intent with index of: " + selectedIndex);
        mController.setSelectedIndex(selectedIndex);
        recipeSelected();
        super.onNewIntent(intent);
    }*/

    @Override
    public void onBackPressed() {
        Timber.d("Back pressed. Backstack count: " + getFragmentManager().getBackStackEntryCount());
        if (getFragmentManager().getBackStackEntryCount() > 0 && mDetailFragment.onBack()){
            Timber.d("Popping back stack");
            getFragmentManager().popBackStack();
        } else if (getFragmentManager().getBackStackEntryCount() == 0){
            Timber.d("Calling super onBackPressed");
            super.onBackPressed();
        }
    }

    @VisibleForTesting
    @NonNull
    public synchronized IdlingResource getIdlingResource(){
        if (mSimpleIdlingResource == null){
            mSimpleIdlingResource = new SimpleIdlingResource();
        }
        return mSimpleIdlingResource;
    }
}
