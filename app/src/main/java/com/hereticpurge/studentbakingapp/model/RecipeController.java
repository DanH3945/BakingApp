package com.hereticpurge.studentbakingapp.model;

import android.util.Log;

import java.util.ArrayList;

public final class RecipeController {

    private static final String TAG = "RecipeController";

    private ArrayList<Recipe> mRecipeList;
    private static RecipeController sController;

    private RecipeController(){
        mRecipeList = new ArrayList<>();
    }

    public static RecipeController getController(){
        if (sController == null){
            sController = new RecipeController();
        }
        return sController;
    }

    public ArrayList<Recipe> getRecipeList(){
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
