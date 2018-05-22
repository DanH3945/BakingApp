package com.hereticpurge.studentbakingapp.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

public final class RecipeController {

    private static final String TAG = "RecipeController";

    private ArrayList<Recipe> mRecipeList;
    private static RecipeController sController;

    //step map TAGs
    public static final String STEP_ID = "id";
    public static final String STEP_SHORT_DESCRIPTION = "shortDesc";
    public static final String STEP_DESCRIPTION = "description";
    public static final String STEP_VIDEO_URL = "videoUrl";
    public static final String STEP_THUMBNAIL_URL = "thumbUrl";

    //ingredient map TAGs
    public static final String INGREDIENT_QUANTITY = "quantity";
    public static final String INGREDIENT_MEASURE = "measure";
    public static final String INGREDIENT = "ingredient";

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

    private class RecipeBuilder {
        //the recipe being built
        Recipe mCurrentRecipe;

        //boolean to determine if this recipe has already been built and therefore shouldn't
        // be changed.
        private boolean mBuilt = false;

        private RecipeBuilder(){
            mCurrentRecipe = new Recipe();
        }

        public Recipe setId(String id){
            mCurrentRecipe.setRecipeId(id);
            return mCurrentRecipe;
        }

        public Recipe setTitle(String title){
            mCurrentRecipe.setRecipeTitle(title);
            return mCurrentRecipe;
        }

        public Recipe addStep(Map<String, String> step){
            mCurrentRecipe.addStep(step.get(STEP_ID),
                    step.get(STEP_SHORT_DESCRIPTION),
                    step.get(STEP_DESCRIPTION),
                    step.get(STEP_VIDEO_URL),
                    step.get(STEP_THUMBNAIL_URL));
            return mCurrentRecipe;
        }

        public Recipe addIngredient(Map<String, String> ingredient){
            mCurrentRecipe.addIngredient(ingredient.get(INGREDIENT_QUANTITY),
                    ingredient.get(INGREDIENT_MEASURE),
                    ingredient.get(INGREDIENT));
            return mCurrentRecipe;
        }

        public void build(){
            if (!mBuilt) {
                mRecipeList.add(mCurrentRecipe);
            }
            mBuilt = true;
        }

    }

}
