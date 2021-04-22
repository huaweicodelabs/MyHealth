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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.myhealth.R;
import com.huawei.myhealth.database.dao.WaterIntakeDao;
import com.huawei.myhealth.java.database.entity.WaterIntake;
import com.huawei.myhealth.databinding.FragmentWaterIntakeBinding;
import com.huawei.myhealth.java.database.dbabstract.MyHealthDatabase;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

import kotlin.text.StringsKt;
/**
 * @since 2020
 * @author Huawei DTSE India
 */

public class WaterIntakeFragment extends Fragment {

    private SharedPreferences sharedPreference;
    private int count;
    private FragmentWaterIntakeBinding binding;
    com.huawei.myhealth.java.database.dbabstract.MyHealthDatabase db;


    public WaterIntakeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWaterIntakeBinding.inflate(inflater, container, false);
        // To Show / Hide Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().show();
        }

        //db initialization
        db = MyHealthDatabase.getDatabase(getActivity());

        // initialize the shared preference
        sharedPreference = getActivity().getSharedPreferences(getResources().getString(R.string.APP), Context.MODE_PRIVATE);

        // get data from db
        getDataFromDB();

        // onClick funtions
        onClick();

        return binding.getRoot();
    }

    private void onClick() {
        // onclik of add water
        binding.ivAddwater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add water intake count to database
                addWaterCount();
            }
        });

    }
   //Method to add watercount in database
    private final void addWaterCount() {
        ++count;

        HiAnalyticsInstance instance = (HiAnalyticsInstance) null;
        instance = HiAnalytics.getInstance((Activity) this.getActivity());
        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.watercount), String.valueOf(count));
        instance.onEvent(getResources().getString(R.string.watercount), bundle);
        binding.tvProgressWatertaken.setProgress(count);
        binding.tvWatertaken.setText((CharSequence) ("" + count + getResources().getString(R.string.watercount_total)));
        if (count >= 10) {
            binding.tvProgressWatertaken.setProgress(count);
            binding.tvWatertaken.setText((CharSequence) ("" + count + getResources().getString(R.string.watercount_total)));
            binding.tvStatus.setText((CharSequence) getResources().getString(R.string.completed));
        }

        String uid = sharedPreference.getString(getResources().getString(R.string.UID), "");
        String dateselected = sharedPreference.getString(getResources().getString(R.string.date_selected), "");
        WaterIntake waterIntake = new WaterIntake(String.valueOf(uid), String.valueOf((String) dateselected.toString()), count);
        Thread thread = new Thread((Runnable) (new Runnable() {
            public final void run() {

                if (count == 1) {
                    db.waterIntakeDao().insert(waterIntake);
                } else {
                    db.waterIntakeDao().update(String.valueOf(count), (String) dateselected.toString());
                }

            }
        }));
        thread.start();
        thread.interrupt();
    }
   //Method to getDataFrom database
    private final void getDataFromDB() {
        String selectedDateStr = sharedPreference.getString(getResources().getString(R.string.date_selected), "");
        Thread thread = new Thread((Runnable) (new Runnable() {
            public final void run() {

                // fetch Records
                //get water intake data from db
                List<WaterIntake> list_waterIntake = db.waterIntakeDao().getWaterIntakeData();
                for (int i = 0; i < list_waterIntake.size(); i++) {
                    if (selectedDateStr.toString().equals(list_waterIntake.get(i).getDate())) {
                        count = list_waterIntake.get(i).getNoOfGlassConsumed();

                        binding.tvProgressWatertaken.setProgress(count);
                        binding.tvWatertaken.setText("" + count + getResources().getString(R.string.watercount_total));
                        if (count >= 10) {
                            binding.tvProgressWatertaken.setProgress(count);
                            binding.tvWatertaken.setText("" + count + getResources().getString(R.string.watercount_total));
                            binding.tvStatus.setText(getResources().getString(R.string.completed));
                        }
                    }
                }
            }
        }));
        thread.start();
        thread.interrupt();
    }
}