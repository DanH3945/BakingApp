package com.hereticpurge.studentbakingapp.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import timber.log.Timber;

public final class RecipeController {

    private final ArrayList<Recipe> mRecipeList = new ArrayList<>();
    private static RecipeController sController;

    private RecipeController(){}

    public static RecipeController getController(){
        if (sController == null){
            sController = new RecipeController();
            Timber.v("getController: new Controller Created");
        }
        Timber.v("getController: returning current controller");
        return sController;
    }

    @Nullable
    public Recipe getFirst(){
        if (mRecipeList.size() > 0){
            return mRecipeList.get(0);
        } else {
            return null;
        }
    }

    public ArrayList<Recipe> getRecipeList(){
        return mRecipeList;
    }

    public void addRecipe(Recipe recipe){
        mRecipeList.add(recipe);
        Timber.d("addRecipe: Recipe added.  " + "Total list size is: " + mRecipeList.size());
    }

}
