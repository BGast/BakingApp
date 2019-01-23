package com.brockgast.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.brockgast.android.bakingapp.fragments.RecipeDescriptionFragment;
import com.brockgast.android.bakingapp.fragments.RecipeInstructionsFragment;
import com.brockgast.android.bakingapp.model.Ingredients;
import com.brockgast.android.bakingapp.model.Recipe;
import com.brockgast.android.bakingapp.model.RecipeSteps;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private Boolean mIsDualPane;
    private int mCurrentPosition;
    private ArrayList<Ingredients> mIngredientsArrayList = new ArrayList<>();
    private ArrayList<RecipeSteps> mRecipeStepsArrayList = new ArrayList<>();

    private RecipeInstructionsFragment mRecipeInstructionsFragment;

    /**
     * @param savedInstanceState of type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.recipe_intent_key))) {
            Recipe recipe = intent.getParcelableExtra(getString(R.string.recipe_intent_key));

            String mRecipeName = recipe.getName();
            Objects.requireNonNull(getSupportActionBar()).setTitle(mRecipeName);

            mIngredientsArrayList = recipe.getIngredientsList();
            mRecipeStepsArrayList = recipe.getRecipeSteps();
        }

        mIsDualPane = getResources().getBoolean(R.bool.isDualPane);

        RecipeDescriptionFragment mRecipeDescriptionFragment = new RecipeDescriptionFragment();
        mRecipeInstructionsFragment = new RecipeInstructionsFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.recipe_detail_fragment_container, mRecipeDescriptionFragment,
                            getString(R.string.description_list_fragment))
                    .commit();
        }
        setContentView(R.layout.activity_detail);
    }

    /**
     * Opens fragment_recipe_instruction.
     * Decides whether to replace fragment_recipe_description
     * or not based on if user is using a tablet.
     */
    public void openRecipeInstructionsFragment() {
        if (mIsDualPane) {
            RecipeInstructionsFragment recipeInstructionsFragment = (RecipeInstructionsFragment)
                    getSupportFragmentManager()
                    .findFragmentByTag(getString(R.string.instruction_fragment));
            if (recipeInstructionsFragment != null && recipeInstructionsFragment.isVisible()) {
                recipeInstructionsFragment.stepChange(mCurrentPosition);
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.instruction_fragment_container, mRecipeInstructionsFragment,
                                getString(R.string.instruction_fragment))
                        .commit();
            }
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.recipe_detail_fragment_container, mRecipeInstructionsFragment,
                            getString(R.string.instruction_fragment))
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Allows fragments to grab mIngredientsArrayList
     * @return mIngredientsArrayList
     */
    public ArrayList<Ingredients> getmIngredientsArrayList() {
        return mIngredientsArrayList;
    }

    /**
     * Allows fragments to grab mRecipeStepsArrayList
     * @return mRecipeStepsArrayList
     */
    public ArrayList<RecipeSteps> getmRecipeStepsArrayList() {
        return mRecipeStepsArrayList;
    }

    /**
     * Allows fragments to grab mCurrentPosition
     * @return mCurrentPosition
     */
    public int getmCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * Allows fragments to communicate and change mCurrentPosition between each other
     * @param mCurrentPosition of type int
     */
    public void setmCurrentPosition(int mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
    }
}
