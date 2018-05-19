package com.hereticpurge.studentbakingapp.model;

import android.util.Log;

import java.util.ArrayList;

public final class RecipeController {

    private static final String TAG = "RecipeController";

    private ArrayList<Recipe> mRecipeList;
    private static RecipeController mController;

    private RecipeController(){
        mRecipeList = new ArrayList<>();
    }

    public static RecipeController getController(){
        if (mController == null){
            mController = new RecipeController();
            Log.d(TAG, "getController: Created controller");
        }
        Log.d(TAG, "getController: Returning controller");
        return mController;
    }

    public ArrayList<Recipe> getRecipeList(){
        Log.d(TAG, "getRecipeList: Returning recipe list");
        return mRecipeList;
    }

    public RecipeBuilder getRecipeBuilder(){
        return new RecipeBuilder();

    }

    public class RecipeBuilder {
        Recipe mCurrentRecipe;

        private RecipeBuilder(){}

    }

}
