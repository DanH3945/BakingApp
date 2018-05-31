package com.hereticpurge.studentbakingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hereticpurge.studentbakingapp.model.Recipe;

import java.util.ArrayList;

public class DetailFragment extends Fragment{

    private Recipe mRecipe;

    private int mStepIndex;
    private int START_INDEX = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipe_detail_fragment_layout, container, false);


        return root;
    }

    public void displayRecipe(Recipe recipe){
        mRecipe = recipe;
        mStepIndex = START_INDEX;
        nextStep();
    }

    private void nextStep(){
        if (mStepIndex < (mRecipe.getRecipeSteps().size() - 1)){
            showStep(mRecipe.getRecipeSteps().get(++mStepIndex));
        }
    }

    private void previousStep(){
        if (mStepIndex > 0){
            showStep(mRecipe.getRecipeSteps().get(--mStepIndex));
        }
    }

    private void showStep(Recipe.RecipeStep recipeStep){
        if (recipeStep.getVideoUrl() != ""){

        }
    }

}
