
package com.hereticpurge.studentbakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class DetailFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void getIdlingResource() {
        mIdlingResource = mainActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void SwipeSwitchesSteps() {

        Espresso.onView(ViewMatchers.withId(R.id.recipe_list_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Espresso.onView(ViewMatchers.withId(R.id.detail_scroll_view)).perform(ViewActions.swipeLeft());
        Espresso.onView(ViewMatchers.withId(R.id.detail_text_short_description)).check(ViewAssertions.matches(ViewMatchers.withText("Starting prep")));
    }

    @After
    public void releaseIdle() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

}
