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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.myhealth.R;
import com.huawei.myhealth.databinding.FragmentCalBurnBinding;
import com.huawei.myhealth.java.database.dbabstract.MyHealthDatabase;
import com.huawei.myhealth.java.database.entity.ExerciseDone;
import com.huawei.myhealth.java.database.entity.UserProfile;
import com.huawei.myhealth.java.utils.CommonUtil;
import com.huawei.myhealth.java.utils.DefaultValues;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class CalBurnFragment extends Fragment {
    private final String TAG = "CalBurnFragment";
    private FragmentCalBurnBinding binding;
    private String workout_selected;
    private int workoutId = DefaultValues.EXERCISE_ID;
    private int weight = DefaultValues.VALUE_60;
    private int cal_burned;
    MyHealthDatabase db;
    boolean newRecord = true;


    public CalBurnFragment() {
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
        binding = FragmentCalBurnBinding.inflate(inflater, container, false);

        // To Show / Hide Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().show();
        }

        // initialize
        init();

        // get data from db
        getDataFromDB();

        // onClick funtions
        onClick();

        return binding.getRoot();


    }
    //Initialization
    private final void init() {
        //db initialization
        db = MyHealthDatabase.getDatabase(getActivity());

        String[] list = new String[]{getResources().getString(R.string.walking), getResources().getString(R.string.running), getResources().getString(R.string.bicycling),
                getResources().getString(R.string.swimming_freestyle)};

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.row_spinner, list);

        binding.spItems.setAdapter(arrayAdapter);
        binding.spItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        workout_selected = getResources().getString(R.string.walking);
                        workoutId = DefaultValues.WORKOUTID_WALKING;
                        break;
                    case 1:
                        workout_selected = getResources().getString(R.string.running);
                        workoutId = DefaultValues.WORKOUTID_RUNNING;
                        break;
                    case 2:
                        workout_selected = getResources().getString(R.string.bicycling);
                        workoutId = DefaultValues.WORKOUTID_BICYCLING;
                        break;
                    case 3:
                        workout_selected = getResources().getString(R.string.swimming_freestyle);
                        workoutId = DefaultValues.WORKOUTID_SWIMMING;
                        break;
                    default:
                        workout_selected = getResources().getString(R.string.walking);
                        workoutId = DefaultValues.WORKOUTID_WALKING;
                }

                Bundle bundle = new Bundle();
                bundle.putString(getResources().getString(R.string.workout), workout_selected);
                HiAnalyticsInstance hiAnalyticsInstance = new HiAnalyticsInstance();
                hiAnalyticsInstance.onEvent(getResources().getString(R.string.workout), bundle);

                if ((binding.etDistance.getText().toString().equals("")) && (binding.etTime.getText().toString().equals(""))) {
                    //do nothing
                }
                else
                {
                    calculateCalorieBurned(workout_selected, binding.etTime.getText().toString());
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.etDistance.addTextChangedListener((TextWatcher) (new TextWatcher() {
            public void beforeTextChanged(@Nullable CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(@Nullable CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(@Nullable Editable s) {
                if (s != null && !s.equals("")) {
                    if (!(s.equals(""))) {
                        calculateCalorieBurned(workout_selected, binding.etTime.getText().toString());
                    }
                }

            }
        }));

        binding.etTime.addTextChangedListener((TextWatcher) (new TextWatcher() {
            public void beforeTextChanged(@Nullable CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(@Nullable CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(@Nullable Editable s) {
                if (s != null && !s.equals("")) {
                    if (!(s.equals(""))) {
                        if (binding.etDistance.getText().toString().equals("")) {
                            //do nothing
                        }
                        else
                        {
                            calculateCalorieBurned(workout_selected, s.toString());
                        }
                    }
                }

            }
        }
        ));
    }
    //Action for onClick
    private final void onClick() {

        SharedPreferences sharedPreference = getActivity().getSharedPreferences(getResources().getString(R.string.APP), Context.MODE_PRIVATE);
        String uid = sharedPreference.getString(getResources().getString(R.string.UID), "");
        String dateselected = sharedPreference.getString(getResources().getString(R.string.date_selected), "");

        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_pattern));
        String selectedDateStr = sdf.format(new Date());


        binding.tvDone.setOnClickListener((View.OnClickListener) (new View.OnClickListener() {
            public final void onClick(View view) {


                if (binding.etTime.getText().toString().equals("")) {
                    if (binding.etCalEntered.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), getString(R.string.enter_missing_fields), Toast.LENGTH_SHORT).show();
                    } else {

                        ExerciseDone excercideDone = new ExerciseDone(uid.toString(), dateselected.toString(), workoutId, workout_selected.toString(), String.valueOf(DefaultValues.DEFAULT_VALUE), String.valueOf(DefaultValues.DEFAULT_VALUE), String.valueOf(DefaultValues.DEFAULT_VALUE), binding.etCalEntered.getText().toString(), getResources().getString(R.string.manual), selectedDateStr.toString(), selectedDateStr.toString());
                        Thread thread = new Thread((Runnable) (new Runnable() {
                            public final void run() {
                                List<ExerciseDone> list_calBurn = db.excerciseDoneDao().getExerciseDoneData();
                                for(int i=0; i<list_calBurn.size(); i++) {
                                    if(list_calBurn.get(i).getDateNTime().equals(selectedDateStr))
                                    {
                                        newRecord = false;
                                    }
                                }

                                if (newRecord) {
                                    db.excerciseDoneDao().insert(excercideDone);

                                } else {
                                    cal_burned = (cal_burned + Integer.parseInt(binding.etCalEntered.getText().toString()));
                                    db.excerciseDoneDao().update(String.valueOf(cal_burned), dateselected);
                                }

                            }
                        }));
                        thread.start();
                        thread.interrupt();
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_homeScreenDest_to_timePeriodDialog);

                    }
                } else if (binding.etDistance.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.enter_missing_fields), Toast.LENGTH_LONG).show();
                } else {

                    ExerciseDone excercideDone = new ExerciseDone(uid.toString(), dateselected.toString(), workoutId, workout_selected.toString(), String.valueOf(DefaultValues.DEFAULT_VALUE), String.valueOf(DefaultValues.DEFAULT_VALUE), String.valueOf(DefaultValues.DEFAULT_VALUE), binding.tvCalBurnedText.getText().toString(), getResources().getString(R.string.manual), selectedDateStr.toString(), selectedDateStr.toString());
                    Thread thread = new Thread((Runnable) (new Runnable() {
                        public final void run() {
                            List<ExerciseDone> list_calBurn = db.excerciseDoneDao().getExerciseDoneData();
                            for(int i=0; i<list_calBurn.size(); i++) {
                                if(list_calBurn.get(i).getDateNTime().equals(selectedDateStr))
                                {
                                    newRecord = false;
                                }
                            }

                            if (newRecord) {
                                db.excerciseDoneDao().insert(excercideDone);

                            } else {
                                cal_burned = (cal_burned + Integer.parseInt(binding.tvCalBurnedText.getText().toString()));
                                db.excerciseDoneDao().update(String.valueOf(cal_burned), dateselected);
                            }

                        }
                    }));
                    thread.start();
                    thread.interrupt();
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.homeScreenDest);
                }

            }
        }));
    }
    //Method to get Database data
    private final void getDataFromDB() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.APP), Context.MODE_PRIVATE);
        String selectedDateStr = sharedPreferences.getString(getResources().getString(R.string.date_selected), "");

        Thread thread = new Thread((Runnable) (new Runnable() {
            public final void run() {
                SharedPreferences sharedPreference = getActivity().getSharedPreferences(getResources().getString(R.string.APP), Context.MODE_PRIVATE);
                String dateselected = sharedPreference.getString(getResources().getString(R.string.date_selected), "");
                // fetch Records
                List<UserProfile> list_userprofile = db.userProfileDao().getUserData();
                for(int i=0; i<list_userprofile.size(); i++) {
                    try {
                        weight = Integer.parseInt(list_userprofile.get(i).getUserWeightInKGInt());
                    } catch (NumberFormatException e) {
                        CommonUtil.logger(e.toString(),TAG);
                    }
                }

                //get cal burn data from db
                List<ExerciseDone> list_calBurn = db.excerciseDoneDao().getExerciseDoneData();
                for(int j=0; j<list_calBurn.size(); j++) {
                    if(list_calBurn.get(j).getDateNTime().equals(selectedDateStr))
                    {    String calBurnstr = list_calBurn.get(j).getCalorieBurned();
                        if(!calBurnstr.equals("")){
                            cal_burned =Integer.parseInt(calBurnstr);
                        }
                    }
                }
            }


        }));
        thread.start();
        thread.interrupt();
    }
    /**
     * calculation for cal burn
     *
     * @param workoutType String
     * @param  time String
     */
    private void calculateCalorieBurned(String workoutType, String time) {
        double met = 0.0;

        switch (workoutType) {
            case "Walking":
                met = DefaultValues.MET_WALKING;
                break;
            case "Running":
                met = DefaultValues.MET_RUNNING;
                break;
            case "Bicycling":
                met = DefaultValues.MET_CYCLING;
                break;
            case "Swimming_freestyle":
                met = DefaultValues.MET_SWIMMING;
                break;
            default:
                met = DefaultValues.DEFAULT_DOUBLEVALUE;

        }

        double time_decimal = DefaultValues.DEFAULT_DOUBLEVALUE;
        try {
            time_decimal = Double.parseDouble(time);
        } catch (NumberFormatException exception) {
            CommonUtil.logger(exception.toString(), TAG);
            time_decimal = DefaultValues.DEFAULT_DOUBLEVALUE;
        }
        double cal_burned = (met * weight * DefaultValues.MET_CYCLING) / DefaultValues.VALUE_200;
        cal_burned = (cal_burned * time_decimal * DefaultValues.VALUE_60) / DefaultValues.VALUE_10;
        cal_burned = (cal_burned / DefaultValues.VALUE_3);

        DecimalFormat twoDForm = new DecimalFormat(getResources().getString(R.string.double_pattern));
        cal_burned = java.lang.Double.valueOf(twoDForm.format(cal_burned));
        int int_cal_burned = DefaultValues.DEFAULT_VALUE;
        int_cal_burned = (int) cal_burned;
        binding.tvCalBurned.setText(getResources().getString(R.string.Cal_burn));
        binding.tvCalBurnedText.setText(String.valueOf(int_cal_burned));
    }

}