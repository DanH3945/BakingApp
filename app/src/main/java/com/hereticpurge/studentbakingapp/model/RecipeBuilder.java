package com.hereticpurge.studentbakingapp.model;

import java.util.Map;

public class RecipeBuilder {

    //step map TAGs
    public static final String STEP_ID = "id";
    public static final String STEP_SHORT_DESCRIPTION = "shortDescription";
    public static final String STEP_DESCRIPTION = "description";
    public static final String STEP_VIDEO_URL = "videoURL";
    public static final String STEP_THUMBNAIL_URL = "thumbnailURL";

    //ingredient map TAGs
    public static final String INGREDIENT_QUANTITY = "quantity";
    public static final String INGREDIENT_MEASURE = "measure";
    public static final String INGREDIENT = "ingredient";
    //the recipe being built
    private final Recipe mCurrentRecipe;

    //boolean to determine if this recipe has already been built and therefore shouldn't
    // be changed.
    private boolean mBuilt = false;
    private final RecipeController sController;

    public RecipeBuilder(){
        mCurrentRecipe = new Recipe();
        sController = RecipeController.getController();
    }

    public RecipeBuilder setId(String id) {
        mCurrentRecipe.setRecipeId(id);
        return this;
    }

    public RecipeBuilder setTitle(String title) {
        mCurrentRecipe.setRecipeTitle(title);
        return this;
    }

    public RecipeBuilder addStep(Map<String, String> step) {
        mCurrentRecipe.addStep(step.get(STEP_ID),
                step.get(STEP_SHORT_DESCRIPTION),
                step.get(STEP_DESCRIPTION),
                step.get(STEP_VIDEO_URL),
                step.get(STEP_THUMBNAIL_URL));
        return this;
    }

    public RecipeBuilder addIngredient(Map<String, String> ingredient) {
        mCurrentRecipe.addIngredient(ingredient.get(INGREDIENT_QUANTITY),
                ingredient.get(INGREDIENT_MEASURE),
                ingredient.get(INGREDIENT));
        return this;
    }

    public void build() {
        if (!mBuilt) {
            sController.addRecipe(mCurrentRecipe);
        }
        mBuilt = true;
    }


}
