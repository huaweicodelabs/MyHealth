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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room.databaseBuilder
import com.huawei.myhealth.R
import com.huawei.myhealth.database.dbabstract.MyHealthDatabase
import com.huawei.myhealth.database.entity.UserProfile
import com.huawei.myhealth.databinding.FragmentCompleteProfileBinding
import java.text.SimpleDateFormat
import java.util.*
/**
 * @since 2020
 * @author Huawei DTSE India
 */

class CompleteProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentCompleteProfileBinding? = null
    private val binding get() = _binding!!
    var gender = ""
    var mobileNo = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCompleteProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        //To Show / Hide Action Bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        init()
        onClick()
        return view
    }

    //Initialization
    private fun init() {
        populateGenderSpinner()
        binding.chooseGenderSpinner.onItemSelectedListener = this
    }

    //populate the data
    private fun populateGenderSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                activity as AppCompatActivity,
                R.array.gender_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.chooseGenderSpinner.adapter = adapter
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // TODO("Not yet implemented")

        when (position) {
            1 -> gender = resources.getString(R.string.male)
            2 -> gender = resources.getString(R.string.female)
            3 -> gender = resources.getString(R.string.third_gender)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // TODO("Not yet implemented")
    }

    private fun onClick() {
        binding.saveCompleteProfileBtn!!.setOnClickListener {
            validFields()
        }
    }

    //Methos to validate fields
    private fun validFields() {
        if (binding.enterFullNameET.text.toString().equals("")) {
            showToast(resources.getString(R.string.name))
        } else if (binding.enterBioET.text.toString().equals("")) {
            showToast(resources.getString(R.string.bio))
        } else if (binding.enterAgeET.text.toString().equals("")) {
            showToast(resources.getString(R.string.age))
        }
        else if (binding.enterWeightET.text.toString().equals("")) {
            showToast(resources.getString(R.string.weight))
        } else if (gender.equals("")) {
            showToast(resources.getString(R.string.gender))
        } else {
            saveData()
        }
    }
  //Method to display toast
    private fun showToast(data: String) {
        Toast.makeText(activity, resources.getString(R.string.enterdata) + data, Toast.LENGTH_LONG).show()
    }

    //method to save the data
    private fun saveData() {
        var db = activity?.let { databaseBuilder(it, MyHealthDatabase::class.java, resources.getString(R.string.my_health_database)).build() }
        var uid: UUID = UUID.randomUUID()
        val sdf = SimpleDateFormat( resources.getString(R.string.date_pattern))
        val currentDate = sdf.format(Date())
        val sharedPreference = activity?.getSharedPreferences( resources.getString(R.string.APP), Context.MODE_PRIVATE)
        var mobileno = sharedPreference?.getString(resources.getString(R.string.mobilenum), "")
        var editor = sharedPreference?.edit()
        editor?.putString(resources.getString(R.string.UID), uid.toString())
        editor?.putString(resources.getString(R.string.name), binding.enterFullNameET.text.toString())
        editor?.putString(resources.getString(R.string.bio), binding.enterBioET.text.toString())
        editor?.putString(resources.getString(R.string.gender), gender.toString())
        editor?.commit()
        val userProfile = UserProfile(uid.toString(), binding.enterFullNameET.text.toString(), mobileno.toString(), "", "", binding.enterBioET.text.toString(), gender, binding.enterHeightET.text.toString(), binding.enterWeightET.text.toString(), currentDate, currentDate)
        val thread = Thread {
            // add Records
            db?.userProfileDao()?.insert(userProfile)
        }
        thread.start()
        thread.interrupt()
        //navigate to home screen
        findNavController().navigate(R.id.homeScreenDest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}