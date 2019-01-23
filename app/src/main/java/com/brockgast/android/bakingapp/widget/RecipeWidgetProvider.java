package com.brockgast.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.brockgast.android.bakingapp.R;
import com.brockgast.android.bakingapp.model.Ingredients;

import java.util.ArrayList;

import butterknife.BindView;

public class RecipeWidgetProvider extends AppWidgetProvider {

    @BindView(R.id.widget_recipe_title)
    TextView mRecipeNameTextView;
    @BindView(R.id.widget_recipe_ingredients)
    TextView mIngredientsTextView;

    private static String mRecipeName = "";
    private static ArrayList<Ingredients> ingredientsArrayList = new ArrayList<>();

    /**
     * @param context of type Context
     * @param appWidgetManager of type AppWidgetManager
     * @param appWidgetId of type int
     */
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        CharSequence widgetRecipeNameText = mRecipeName;

        StringBuilder ingredientsBuilder = new StringBuilder();
        for (Ingredients i : ingredientsArrayList) {
            ingredientsBuilder.append("(").append(i.getQuantity()).append(" ").append
                    (i.getMeasure()).append(") ").append(i.getIngredient()).append("\n");
        }
        String ingredients = ingredientsBuilder.toString();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        if (!mRecipeName.equals("") && !ingredients.isEmpty()) {
            views.setTextViewText(R.id.widget_recipe_title, widgetRecipeNameText);
            views.setTextViewText(R.id.widget_recipe_ingredients, ingredients);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * @param context of type Context
     * @param appWidgetManager of type AppWidgetManager
     * @param appWidgetIds of type int[]
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    /**
     * @param context of type Context
     * @param intent of type Intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String recipeSearchTag = context.getString(R.string.recipe_name);
        String ingredientsSearchTag = context.getString(R.string.ingredients_text);
        if (intent.hasExtra(recipeSearchTag) && intent.hasExtra(ingredientsSearchTag)) {
            mRecipeName = intent.getStringExtra(recipeSearchTag);
            ingredientsArrayList = intent.getParcelableArrayListExtra(ingredientsSearchTag);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context.getPackageName(),
                    RecipeWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}

