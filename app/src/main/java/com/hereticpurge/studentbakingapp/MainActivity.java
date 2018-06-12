package com.hereticpurge.studentbakingapp;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hereticpurge.studentbakingapp.model.RecipeController;
import com.hereticpurge.studentbakingapp.utilities.JsonUtils;
import com.hereticpurge.studentbakingapp.utilities.NetworkUtils;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements VolleyResponseListener {

    private static final String VOLLEY_INITIAL_QUERY_TAG = "VolleyInitQuery";
    private static final String WIDGET_CALL_INDEX = "WidgetCallIndex";
    private boolean isTablet;

    private DetailFragment mDetailFragment;
    private RecipeListFragment mRecipeListFragment;
    private RecipeController mController = RecipeController.getController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        NetworkUtils.queryRecipeJson(VOLLEY_INITIAL_QUERY_TAG, this, this);

    }

    @Override
    public void onVolleyResponse(String jsonString, String requestTag) {

        // switch statement to handle incoming responses in case more need to be added later.
        switch (requestTag){
            case VOLLEY_INITIAL_QUERY_TAG:
                JsonUtils.populateRecipesFromJson(jsonString);
                initUI();
                break;

            default:
                Toast.makeText(this, R.string.volley_response_error, Toast.LENGTH_LONG).show();
                Timber.d("OnVolleyResponse: Volley Response Switch Failure");
        }

    }

    @Override
    public void onVolleyErrorResponse(VolleyError volleyError, String requestTag) {
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
        // When the activity is started from an intent sent via the widget it should send
        // the index position of the recipe it's displaying.  This bit will grab the index and
        // display the same index position.
        int startIndex = getIntent().getIntExtra(WIDGET_CALL_INDEX, -1);
        if (startIndex >= 0 & startIndex <= mController.getRecipeList().size() - 1){
            mController.setSelectedIndex(startIndex);
            recipeSelected();
        }
    }

    public void recipeSelected(){
        if (!isTablet){
            FragmentTransaction switchDetailTransaction = getFragmentManager().beginTransaction();
            switchDetailTransaction.replace(R.id.main_fragment_container, mDetailFragment);
            switchDetailTransaction.addToBackStack(null);
            switchDetailTransaction.commit();
        }
        getFragmentManager().executePendingTransactions();
        mDetailFragment.displayRecipe();
    }

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

    @Override
    protected void onDestroy() {
        mController.clear();
        super.onDestroy();
    }

}
