package com.hereticpurge.studentbakingapp.model;

import java.util.ArrayList;

import timber.log.Timber;

public final class RecipeController {

    private final ArrayList<Recipe> sRecipeList = new ArrayList<>();
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

    public ArrayList<Recipe> getRecipeList(){
        return sRecipeList;
    }

    public void addRecipe(Recipe recipe){
        sRecipeList.add(recipe);
        Timber.d("addRecipe: Recipe added");
    }

}
