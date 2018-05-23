package com.hereticpurge.studentbakingapp;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyResponseListener {

    void onVolleyResponse(String jsonString, String TAG);
    void onVolleyErrorResponse(VolleyError volleyError, String TAG);
}
