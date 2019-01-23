package com.brockgast.android.bakingapp.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.brockgast.android.bakingapp.model.Recipe;
import com.brockgast.android.bakingapp.model.RecipeSteps;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class RecipesViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<RecipeSteps>> recipeSteps = new MutableLiveData<>();

    public LiveData<ArrayList<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<ArrayList<RecipeSteps>> getRecipeSteps() {
        return recipeSteps;
    }
}
