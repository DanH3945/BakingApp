package com.hereticpurge.studentbakingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecipeListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipe_list_fragment_layout, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recipe_list_recyclerview);
        RecipeListRecyclerAdapter recyclerAdapter = new RecipeListRecyclerAdapter((MainActivity) getActivity());
        recyclerView.setAdapter(recyclerAdapter);

        return root;
    }

}
