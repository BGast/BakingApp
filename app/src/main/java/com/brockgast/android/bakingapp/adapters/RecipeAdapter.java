package com.brockgast.android.bakingapp.adapters;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brockgast.android.bakingapp.DetailActivity;
import com.brockgast.android.bakingapp.R;
import com.brockgast.android.bakingapp.model.Recipe;
import com.brockgast.android.bakingapp.widget.RecipeWidgetProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private static final String TAG = "RecipeAdapter";

    private final Context mContext;
    private final ArrayList<Recipe> mRecipe;

    /**
     * Constructor for RecipeAdapter
     * @param context of type Context
     * @param recipe of type ArrayList<Recipe>
     */
    public RecipeAdapter(Context context, ArrayList<Recipe> recipe) {
        this.mContext = context;
        this.mRecipe = recipe;
    }

    /**
     * @param parent of type ViewGroup
     * @param viewType of type int
     * @return RecipeHolder(recipeView)
     */
    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipeView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recipes, parent, false);

        return new RecipeHolder(recipeView);
    }

    /**
     * @param recipeHolder of type RecipeHolder
     * @param position of type int
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeHolder recipeHolder, int position) {
        recipeHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipe == null ? 0 : mRecipe.size();
    }

    class RecipeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dessert_name)
        TextView recipeName;
        @BindView(R.id.dessert_servings)
        TextView recipeServings;

        RecipeHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {   // Sets recipe information in activity_main
            Log.d(TAG, "bind: onBindViewHolder called");

            Recipe recipeItem = mRecipe.get(position);
            recipeName.setText(recipeItem.getName());
            recipeServings.setText(String.valueOf(recipeItem.getServings()));

        }

        @OnClick
        public void onClick(View view) {    // Sets onClick for recipes in activity_main
            int position = getAdapterPosition();
            Recipe selectedRecipe = mRecipe.get(position);

            Intent widgetIntent = new Intent(mContext, RecipeWidgetProvider.class);
            widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            widgetIntent.putExtra
                    (mContext.getString(R.string.recipe_name), selectedRecipe.getName());
            widgetIntent.putExtra
                    (mContext.getString
                            (R.string.ingredients_text), selectedRecipe.getIngredientsList());
            mContext.sendBroadcast(widgetIntent);

            Intent detailActivityIntent = new Intent(mContext, DetailActivity.class);
            detailActivityIntent.putExtra(mContext.getString(R.string.recipe_intent_key), selectedRecipe);
            mContext.startActivity(detailActivityIntent);
        }
    }
}
