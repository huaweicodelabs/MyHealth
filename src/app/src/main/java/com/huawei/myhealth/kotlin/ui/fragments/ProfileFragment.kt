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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.huawei.myhealth.R
import com.huawei.myhealth.databinding.FragmentProfileBinding
import com.huawei.myhealth.utils.CommonUtil
import java.lang.NullPointerException

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class ProfileFragment : Fragment() {
    private val TAG = "ProfileFragment"

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        // To Show / Hide Action Bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        getDataFromDB()
        onClick()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun validFields() {
        if (!(binding.userNameTV.text.toString().equals(""))) {
            if (!(binding.userBioTV.text.toString().equals(""))) {
                val sharedPreference = activity?.getSharedPreferences(resources.getString(R.string.APP), Context.MODE_PRIVATE)
                var editor = sharedPreference?.edit()
                editor?.putString(resources.getString(R.string.name), binding.userNameTV.text.toString())
                editor?.putString(resources.getString(R.string.bio), binding.userBioTV.text.toString())
                editor?.commit()

                findNavController().navigate(R.id.homeScreenDest)
            }
        }
    }

    private fun onClick() {
        binding.saveProfileBtn!!.setOnClickListener {
            validFields()
        }
    }

    // Fetch data from database and update UI
    private fun getDataFromDB() {
        val sharedPreference = activity?.getSharedPreferences(resources.getString(R.string.APP), Context.MODE_PRIVATE)
        var mobileno = sharedPreference?.getString(resources.getString(R.string.mobilenum), "")
        var name = sharedPreference?.getString(resources.getString(R.string.name), "")
        var bio = sharedPreference?.getString(resources.getString(R.string.bio), "")
        var gender = sharedPreference?.getString(resources.getString(R.string.gender), "")

        updateUI(name.toString(), bio.toString(), mobileno.toString(), gender.toString())
    }

    private fun updateUI(fullNameStr: String, userBioStr: String, mobileNoStr: String, userGenderStr: String) {

        try {
            binding.userNameTV.setText(fullNameStr)
            binding.userMobileTV.setText(mobileNoStr)
            binding.userBioTV.setText(userBioStr)
            binding.genderAndAgeTV.setText(userGenderStr)
        } catch (e: NullPointerException) {
            CommonUtil.logger(e.toString(),TAG)
        }
    }
}