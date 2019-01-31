package com.brockgast.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Recipe implements Parcelable {

    @Expose
    private final int id;
    @Expose
    private final String name;
    @Expose
    private final int servings;
    @SerializedName("ingredients")
    @Expose
    private final ArrayList<Ingredients> ingredientsList;
    @SerializedName("steps")
    @Expose
    private final ArrayList<RecipeSteps> recipeSteps;

    public Recipe(int id, String name, int servings, ArrayList<Ingredients> ingredientsList, ArrayList<RecipeSteps> recipeSteps) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.ingredientsList = ingredientsList;
        this.recipeSteps = recipeSteps;
    }

    // Used http://www.parcelabler.com/ to help with this
    private Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        if (in.readByte() == 0x01) {
            ingredientsList = new ArrayList<>();
            in.readList(ingredientsList, Ingredients.class.getClassLoader());
        } else {
            ingredientsList = null;
        }
        if (in.readByte() == 0x01) {
            recipeSteps = new ArrayList<>();
            in.readList(recipeSteps, RecipeSteps.class.getClassLoader());
        } else {
            recipeSteps = null;
        }
    }

    // Used http://www.parcelabler.com/ to help with this
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        if (ingredientsList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredientsList);
        }
        if (recipeSteps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(recipeSteps);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    /**
     * Gets name for dessert_name in layout_recipes
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets servings for dessert_servings in layout_recipes
     * @return servings
     */
    public int getServings() {
        return servings;
    }

    /**
     * @return ingredientsList
     */
    public ArrayList<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    /**
     * @return recipeSteps
     */
    public ArrayList<RecipeSteps> getRecipeSteps() {
        return recipeSteps;
    }

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", servings=" + servings +
                ", ingredientsList=" + ingredientsList +
                ", recipeSteps=" + recipeSteps +
                '}';
    }
}
