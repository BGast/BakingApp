package com.brockgast.android.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
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
import com.brockgast.android.bakingapp.utils.JsonUtils;
import com.brockgast.android.bakingapp.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesFragment extends Fragment {

    private static final String TAG = "RecipesFragment";
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view_recipes)
    RecyclerView mRecipeRecyclerView;

    private ArrayList<Recipe> mRecipeArrayList = new ArrayList<>();

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

        URL recipeUrl = NetworkUtils.urlBuilder();
        new FetchRecipe().execute(recipeUrl);

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
                    URL recipeUrl = NetworkUtils.urlBuilder();
                    new FetchRecipe().execute(recipeUrl);
                } else {
                    mRecipeArrayList.size();
                    URL recipeUrl = NetworkUtils.urlBuilder();
                    new FetchRecipe().execute(recipeUrl);
                }
            }
        });

    }

    class FetchRecipe extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String recipeResponse;

            try {
                recipeResponse = NetworkUtils.getResponseFromHttpUrl(searchUrl); // Gets recipe URL
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return recipeResponse;
        }

        @Override
        protected void onPostExecute(String recipeResponse) {
            new FetchRecipe().cancel(true);
            if (recipeResponse != null && !recipeResponse.equals("")) {

                try {
                    mRecipeArrayList = JsonUtils.extractRecipes(recipeResponse); // Passes URL for JSON data extraction into models
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RecyclerView.Adapter mRecipeAdapter = new RecipeAdapter(getContext(),
                        mRecipeArrayList); // Passes new recipe information to fill activity_main
                mRecipeRecyclerView.setAdapter(mRecipeAdapter);
            } else {
                Log.e(TAG, "onPostExecute: Problems with adapter");
            }
        }
    }
}




























