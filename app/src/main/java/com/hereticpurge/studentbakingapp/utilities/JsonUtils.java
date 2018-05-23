package com.hereticpurge.studentbakingapp.utilities;

import com.hereticpurge.studentbakingapp.model.RecipeBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Dilbert on 5/17/2018.
 */

public final class JsonUtils {

    private static final String TAG = "JsonUtils";

    public static void populateRecipesFromJson(String jsonString){

        RecipeBuilder builder = new RecipeBuilder();
        Timber.d("populateRecipesFromJson: RecipeBuilder created");

        try {
            JSONArray baseArray = new JSONArray(jsonString);
            for(int recipeNum = 0; recipeNum < baseArray.length(); recipeNum++){

                JSONObject recipeJsonObject = baseArray.getJSONObject(recipeNum);
                String id = recipeJsonObject.getString("id");
                String name = recipeJsonObject.getString("name");

                Timber.d("populateRecipesFromJson: setting id and name for recipe #: " + Integer.toString(recipeNum));

                builder.setId(id).setTitle(name);

                JSONArray ingredientArray = recipeJsonObject.getJSONArray("ingredients");

                for(int ingredientNum = 0; ingredientNum < ingredientArray.length(); ingredientNum++){
                    JSONObject ingredientObject = ingredientArray.getJSONObject(ingredientNum);

                    Map<String, String> ingredient = new HashMap<>();
                    ingredient.put(RecipeBuilder.INGREDIENT_QUANTITY, ingredientObject.getString(RecipeBuilder.INGREDIENT_QUANTITY));
                    ingredient.put(RecipeBuilder.INGREDIENT_MEASURE, ingredientObject.getString(RecipeBuilder.INGREDIENT_MEASURE));
                    ingredient.put(RecipeBuilder.INGREDIENT, ingredientObject.getString(RecipeBuilder.INGREDIENT));
                    builder.addIngredient(ingredient);

                    Timber.d("populateRecipesFromJson: attempting Recipe #: " + Integer.toString(recipeNum) + " & Ingredient #: " + Integer.toString(ingredientNum));
                }

                JSONArray stepArray = recipeJsonObject.getJSONArray("steps");

                for (int stepNum = 0; stepNum < stepArray.length(); stepNum++){
                    JSONObject stepObject = stepArray.getJSONObject(stepNum);

                    Map<String, String> step = new HashMap<>();
                    step.put(RecipeBuilder.STEP_ID, stepObject.getString(RecipeBuilder.STEP_ID));
                    step.put(RecipeBuilder.STEP_SHORT_DESCRIPTION, stepObject.getString(RecipeBuilder.STEP_SHORT_DESCRIPTION));
                    step.put(RecipeBuilder.STEP_DESCRIPTION, stepObject.getString(RecipeBuilder.STEP_DESCRIPTION));
                    step.put(RecipeBuilder.STEP_VIDEO_URL, stepObject.getString(RecipeBuilder.STEP_VIDEO_URL));
                    step.put(RecipeBuilder.STEP_THUMBNAIL_URL, stepObject.getString(RecipeBuilder.STEP_THUMBNAIL_URL));

                    builder.addStep(step);

                    Timber.d("populateRecipesFromJson: attempting Recipe #: " + Integer.toString(recipeNum) + " & Step #: " + Integer.toString(stepNum));
                }

            }
            Timber.d("Calling RecipeBuilder.build()");
            builder.build();

        } catch (JSONException e) {
            Timber.e("populateRecipesFromJson: Json Exception Caught");
        }

    }

}
