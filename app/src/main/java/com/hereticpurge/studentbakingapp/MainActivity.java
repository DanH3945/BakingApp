package com.hereticpurge.studentbakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hereticpurge.studentbakingapp.utilities.JsonUtils;
import com.hereticpurge.studentbakingapp.utilities.NetworkUtils;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements VolleyResponseListener {

    private static final String VOLLEY_INITIAL_QUERY_TAG = "VolleyInitQuery";


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

        NetworkUtils.queryRecipeJson(VOLLEY_INITIAL_QUERY_TAG, this, this);

    }

    @Override
    public void onVolleyResponse(String jsonString, String requestTag) {

        // switch statement to handle incoming responses incase more need to be added later.
        switch (requestTag){
            case VOLLEY_INITIAL_QUERY_TAG:
                JsonUtils.populateRecipesFromJson(jsonString);
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
}
