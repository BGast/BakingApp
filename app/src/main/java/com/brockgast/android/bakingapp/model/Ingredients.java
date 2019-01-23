package com.brockgast.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Ingredients implements Parcelable {

    private final double quantity;
    private final String measure;
    private final String ingredient;

    public Ingredients(double quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    Ingredients(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    /**
     * Returns quantity for filling ingredients_list in fragment_recipe_description
     * @return quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Returns measure for filling ingredients_list in fragment_recipe_description
     * @return measure
     */
    public String getMeasure() {
        return measure;
    }

    /**
     * Returns ingredient for filling ingredients_list in fragment_recipe_description
     * @return ingredient
     */
    public String getIngredient() {
        return ingredient;
    }

    @NonNull
    @Override
    public String toString() {
        return "Ingredients{" +
                ", quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }
}
