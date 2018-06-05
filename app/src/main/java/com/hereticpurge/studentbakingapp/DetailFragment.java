package com.hereticpurge.studentbakingapp;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hereticpurge.studentbakingapp.model.Recipe;
import com.hereticpurge.studentbakingapp.model.RecipeController;

import timber.log.Timber;

public class DetailFragment extends Fragment{

    private Recipe mRecipe;

    private int mStepIndex;
    private int START_INDEX = -1;

    private RecipeController mController = RecipeController.getController();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recipe_detail_fragment_layout, container, false);

        view.setOnTouchListener(new SimpleSwipeListener());

        return view;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    public void displayRecipe(){
        if (mController.getSelected() != null) {
            mRecipe = mController.getSelected();
            Timber.d("Loaded Recipe: " + mRecipe.getRecipeTitle());
            mStepIndex = START_INDEX;
            nextStep();
        }
    }

    private void nextStep(){
        if (mRecipe != null && mStepIndex < (mRecipe.getRecipeSteps().size() - 1)){
            showStep(mRecipe.getRecipeSteps().get(++mStepIndex));
            Timber.d("Now Showing Step #: " + mStepIndex);
        }
    }

    private void previousStep(){
        if (mRecipe != null && mStepIndex > 0){
            showStep(mRecipe.getRecipeSteps().get(--mStepIndex));
            Timber.d("Now Showing Step #: " + mStepIndex);
        }
    }

    private void showStep(Recipe.RecipeStep recipeStep){

    }

    class SimpleSwipeListener implements View.OnTouchListener{

        private float xStart = 0;
        private float xStop = 0;
        private float yStart = 0;
        private float yStop = 0;

        float SWIPE_X_MIN_DISTANCE = 200;
        float SWIPE_Y_MAX_DISTANCE = 500;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    xStart = event.getX();
                    yStart = event.getY();
                    Timber.d("X POSITION: " + Float.toString(xStart));
                    Timber.d("Y POSITION: " + Float.toString(yStart));
                    break;

                case MotionEvent.ACTION_UP:
                    xStop = event.getX();
                    yStop = event.getY();

                    float xTrans = Math.abs(xStop - xStart);
                    float yTrans = Math.abs(yStop - yStart);

                    if (xTrans > SWIPE_X_MIN_DISTANCE && yTrans < SWIPE_Y_MAX_DISTANCE){
                        if (xStart > xStop){
                            Timber.d("SWIPE LEFT");
                            previousStep();
                        } else {
                            Timber.d("SWIPE RIGHT");
                            nextStep();
                        }
                    }
                    Timber.d("----------  MOTION EVENT BREAK  ----------");
                    break;
            }
            return true;
        }
    }
}
