package com.hereticpurge.studentbakingapp.model;

import java.util.ArrayList;

public class Recipe {

    private String mRecipeId;
    private String mRecipeTitle;
    private String mServings;
    private ArrayList<RecipeStep> mRecipeSteps;
    private ArrayList<RecipeIngredient> mRecipeIngredients;

    public Recipe(){
        mRecipeSteps = new ArrayList<>();
        mRecipeIngredients = new ArrayList<>();
    }

    public void addIngredient(String quantity, String measure, String ingredient){

        RecipeIngredient recipeIngredient = new RecipeIngredient(quantity, measure, ingredient);
        mRecipeIngredients.add(recipeIngredient);

    }

    public void addStep(String stepId, String shortDescription, String description, String videoUrl, String thumbnailUrl){

        RecipeStep recipeStep = new RecipeStep(stepId, shortDescription, description, videoUrl, thumbnailUrl);
        mRecipeSteps.add(recipeStep);

    }

    public String getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(String recipeId) {
        mRecipeId = recipeId;
    }

    public String getRecipeTitle() {
        return mRecipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        mRecipeTitle = recipeTitle;
    }

    public String getServings(){
        return mServings;
    }

    public void setServes(String servings){
        this.mServings = servings;
    }

    public ArrayList<RecipeStep> getRecipeSteps() {
        return mRecipeSteps;
    }

    public void setRecipeSteps(ArrayList<RecipeStep> recipeSteps) {
        mRecipeSteps = recipeSteps;
    }

    public ArrayList<RecipeIngredient> getRecipeIngredients() {
        return mRecipeIngredients;
    }

    public void setRecipeIngredients(ArrayList<RecipeIngredient> recipeIngredients) {
        mRecipeIngredients = recipeIngredients;
    }

    public class RecipeStep {

        private String mStepId;
        private String mShortDescription;
        private String mDescription;
        private String mVideoUrl;
        private String mThumbnailUrl;

        RecipeStep(String stepId, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
            mStepId = stepId;
            mShortDescription = shortDescription;
            mDescription = description;
            mVideoUrl = videoUrl;
            mThumbnailUrl = thumbnailUrl;
        }

        public String getStepId() {
            return mStepId;
        }

        public void setStepId(String stepId) {
            mStepId = stepId;
        }

        public String getShortDescription() {
            return mShortDescription;
        }

        public void setShortDescription(String shortDescription) {
            mShortDescription = shortDescription;
        }

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
        }

        public String getVideoUrl() {
            return mVideoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            mVideoUrl = videoUrl;
        }

        public String getThumbnailUrl() {
            return mThumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            mThumbnailUrl = thumbnailUrl;
        }
    }

    public class RecipeIngredient {

        private String mQuantity;
        private String mMeasure;
        private String mIngredient;

        RecipeIngredient(String quantity, String measure, String ingredient) {
            mQuantity = quantity;
            mMeasure = measure;
            mIngredient = ingredient;
        }

        public String getQuantity() {
            return mQuantity;
        }

        public void setQuantity(String quantity) {
            mQuantity = quantity;
        }

        public String getMeasure() {
            return mMeasure;
        }

        public void setMeasure(String measure) {
            mMeasure = measure;
        }

        public String getIngredient() {
            return mIngredient;
        }

        public void setIngredient(String ingredient) {
            mIngredient = ingredient;
        }
    }

}
