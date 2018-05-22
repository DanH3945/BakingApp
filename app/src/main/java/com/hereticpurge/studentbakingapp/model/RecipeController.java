package com.hereticpurge.studentbakingapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public final class RecipeController {

    private static final String TAG = "RecipeController";

    private final ArrayList<Recipe> sRecipeList = new ArrayList<>();
    private static RecipeController sController;

    private RecipeController(){}

    public static RecipeController getController(){
        if (sController == null){
            sController = new RecipeController();
        }
        return sController;
    }

    public ArrayList<Recipe> getRecipeList(){
        return sRecipeList;
    }

    public void addRecipe(Recipe recipe){
        sRecipeList.add(recipe);
    }

}
