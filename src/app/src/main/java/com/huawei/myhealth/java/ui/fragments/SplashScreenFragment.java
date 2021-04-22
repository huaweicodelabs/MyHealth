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
import androidx.navigation.NavActionBuilder;
import androidx.navigation.NavOptionsBuilder;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huawei.myhealth.R;
import com.huawei.myhealth.java.utils.DefaultValues;
/**
 * @since 2020
 * @author Huawei DTSE India
 */

public class SplashScreenFragment extends Fragment {
    

    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        // To Show / Hide Action Bar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().hide();
        }

        // check for screen navigation
        checkForNavigation(view);
        
        return view;
                
    }

    //Method to check for screen navigation
    private void checkForNavigation(View view) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreference = getActivity().getSharedPreferences(getResources().getString(R.string.APP), Context.MODE_PRIVATE);
                String mobileno = sharedPreference.getString(getResources().getString(R.string.mobilenum), "");
                String name = sharedPreference.getString(getResources().getString(R.string.name), "");

                if (mobileno.equals("")) {
                    Navigation.findNavController(view).navigate(R.id.loginFragmentDest, null);
                } else if (name.equals("")) {
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.completeProfileFragmentDest);
                } else {
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.homeScreenDest);
                }
            }
        }, DefaultValues.SPLASH_TIME_OUT);
    }


}