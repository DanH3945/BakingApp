package com.hereticpurge.studentbakingapp.utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hereticpurge.studentbakingapp.VolleyResponseListener;

public final class NetworkUtils {

    private static final String TAG = "NetworkUtils";
    private static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    // Overloaded the method to accept both outside url input as well as the default url from above.  Not used now but for future proofing.
    public static void queryRecipeJson(String requestTag, Context context, VolleyResponseListener volleyResponseListener){
        queryRecipeJson(RECIPE_URL, requestTag, context, volleyResponseListener);
    }

    public static void queryRecipeJson(String url, final String requestTag, Context context, final VolleyResponseListener volleyResponseListener) {

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        volleyResponseListener.onVolleyResponse(response, requestTag);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        volleyResponseListener.onVolleyErrorResponse(error, requestTag);

                    }
                });

        stringRequest.setTag(requestTag);
        queue.add(stringRequest);
    }
}
