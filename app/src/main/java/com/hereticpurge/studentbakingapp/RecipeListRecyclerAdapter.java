package com.hereticpurge.studentbakingapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hereticpurge.studentbakingapp.model.Recipe;
import com.hereticpurge.studentbakingapp.model.RecipeController;

import java.util.ArrayList;

public class RecipeListRecyclerAdapter extends RecyclerView.Adapter<RecipeListRecyclerAdapter.ViewHolder> {

    ArrayList<Recipe> mRecipeList;
    MainActivity mMainActivity;

    RecipeListRecyclerAdapter(MainActivity mainActivity){
        mRecipeList = RecipeController.getController().getRecipeList();
        mMainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Recipe currentRecipe = mRecipeList.get(position);

        holder.titleTextView.setText(currentRecipe.getRecipeTitle());
        holder.servingsTextView.setText(currentRecipe.getServings());
        holder.ingredientNumTextView.setText(Integer.toString(currentRecipe.getRecipeIngredients().size()));
        holder.stepsNumTextView.setText(Integer.toString(currentRecipe.getRecipeSteps().size()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeController.getController().setSelectedIndex(position);
                mMainActivity.recipeSelected();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView titleTextView;
        final TextView servingsTextView;
        final TextView ingredientNumTextView;
        final TextView stepsNumTextView;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.titleTextView = itemView.findViewById(R.id.recipe_list_item_title);
            this.servingsTextView = itemView.findViewById(R.id.recipe_list_item_servings);
            this.ingredientNumTextView = itemView.findViewById(R.id.recipe_list_item_numIngredients);
            this.stepsNumTextView = itemView.findViewById(R.id.recipe_list_item_numSteps);
        }
    }
}
