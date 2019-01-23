package com.brockgast.android.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brockgast.android.bakingapp.DetailActivity;
import com.brockgast.android.bakingapp.R;
import com.brockgast.android.bakingapp.adapters.RecipeStepAdapter;
import com.brockgast.android.bakingapp.model.Ingredients;
import com.brockgast.android.bakingapp.model.RecipeSteps;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDescriptionFragment extends Fragment {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view_recipe_step)
    RecyclerView mRecipeStepRecyclerView;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.ingredients_list)
    TextView mIngredientsList;

    /**
     * @param inflater of type LayoutInflater
     * @param container of type ViewGroup
     * @param savedInstanceState of type Bundle
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_recipe_description,
                container, false);

        ButterKnife.bind(this, view);

        showIngredients();

        mRecipeStepRecyclerView.setHasFixedSize(true);

        ArrayList<RecipeSteps> recipeStepsArrayList = ((DetailActivity)
                Objects.requireNonNull(getActivity())).getmRecipeStepsArrayList();

        RecipeStepAdapter mRecipeAdapterClass = new RecipeStepAdapter(
                getContext(), recipeStepsArrayList);

        mRecipeStepRecyclerView.setAdapter(mRecipeAdapterClass);

        RecyclerView.LayoutManager mRecipeManager = new LinearLayoutManager(getContext());
        mRecipeStepRecyclerView.setLayoutManager(mRecipeManager);

        return view;
    }

    /**
     * @param savedInstanceState of type Bundle
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecipesViewModel mViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        mViewModel.getRecipeSteps().observe(this, new Observer<ArrayList<RecipeSteps>>() {
            @Override
            public void onChanged(@Nullable ArrayList<RecipeSteps> recipes) {}
        });

    }

    /**
     * Fills TextView with all needed ingredients for selected desert in fragment_recipe_description
     */
    private void showIngredients() {

        ArrayList<Ingredients> ingredientsArrayList = ((DetailActivity)
                Objects.requireNonNull(getActivity())).getmIngredientsArrayList();

        for (int i = 0; i < ingredientsArrayList.size(); i++) {
            Ingredients ingredients = ingredientsArrayList.get(i);
            String ingredientsList;
            ingredientsList = "(" + ingredients.getQuantity() + " " + ingredients.getMeasure()
                    + ") " + ingredients.getIngredient() + "\n";
            mIngredientsList.append(ingredientsList);
        }
    }
}


