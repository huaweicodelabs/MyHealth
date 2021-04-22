/*
 *
 *  * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package com.huawei.myhealth.java.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.myhealth.R;
import com.huawei.myhealth.java.database.entity.FoodNutrientsInsight;
import com.huawei.myhealth.java.ui.fragments.FoodSearchFragmentDirections;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class FoodNutrientsListAdapter extends ListAdapter<FoodNutrientsInsight, FoodNutrientsListAdapter.FoodNutrientsViewHolder> {

    private Fragment parentFragment;

    public FoodNutrientsListAdapter(Fragment fragment) {
        super(FoodNutrientsInsight.DIFF_CALLBACK);
        this.parentFragment = fragment;
    }

    @NonNull
    @Override
    public FoodNutrientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_food_name_item, parent, false);
        return new FoodNutrientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodNutrientsViewHolder holder, int position) {
        FoodNutrientsInsight foodNutrientsInsight = getItem(position);

        if (foodNutrientsInsight != null) {
            holder.foodNameTV.setText(foodNutrientsInsight.getFoodName());
        }

        holder.foodNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int foodIdGot = foodNutrientsInsight.getFoodId();
                int calorieInFoodGot = foodNutrientsInsight.getEnergyKCal();
                FoodSearchFragmentDirections.ActionFoodSearchFragmentToViewAndAddFoodFragment action =
                        FoodSearchFragmentDirections.actionFoodSearchFragmentToViewAndAddFoodFragment();
                action.setFoodIdArg(foodIdGot);
                action.setCalorieInFoodArg(calorieInFoodGot);
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    static class FoodNutrientsViewHolder extends RecyclerView.ViewHolder {
        private TextView foodNameTV;

        FoodNutrientsViewHolder(View view) {
            super(view);
            foodNameTV = view.findViewById(R.id.foodNameItemTV);
        }

        void bindTo(FoodNutrientsInsight foodNutrientsInsight) {
            foodNameTV.setText(foodNutrientsInsight.getFoodName());
        }
    }
}

