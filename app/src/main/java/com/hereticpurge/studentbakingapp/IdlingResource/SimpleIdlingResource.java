package com.hereticpurge.studentbakingapp.IdlingResource;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

public class SimpleIdlingResource implements IdlingResource {

    @Nullable private volatile ResourceCallback mCallBack;
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(false);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        Timber.d("Checking Idler State: " + mIsIdleNow.toString());
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallBack = callback;
    }

    public void setIdleState(boolean state){
        Timber.d("Switching Idler State to: " + Boolean.toString(state));
        mIsIdleNow.set(state);
        if (isIdleNow() && mCallBack != null){
            mCallBack.onTransitionToIdle();
        }
    }
}
