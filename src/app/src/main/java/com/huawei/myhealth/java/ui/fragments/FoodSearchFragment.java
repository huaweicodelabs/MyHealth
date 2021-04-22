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

package com.huawei.myhealth.java.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huawei.myhealth.R;

import com.huawei.myhealth.databinding.FragmentFoodSearchBinding;
import com.huawei.myhealth.java.adapter.FoodNutrientsListAdapter;
import com.huawei.myhealth.java.database.entity.FoodNutrientsInsight;
import com.huawei.myhealth.java.database.viewmodel.FoodNutrientsViewModel;
import com.huawei.myhealth.java.utils.DefaultValues;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class FoodSearchFragment extends Fragment {

    private FragmentFoodSearchBinding binding;

    private FoodNutrientsViewModel foodNutrientsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFoodSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        foodNutrientsViewModel = new ViewModelProvider(this).get(FoodNutrientsViewModel.class);

        FoodNutrientsListAdapter foodNameListAdapter = new FoodNutrientsListAdapter(this);
        binding.foodNameListRecyclerView.setAdapter(foodNameListAdapter);
        binding.foodNameListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        populateDatabase();

        return view;
    }

    private void populateDatabase() {
        FoodNutrientsInsight foodNutrientsInsight = new FoodNutrientsInsight(DefaultValues.FOODID, getResources().getString(R.string.foodname), getResources().getString(R.string.foodportiontype), DefaultValues.FOODID, getResources().getString(R.string.foodthumbnail), getResources().getString(R.string.foodpic),
                DefaultValues.ENERGY_VALUE, getResources().getString(R.string.foodprotein), getResources().getString(R.string.foodfat), getResources().getString(R.string.foodcarbohydrate), getResources().getString(R.string.foodfibre), getResources().getString(R.string.foodsugar),
                String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE),
                String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE),
                String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE),
                String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE), String.valueOf(DefaultValues.ENERGY_VALUE));

        foodNutrientsViewModel.insert(foodNutrientsInsight);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}