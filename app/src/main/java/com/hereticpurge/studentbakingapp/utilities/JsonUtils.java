package com.hereticpurge.studentbakingapp.utilities;

import com.hereticpurge.studentbakingapp.model.RecipeBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dilbert on 5/17/2018.
 */

public final class JsonUtils {

    private static final String TAG = "JsonUtils";

    public static void populateRecipesFromJson(String jsonString){

        RecipeBuilder builder = new RecipeBuilder();

        try {
            JSONArray baseArray = new JSONArray(jsonString);
            for(int i = 0; i < baseArray.length(); i++){

                JSONObject recipeJsonObject = baseArray.getJSONObject(i);
                String id = recipeJsonObject.getString("id");
                String name = recipeJsonObject.getString("name");
                builder.setId(id).setTitle(name);

                JSONArray ingredientArray = recipeJsonObject.getJSONArray("ingredients");

                for(int x = 0; x < ingredientArray.length(); x++){
                    JSONObject ingredientObject = ingredientArray.getJSONObject(x);

                    Map<String, String> ingredient = new HashMap<>();
                    ingredient.put(RecipeBuilder.INGREDIENT_QUANTITY, ingredientObject.getString("quantity"));
                    ingredient.put(RecipeBuilder.INGREDIENT_MEASURE, ingredientObject.getString("measure"));
                    ingredient.put(RecipeBuilder.INGREDIENT, ingredientObject.getString("ingredient"));
                    builder.addIngredient(ingredient);
                }

                JSONArray stepArray = recipeJsonObject.getJSONArray("steps");

                for (int y = 0; y < stepArray.length(); y++){
                    JSONObject stepObject = stepArray.getJSONObject(y);

                    Map<String, String> step = new HashMap<>();
                    step.put(RecipeBuilder.STEP_ID, stepObject.getString("id"));
                    step.put(RecipeBuilder.STEP_SHORT_DESCRIPTION, stepObject.getString("shortDescription"));
                    step.put(RecipeBuilder.STEP_DESCRIPTION, stepObject.getString("description"));
                    step.put(RecipeBuilder.STEP_VIDEO_URL, stepObject.getString("videoURL"));
                    step.put(RecipeBuilder.STEP_THUMBNAIL_URL, stepObject.getString("thumbnailURL"));

                    builder.addStep(step);
                }

            }
            builder.build();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
