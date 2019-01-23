package com.brockgast.android.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brockgast.android.bakingapp.DetailActivity;
import com.brockgast.android.bakingapp.R;
import com.brockgast.android.bakingapp.model.RecipeSteps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepHolder> {

    private final Context mContext;
    private final ArrayList<RecipeSteps> mRecipeStepsArrayList;

    /**
     * Constructor for RecipeStepAdapter
     * @param context of type Context
     * @param recipeStepsArrayList of type ArrayList<RecipeSteps>
     */
    public RecipeStepAdapter(Context context, ArrayList<RecipeSteps> recipeStepsArrayList) {
        this.mContext = context;
        this.mRecipeStepsArrayList = recipeStepsArrayList;
    }

    /**
     * @param parent of type ViewGroup
     * @param viewType of type int
     * @return RecipeStepHolder(recipeStepView)
     */
    @NonNull
    @Override
    public RecipeStepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipeStepView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recipe_steps, parent, false);

        return new RecipeStepHolder(recipeStepView);
    }

    /**
     * @param recipeStepHolder of type RecipeStepHolder
     * @param position of type int
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeStepHolder recipeStepHolder, int position) {
        recipeStepHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipeStepsArrayList == null ? 0 : mRecipeStepsArrayList.size();
    }

    class RecipeStepHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.dessert_step_short_description)
        TextView shortDescription;

        RecipeStepHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {   // Sets recipe shortDescription in fragment_recipe_description
            RecipeSteps recipeStep = mRecipeStepsArrayList.get(position);
            shortDescription.setText(recipeStep.getShortDescription());
        }

        @OnClick
        public void onClick(View view) {    // Sets onClick for shortDescription in fragment_recipe_description
            int position = getAdapterPosition();
            DetailActivity parentActivity = (DetailActivity) mContext;
            parentActivity.setmCurrentPosition(position);
            parentActivity.openRecipeInstructionsFragment();

        }

    }
}
