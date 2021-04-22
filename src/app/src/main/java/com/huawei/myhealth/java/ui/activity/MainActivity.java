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
package com.huawei.myhealth.java.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;
import com.huawei.myhealth.R;
/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener{
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    BottomNavigationView bottomNav;
    HiAnalyticsInstance instance;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Enabling Analytics
        HiAnalyticsTools.enableLog();
        // Generate the Analytics Instance
        instance = HiAnalytics.getInstance(this);
        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
        navController.addOnDestinationChangedListener(this);
        //appBarConfiguration = new AppBarConfiguration(navController.getGraph());
        bottomNav = findViewById(R.id.bottom_nav_view);
        setupActionBar(navController);
        setupBottomNavMenu(bottomNav,navController);
        visibilityNavElements(navController);

    }

    private void setupActionBar(
                                NavController navController) {
        NavigationUI.setupActionBarWithNavController(this, navController);
      
    }

    private void setupBottomNavMenu(BottomNavigationView bottomNav, NavController navController) {
        NavigationUI.setupWithNavController(bottomNav,navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController((Activity) getApplicationContext(),R.id.my_nav_host_fragment).navigateUp();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

    }

    private void visibilityNavElements(NavController navController)
    {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
              switch (destination.getId())
              {
                  case R.id.splashScreenDest:
                      bottomNav.setVisibility(View.GONE);
                      break;
                  case R.id.loginFragmentDest:
                      bottomNav.setVisibility(View.GONE);
                      break;
                  case R.id.completeProfileFragmentDest:
                      bottomNav.setVisibility(View.GONE);
                      break;
                  default:
                      bottomNav.setVisibility(View.VISIBLE);
                      break;
              }
            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        navController.removeOnDestinationChangedListener(this);
    }
}