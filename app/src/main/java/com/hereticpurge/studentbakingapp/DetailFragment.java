package com.hereticpurge.studentbakingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hereticpurge.studentbakingapp.model.Recipe;

public class DetailFragment extends Fragment{

    Recipe mRecipe;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipe_detail_fragment_layout, container, false);


        return root;
    }

    public void displayRecipe(Recipe recipe){
        mRecipe = recipe;
    }


}
