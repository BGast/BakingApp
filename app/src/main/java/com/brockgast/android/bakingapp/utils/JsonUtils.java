package com.brockgast.android.bakingapp.utils;

import com.brockgast.android.bakingapp.model.Ingredients;
import com.brockgast.android.bakingapp.model.Recipe;
import com.brockgast.android.bakingapp.model.RecipeSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_SERVINGS = "servings";
    private static final String PARAM_INGREDIENTS = "ingredients";
    private static final String PARAM_STEPS = "steps";
    private static final String PARAM_QUANTITY = "quantity";
    private static final String PARAM_MEASURE = "measure";
    private static final String PARAM_INGREDIENT = "ingredient";
    private static final String PARAM_ID = "id";
    private static final String PARAM_SHORT_DESCRIPTION = "shortDescription";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_VIDEO = "videoURL";

    private JsonUtils() {
        throw new AssertionError();
    }

    /**
     * Grabs JSON data from URL and fills appropriate models with necessary information
     * @param json of type String
     * @return result
     */
    public static ArrayList<Recipe> extractRecipes(String json) {
        try {
            Recipe recipe;

            ArrayList<Recipe> result = new ArrayList<>();

            JSONArray recipeJson = new JSONArray(json);

            for (int i = 0; i < recipeJson.length(); i++) {

                int recipeId = recipeJson.getJSONObject(i).getInt(PARAM_ID);

                String recipeName = recipeJson.getJSONObject(i).getString(PARAM_NAME);

                int servingSize = recipeJson.getJSONObject(i).getInt(PARAM_SERVINGS);

                JSONArray ingredientsJson = recipeJson.getJSONObject(i)
                        .getJSONArray(PARAM_INGREDIENTS);

                JSONArray recipeStepsJson = recipeJson.getJSONObject(i).getJSONArray(PARAM_STEPS);

                ArrayList<Ingredients> mIngredientList = new ArrayList<>();
                ArrayList<RecipeSteps> mRecipeStepList = new ArrayList<>();

                for (int j = 0; j < ingredientsJson.length(); j++) {

                    JSONObject objectAt = ingredientsJson.getJSONObject(j);

                    double quantity = objectAt.getDouble(PARAM_QUANTITY);

                    String measure = objectAt.getString(PARAM_MEASURE);

                    String ingredientName = objectAt.getString(PARAM_INGREDIENT);

                    Ingredients ingredient = new Ingredients(quantity, measure, ingredientName);

                    mIngredientList.add(ingredient);
                }

                for (int k = 0; k < recipeStepsJson.length(); k++) {

                    JSONObject objectAt = recipeStepsJson.getJSONObject(k);

                    int stepId = objectAt.getInt(PARAM_ID);

                    String shortDescription = objectAt.getString(PARAM_SHORT_DESCRIPTION);

                    String description = objectAt.getString(PARAM_DESCRIPTION);

                    String videoUrl = objectAt.getString(PARAM_VIDEO);

                    RecipeSteps recipeStep = new RecipeSteps(stepId, shortDescription,
                            description, videoUrl);

                    mRecipeStepList.add(recipeStep);
                }

                recipe = new Recipe(recipeId, recipeName, servingSize,
                        mIngredientList, mRecipeStepList);

                result.add(recipe);
            }
            return result;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
