package com.hereticpurge.studentbakingapp.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import timber.log.Timber;

public final class RecipeController {

    private final ArrayList<Recipe> mRecipeList = new ArrayList<>();
    private static RecipeController sController;
    private int mSelectedIndex = 0;

    private RecipeController(){}

    public static RecipeController getController(){
        if (sController == null){
            sController = new RecipeController();
            Timber.v("getController: new Controller Created");
        }
        Timber.v("getController: returning current controller");
        return sController;
    }

    public ArrayList<Recipe> getRecipeList(){
        return mRecipeList;
    }

    public void addRecipe(Recipe recipe){
        mRecipeList.add(recipe);
        Timber.d("addRecipe: Recipe added.  " + "Total list size is: " + mRecipeList.size());
    }

    public void clear(){
        mRecipeList.clear();
    }

    public void setSelectedIndex(int index){
        mSelectedIndex = index;
    }

    public Recipe getSelected(){
        return mRecipeList.get(mSelectedIndex);
    }

    public int getSelectedIndex(){
        return mSelectedIndex;
    }

}
