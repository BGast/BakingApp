package com.brockgast.android.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brockgast.android.bakingapp.R;
import com.brockgast.android.bakingapp.adapters.RecipeAdapter;
import com.brockgast.android.bakingapp.model.Recipe;
import com.brockgast.android.bakingapp.utils.RecipeApiClient;
import com.brockgast.android.bakingapp.utils.RecipeJsonApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("NullableProblems")
public class RecipesFragment extends Fragment {

    private static final String TAG = "RecipesFragment";

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view_recipes)
    RecyclerView mRecipeRecyclerView;

    private ArrayList<Recipe> mRecipeArrayList;

    /**
     * @param inflater of type LayoutInflater
     * @param container of type ViewGroup
     * @param savedInstanceState of type Bundle
     * @return view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.recipes_fragment, container, false);

        ButterKnife.bind(this, view);

        mRecipeRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mRecipeManager = new LinearLayoutManager(getContext());
        mRecipeRecyclerView.setLayoutManager(mRecipeManager);

        getRecipe();

        return view;
    }

    /**
     * Sets up ViewModel
     * @param savedInstanceState of type Bundle
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecipesViewModel mViewModel = ViewModelProviders.of(this)
                .get(RecipesViewModel.class);
        mViewModel.getRecipes().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                if (mRecipeArrayList.size() == 0) {
                    getRecipe();
                } else {
                    mRecipeArrayList.size();
                    getRecipe();
                }
            }
        });
    }

    /**
     * Gets all recipe JSON and fills mRecipeArrayList
     */
    private void getRecipe() {

        RecipeJsonApi recipeJsonApi = RecipeApiClient.getApiClient().create(RecipeJsonApi.class);

        Call<List<Recipe>> call = recipeJsonApi.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                mRecipeArrayList = (ArrayList<Recipe>) response.body();
                Log.d(TAG, "onResponse: " + mRecipeArrayList);

                RecyclerView.Adapter mRecipeAdapter = new RecipeAdapter(getContext(),
                        mRecipeArrayList); // Passes new recipe information to fill activity_main
                mRecipeRecyclerView.setAdapter(mRecipeAdapter);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "onFailure: something went wrong: " + t.getMessage());
            }
        });
    }
}