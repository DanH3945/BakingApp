package com.hereticpurge.studentbakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hereticpurge.studentbakingapp.model.RecipeController;
import com.hereticpurge.studentbakingapp.utilities.JsonUtils;
import com.hereticpurge.studentbakingapp.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements VolleyResponseListener {

    private static final String TAG = "MainActivity";
    private static final String VOLLEY_DEFAULT_TAG = "volleyDefault";
    RecipeController mController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mController = RecipeController.getController();

        NetworkUtils.queryRecipeJson(VOLLEY_DEFAULT_TAG, this, this);

    }

    @Override
    public void onVolleyResponse(String jsonString, String requestTag) {

        // switch statement to handle incoming responses incase more need to be added later.
        switch (requestTag){
            case VOLLEY_DEFAULT_TAG:
                JsonUtils.populateRecipesFromJson(jsonString, mController);

            default:
                Toast.makeText(this, R.string.volley_response_error, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onVolleyErrorResponse(VolleyError volleyError, String requestTag) {
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
    }
}
