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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.huawei.myhealth.R;
import com.huawei.myhealth.databinding.FragmentViewAddFoodBinding;
import com.huawei.myhealth.java.database.entity.FoodIntake;
import com.huawei.myhealth.java.database.viewmodel.FoodIntakeViewModel;

import java.util.Date;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class ViewAndAddFoodFragment extends Fragment implements View.OnClickListener {

    private FragmentViewAddFoodBinding binding;
    private int foodIdGot = 0;
    private int calorieInFoodGot = 0;
    private String portionTaken = "0";
    private int portionTakenListPosition = 0;
    private String portionType = null;

    private FoodIntakeViewModel foodIntakeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewAddFoodBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        foodIntakeViewModel = new ViewModelProvider(this).get(FoodIntakeViewModel.class);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        foodIdGot = ViewAndAddFoodFragmentArgs.fromBundle(getArguments()).getFoodIdArg();
        calorieInFoodGot = ViewAndAddFoodFragmentArgs.fromBundle(getArguments()).getCalorieInFoodArg();

        populateNoOfPortion();
        populatePortionType();

        binding.addCalorieCountBtn.setOnClickListener(this);

        binding.noOfPortionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                portionTakenListPosition = position;
                if (position != 0) {
                    portionTaken = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.portionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    portionType = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void populateNoOfPortion() {
        ArrayAdapter<CharSequence> noOfPortionAdapter = ArrayAdapter.createFromResource(getContext(), R.array.no_of_portion,
                android.R.layout.simple_spinner_item);
        noOfPortionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.noOfPortionSpinner.setAdapter(noOfPortionAdapter);
    }

    private void populatePortionType() {
        ArrayAdapter<CharSequence> portionTypeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.portion_type_for_fruits,
                android.R.layout.simple_spinner_item);
        portionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.portionTypeSpinner.setAdapter(portionTypeAdapter);
    }

    private void saveFoodTakenData(int foodIdGot, int calorieInFood) {
        FoodIntake calorieTaken = new FoodIntake(0, getResources().getString(R.string.admin),
                getResources().getString(R.string.morning), foodIdGot,
                getResources().getString(R.string.foodname), calorieInFood, portionTaken, new Date(), new Date());
        foodIntakeViewModel.insert(calorieTaken);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addCalorieCountBtn) {
            if (portionTakenListPosition != 0) {
                saveFoodTakenData(foodIdGot, calorieInFoodGot);
                Snackbar.make(binding.viewAddFoodParentLay, getResources().getString(R.string.intake_success), Snackbar.LENGTH_LONG).show();
                NavHostFragment.findNavController(this).navigateUp();
            } else {
                Snackbar.make(binding.viewAddFoodParentLay, getResources().getString(R.string.select_portion), Snackbar.LENGTH_LONG).show();
            }
        }
    }
}