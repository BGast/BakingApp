package com.brockgast.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.brockgast.android.bakingapp.model.Ingredients;
import com.brockgast.android.bakingapp.model.Recipe;
import com.brockgast.android.bakingapp.model.RecipeSteps;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    private final Ingredients testIngredient = new Ingredients(1000, "TBLSP",
            "unsalted butter, melted");

    private final RecipeSteps testStep = new RecipeSteps(1,
            "Test Recipe Introduction --> short description",
            "Test Recipe Introduction --> description",
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4");

    private final ArrayList<Ingredients> testIngredients = new ArrayList<>();
    private final ArrayList<RecipeSteps> testSteps = new ArrayList<>();

    @Rule
    public final ActivityTestRule mActivityRule =
            new ActivityTestRule(DetailActivity.class, false, false);

    @Test
    public void checkStepCardView() {
        testIngredients.add(testIngredient);
        testSteps.add(testStep);
        Recipe testRecipe = new Recipe(0, "CupCake", 8, testIngredients, testSteps);
        Intent testIntent = new Intent();
        testIntent.putExtra(mContext.getString(R.string.recipe_intent_key), testRecipe);
        mActivityRule.launchActivity(testIntent);
        onView(withId(R.id.dessert_step_short_description)).check(matches(withText("Test Recipe Introduction --> short description")));
    }

    @Test
    public void testIngredientsView() {
        testIngredients.add(testIngredient);
        testSteps.add(testStep);
        Recipe testRecipe = new Recipe(0, "Brownies", 8, testIngredients, testSteps);
        Intent testIntent = new Intent();
        testIntent.putExtra(mContext.getString(R.string.recipe_intent_key), testRecipe);
        mActivityRule.launchActivity(testIntent);
        onView(withId(R.id.ingredients_list)).perform().check(matches(isDisplayed()));
    }

    @Test
    public void checkStepOpen() {
        testIngredients.add(testIngredient);
        testSteps.add(testStep);
        Recipe testRecipe = new Recipe(2, "Pie", 8, testIngredients, testSteps);
        Intent testIntent = new Intent();
        testIntent.putExtra(mContext.getString(R.string.recipe_intent_key), testRecipe);
        mActivityRule.launchActivity(testIntent);
        onView(withId(R.id.recycler_view_recipe_step)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.video_holder_frame_layout)).check(matches(isDisplayed()));
    }

}
