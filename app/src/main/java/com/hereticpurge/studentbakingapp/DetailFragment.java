package com.hereticpurge.studentbakingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hereticpurge.studentbakingapp.model.Recipe;

import timber.log.Timber;

public class DetailFragment extends Fragment{

    private Recipe mRecipe;

    private int mStepIndex;
    private int START_INDEX = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipe_detail_fragment_layout, container, false);
        root.setOnTouchListener(new SimpleSwipeListener());
        return root;
    }

    public void displayRecipe(Recipe recipe){
        mRecipe = recipe;
        mStepIndex = START_INDEX;
        displayIngredients();
        nextStep();
    }

    private void nextStep(){
        if (mStepIndex < (mRecipe.getRecipeSteps().size() - 1)){
            showStep(mRecipe.getRecipeSteps().get(++mStepIndex));
            Timber.d("Now Showing Step #: " + mStepIndex);
        }
    }

    private void previousStep(){
        if (mStepIndex > 0){
            showStep(mRecipe.getRecipeSteps().get(--mStepIndex));
            Timber.d("Now Showing Step #: " + mStepIndex);
        }
    }

    private void showStep(Recipe.RecipeStep recipeStep){

    }

    private void displayIngredients(){

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
                    Timber.d("----------  MOTION EVENT BREAK  ----------");
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
                    break;
            }
            return true;
        }
    }
}
