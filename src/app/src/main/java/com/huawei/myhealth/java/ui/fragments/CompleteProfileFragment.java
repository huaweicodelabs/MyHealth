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
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.huawei.myhealth.R;
import com.huawei.myhealth.databinding.FragmentCompleteProfileBinding;
import com.huawei.myhealth.java.database.dbabstract.MyHealthDatabase;
import com.huawei.myhealth.java.database.entity.UserProfile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
/**
 * @since 2020
 * @author Huawei DTSE India
 */

public class CompleteProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    FragmentCompleteProfileBinding binding;
    String gender = "";
    String mobileNo = "";
    com.huawei.myhealth.java.database.dbabstract.MyHealthDatabase db;

    public CompleteProfileFragment() {
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
        binding = FragmentCompleteProfileBinding.inflate(inflater, container, false);

        // To Show / Hide Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().show();
        }

        //initialize
        init();

        //action performed
        onClick();

        return binding.getRoot();

    }

    //Initialization
    private void init() {
        gender = getResources().getString(R.string.male);
        //db initialization
        db = MyHealthDatabase.getDatabase(getActivity());

        populateGenderSpinner();
    }

    //populate the data
    private void populateGenderSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter arrayAdapter =  ArrayAdapter.createFromResource(getActivity(), R.array.gender_array,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            binding.chooseGenderSpinner.setAdapter(arrayAdapter);
        }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
          switch (position)
          {
              case 1:
                  gender = getResources().getString(R.string.male);
                  break;
              case 2:
                  gender = getResources().getString(R.string.female);
                  break;
              case 3:
                  gender = getResources().getString(R.string.third_gender);
                  break;
              default:
                  gender = getResources().getString(R.string.male);
                  break;
          }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void onClick() {
        binding.saveCompleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validFields();
            }
        });
    }

    //Methos to validate fields
    private void validFields() {
        if (binding.enterFullNameET.getText().toString().equals("")) {
            showToast(getResources().getString(R.string.name));
        } else if (binding.enterBioET.getText().toString().equals("")) {
            showToast(getResources().getString(R.string.bio));
        } else if (binding.enterAgeET.getText().toString().equals("")) {
            showToast(getResources().getString(R.string.age));
        }
        else if (binding.enterWeightET.getText().toString().equals("")) {
            showToast(getResources().getString(R.string.weight));
        }  else {
            saveData();
        }
    }

    //Method to display toast
    private void showToast(String data) {
        Toast.makeText(getActivity(),  getResources().getString(R.string.enterdata) +" "+ data, Toast.LENGTH_LONG).show();
    }

    //method to save the data
    private void saveData() {
        UUID uid = UUID.randomUUID();
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_pattern));
        String currentDate = sdf.format(new Date());
        SharedPreferences sharedPreference = getActivity().getSharedPreferences( getResources().getString(R.string.APP), Context.MODE_PRIVATE);
        String mobileno = sharedPreference.getString(getResources().getString(R.string.mobilenum), "");
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(getResources().getString(R.string.UID), uid.toString());
        editor.putString(getResources().getString(R.string.name), binding.enterFullNameET.getText().toString());
        editor.putString(getResources().getString(R.string.bio), binding.enterBioET.getText().toString());
        editor.putString(getResources().getString(R.string.gender), gender.toString());
        editor.commit();
        UserProfile userProfile = new UserProfile(uid.toString(), binding.enterFullNameET.getText().toString(), mobileno.toString(), "", "", binding.enterBioET.getText().toString(), gender, binding.enterHeightET.getText().toString(), binding.enterWeightET.getText().toString(), currentDate, currentDate);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // add Records
                db.userProfileDao().insert(userProfile);
            }
        });
        thread.start();
        thread.interrupt();
        //navigate to homescreen
        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.homeScreenDest);
    }
}