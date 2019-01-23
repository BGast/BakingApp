package com.brockgast.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class RecipeSteps implements Parcelable {

    private final int id;
    private final String shortDescription;
    private final String description;
    private final String videoUrl;

    public RecipeSteps(int id, String shortDescription, String description, String videoUrl) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    RecipeSteps(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeSteps> CREATOR = new Creator<RecipeSteps>() {
        @Override
        public RecipeSteps createFromParcel(Parcel in) {
            return new RecipeSteps(in);
        }

        @Override
        public RecipeSteps[] newArray(int size) {
            return new RecipeSteps[size];
        }
    };

    /**
     * Gets shortDescription for dessert_step_short_description in layout_recipe_steps
     * @return shortDescription
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Gets description for recipe_step_description in fragment_recipe_instructions
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets URL to provide video for exoplayer in fragment_recipe_instructions
     * @return videoUrl
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "RecipeSteps{" +
                "id=" + id +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
