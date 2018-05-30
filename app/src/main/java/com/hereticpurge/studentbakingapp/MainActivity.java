package com.hereticpurge.studentbakingapp;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hereticpurge.studentbakingapp.model.Recipe;
import com.hereticpurge.studentbakingapp.model.RecipeController;
import com.hereticpurge.studentbakingapp.utilities.JsonUtils;
import com.hereticpurge.studentbakingapp.utilities.NetworkUtils;

import java.util.ArrayList;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements VolleyResponseListener {

    private static final String VOLLEY_INITIAL_QUERY_TAG = "VolleyInitQuery";

    private final boolean isTablet = getResources().getBoolean(R.bool.isTablet);

    private DetailFragment mDetailFragment;
    private RecipeListFragment mRecipeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        // switch statement to handle incoming responses incase more need to be added later.
        switch (requestTag){
            case VOLLEY_INITIAL_QUERY_TAG:
                JsonUtils.populateRecipesFromJson(jsonString);
                initUI();
                break;

            default:
                Toast.makeText(this, R.string.volley_response_error, Toast.LENGTH_LONG).show();
                Timber.w("OnVolleyResponse: Volley Response Switch Failure");
        }

    }

    @Override
    public void onVolleyErrorResponse(VolleyError volleyError, String requestTag) {
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
        Timber.d("OnVolleyErrorResponse: Volley Network Error");
    }

    // WORK IN PROGRESS BELOW THIS LINE
    // ------------------------------------------------------------------------
    // WORK ON ME TOMORROW

    public void initUI(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (isTablet){
            transaction.replace(R.id.recipe_list_fragment_container, mRecipeListFragment);

            showDetail(RecipeController.getController().getFirst());

        } else {
            transaction.replace(R.id.fragment_container, mRecipeListFragment);
        }
        transaction.commit();
    }

    public void showDetail(Recipe recipe){

    }
}
