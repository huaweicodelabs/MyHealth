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

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.huawei.myhealth.R;
import com.huawei.myhealth.databinding.FragmentInsightsBinding;
import com.huawei.myhealth.java.database.entity.FoodNutrientsInsight;
import com.huawei.myhealth.java.database.viewmodel.FoodNutrientsViewModel;
import com.huawei.myhealth.java.utils.DefaultValues;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class InsightsFragment extends Fragment {

    private FragmentInsightsBinding binding;
    private FoodNutrientsViewModel foodNutrientsViewModel;

    private String selectedDateStr = null;
    private List<String> calorieTakenTodayList = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInsightsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        foodNutrientsViewModel = new ViewModelProvider(this).get(FoodNutrientsViewModel.class);

        // initialize
        init(view);
        // Receiving Data Via Observer
        foodNutrientsViewModel.allFoodList().observe(getViewLifecycleOwner(), foodNutrientsInsights -> {
            for (FoodNutrientsInsight calorieInEachFood : foodNutrientsInsights) {
                calorieTakenTodayList.add(calorieInEachFood.getProtein());
                calorieTakenTodayList.add(calorieInEachFood.getTotalLipidFat());
                calorieTakenTodayList.add(calorieInEachFood.getCarbohydrate());
                calorieTakenTodayList.add(calorieInEachFood.getFiberTotalDietary());

                updateUI();
            }
        });

        return view;
    }

    private void init(View view) {
        //set current date to calendar
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getResources().getString(R.string.date_pattern));
        String currentDateAndTime = simpleDateFormat.format(new Date());
        binding.tvDate.setText(currentDateAndTime);


        binding.rlCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int y = cal.get(Calendar.YEAR);
                int m = cal.get(Calendar.MONTH);
                int d = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = getResources().getString(R.string.date_pattern); // mention the format you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        // Display Selected date in textbox
                        binding.tvDate.setText("" + sdf.format(cal.getTime()));
                        selectedDateStr = "" + sdf.format(cal.getTime());
                        String selectedDateStr1 = sdf.format(new Date());
                        if (selectedDateStr1.equals(selectedDateStr)) {
                            // Receiving Data Via Observer
                            foodNutrientsViewModel.allFoodList().observe(getViewLifecycleOwner(), foodNutrientsInsights -> {
                                for (FoodNutrientsInsight calorieInEachFood : foodNutrientsInsights) {
                                    calorieTakenTodayList.add(calorieInEachFood.getProtein());
                                    calorieTakenTodayList.add(calorieInEachFood.getTotalLipidFat());
                                    calorieTakenTodayList.add(calorieInEachFood.getCarbohydrate());
                                    calorieTakenTodayList.add(calorieInEachFood.getFiberTotalDietary());
                                }
                                //update the UI
                                updateUI();
                            });
                        } else {
                            calorieTakenTodayList.clear();
                            //update the UI
                            updateUI();
                        }
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

    }

    private void updateUI() {
        if ((calorieTakenTodayList != null) && (calorieTakenTodayList.size() > 0)) {
            binding.tvProteinQ.setText("" + calorieTakenTodayList.get(0) + getResources().getString(R.string.g));
            binding.tvFatQ.setText("" + calorieTakenTodayList.get(1) + getResources().getString(R.string.g));
            binding.tvCarbsQ.setText("" + calorieTakenTodayList.get(2) + getResources().getString(R.string.g));
            binding.tvFiberQ.setText("" + calorieTakenTodayList.get(3) + getResources().getString(R.string.g));
            // set percentage
            int protein_percentage = 0;
            protein_percentage = (int) (((Double.parseDouble(calorieTakenTodayList.get(0))) * DefaultValues.VALUE_10) / DefaultValues.VALUE_2);
            protein_percentage = protein_percentage;
            binding.tvProteinPercentage.setText(String.valueOf(protein_percentage) + getResources().getString(R.string.percentage));
            binding.pbProtein.setProgress(protein_percentage);

            int fat_percentage = 0;
            fat_percentage = (int) (((Double.parseDouble(calorieTakenTodayList.get(1))) * DefaultValues.VALUE_16) / DefaultValues.VALUE_2);
            binding.tvFatPercentage.setText(String.valueOf(fat_percentage) + getResources().getString(R.string.percentage));
            binding.pbFat.setProgress(fat_percentage);

            int carbs_percentage = 0;
            carbs_percentage = (int) (((Double.parseDouble(calorieTakenTodayList.get(2))) * DefaultValues.VALUE_7) / DefaultValues.VALUE_2);
            binding.tvCarbsPercentage.setText(String.valueOf(carbs_percentage) + getResources().getString(R.string.percentage));
            binding.pbCarbs.setProgress(carbs_percentage);

            int fiber_percentage = 0;
            fiber_percentage = (int) (((Double.parseDouble(calorieTakenTodayList.get(3))) * DefaultValues.VALUE_69) / DefaultValues.VALUE_2);
            binding.tvFiberPercentage.setText(fat_percentage + getResources().getString(R.string.percentage));
            binding.pbFiber.setProgress(fiber_percentage);
        } else {
            binding.tvProteinQ.setText(getResources().getString(R.string.zerog));
            binding.tvFatQ.setText(getResources().getString(R.string.zerog));
            binding.tvCarbsQ.setText(getResources().getString(R.string.zerog));
            binding.tvFiberQ.setText(getResources().getString(R.string.zerog));

            binding.tvProteinPercentage.setText(getResources().getString(R.string.zeropercentage));
            binding.pbProtein.setProgress(DefaultValues.DEFAULT_VALUE);

            binding.tvFatPercentage.setText(getResources().getString(R.string.zeropercentage));
            binding.pbFat.setProgress(DefaultValues.DEFAULT_VALUE);

            binding.tvCarbsPercentage.setText(getResources().getString(R.string.zeropercentage));
            binding.pbCarbs.setProgress(DefaultValues.DEFAULT_VALUE);

            binding.tvFiberPercentage.setText(getResources().getString(R.string.zeropercentage));
            binding.pbFiber.setProgress(DefaultValues.DEFAULT_VALUE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}