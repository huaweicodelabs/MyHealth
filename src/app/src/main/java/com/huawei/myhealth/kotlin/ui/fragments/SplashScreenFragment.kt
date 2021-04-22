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

package com.huawei.myhealth.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.huawei.myhealth.R
import com.huawei.myhealth.utils.DefaultValues

/**
 * @since 2020
 * @author Huawei DTSE India
 */

class SplashScreenFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_splash_screen, container, false)

        // To Hide Action Bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        // check for screen navigation
        checkForNavigation(view)

        return view
    }

    // check for screen navigation
    private fun checkForNavigation(view: View) {
        Handler(Looper.getMainLooper()).postDelayed({
            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }

            val sharedPreference = activity?.getSharedPreferences(resources.getString(R.string.APP), Context.MODE_PRIVATE)
            var mobileno = sharedPreference?.getString(resources.getString(R.string.mobilenum), "")
            var name = sharedPreference?.getString(resources.getString(R.string.name), "")

            if (mobileno.equals("")) {
                Navigation.findNavController(view).navigate(R.id.loginFragmentDest, null, options)
            } else if (name.equals("")) {
                findNavController().navigate(R.id.completeProfileFragmentDest)
            } else {
                findNavController().navigate(R.id.homeScreenDest)
            }
        }, DefaultValues.SPLASH_TIME_OUT)
    }

    override fun onDestroy() {
        super.onDestroy()

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}