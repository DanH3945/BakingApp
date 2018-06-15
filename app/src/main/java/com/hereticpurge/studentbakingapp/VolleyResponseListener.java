package com.hereticpurge.studentbakingapp;

import com.android.volley.VolleyError;

public interface VolleyResponseListener {

    void onVolleyResponse(String jsonString, String TAG);

    void onVolleyErrorResponse(VolleyError volleyError, String TAG);
}
