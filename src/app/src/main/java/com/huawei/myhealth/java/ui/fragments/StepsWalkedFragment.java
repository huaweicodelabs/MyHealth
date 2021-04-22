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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huawei.myhealth.R;
import com.huawei.myhealth.java.database.dbabstract.MyHealthDatabase;
import com.huawei.myhealth.databinding.FragmentStepsWalkedBinding;
import com.huawei.myhealth.java.database.entity.StepsWalked;
import com.huawei.myhealth.java.utils.DefaultValues;


/**
 * @since 2020
 * @author Huawei DTSE India
 */

public class StepsWalkedFragment extends Fragment {

    private int step_count;
    private FragmentStepsWalkedBinding binding;

    public MyHealthDatabase db;

    private SharedPreferences sharedPreference;
    private double calorie_val = 0.027;
    private int totalCal_burned;


    public StepsWalkedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStepsWalkedBinding.inflate(inflater, container, false);

        // To Show / Hide Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().show();
        }

        // initialize the shared preference
        sharedPreference = getActivity().getSharedPreferences(getResources().getString(R.string.APP), Context.MODE_PRIVATE);

        // onClick funtions
        onClick();

        return binding.getRoot();
    }

    private void onClick() {
        binding.etSteps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.etSteps.getText().toString().equals("")) {
                    //do nothing
                }
                else
                {
                    int step_data = step_count;
                    int step_entereddata = Integer.parseInt(binding.etSteps.getText().toString());
                    step_data = step_entereddata + step_data;
                    binding.tvWatertaken.setText(String.valueOf(step_data) + (getResources().getString(R.string.steps_value)));
                    binding.pbProgressWatertaken.setProgress(step_count + Integer.parseInt(binding.etSteps.getText().toString()));
                }
            }
        });


    }


    @Override
    public void onDestroyView () {
        super.onDestroyView();
        binding = null;
    }
}