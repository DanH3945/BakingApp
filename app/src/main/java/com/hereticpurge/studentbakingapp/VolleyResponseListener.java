package com.hereticpurge.studentbakingapp;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Dilbert on 5/17/2018.
 */

public interface VolleyResponseListener {

    void onVolleyResponse(String jsonString, String TAG);
    void onVolleyErrorResponse(VolleyError volleyError, String TAG);
}
