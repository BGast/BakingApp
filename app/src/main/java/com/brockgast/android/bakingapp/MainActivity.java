package com.brockgast.android.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.brockgast.android.bakingapp.fragments.RecipesFragment;

public class MainActivity extends AppCompatActivity {

    /**
     *
     * @param savedInstanceState of type Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            RecipesFragment recipes = new RecipesFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.main_layout, recipes).commit();
        }
        setContentView(R.layout.activity_main);
    }
}
