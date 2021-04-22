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

package com.huawei.myhealth.kotlin.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.myhealth.R
import com.huawei.myhealth.database.dbabstract.MyHealthDatabase
import com.huawei.myhealth.database.entity.ExerciseDone
import com.huawei.myhealth.databinding.FragmentCalBurnBinding
import com.huawei.myhealth.utils.CommonUtil
import com.huawei.myhealth.utils.DefaultValues
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [CalBurnFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalBurnFragment : Fragment() {
    private val TAG = "CalBurnFragment"

    private var _binding: FragmentCalBurnBinding? = null
    private val binding get() = _binding!!

    var workout_selected: String? = null
    var workoutId = 1 // default workoutid
    var weight = 60 // default average weight to calculate
    var cal_burned = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCalBurnBinding.inflate(inflater, container, false)
        val view = binding.root

        // To Show / Hide Action Bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        init()
        onClick()
        getDataFromDB()

        return view
    }
    //Initialization
    private fun init() {
        val list = arrayOf(
                resources.getString(R.string.walking),
                resources.getString(R.string.running),
                resources.getString(R.string.bicycling),
                resources.getString(R.string.swimming_freestyle)
        )

        binding.spItems.adapter = activity?.let {
            ArrayAdapter(
                    it,
                    R.layout.row_spinner, list
            )
        }
        binding.spItems?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                workout_selected =  resources.getString(R.string.walking)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (position) {
                    0 -> {
                        workout_selected =  resources.getString(R.string.walking)
                        workoutId = DefaultValues.WORKOUTID_WALKING
                    }

                    1 -> {
                        workout_selected = resources.getString(R.string.running)
                        workoutId = DefaultValues.WORKOUTID_RUNNING
                    }
                    2 -> {
                        workout_selected = resources.getString(R.string.bicycling)
                        workoutId = DefaultValues.WORKOUTID_BICYCLING
                    }
                    3 -> {
                        workout_selected =  resources.getString(R.string.swimming_freestyle)
                        workoutId = DefaultValues.WORKOUTID_SWIMMING
                    }

                    else -> {
                        workout_selected = resources.getString(R.string.walking)
                        workoutId = DefaultValues.WORKOUTID_WALKING
                    }
                }
                val bundle = Bundle()
                bundle.putString(resources.getString(R.string.workout), workout_selected)

                var instance: HiAnalyticsInstance? = null
                // Generate the Analytics Instance
                instance = HiAnalytics.getInstance(activity)

                instance.onEvent(resources.getString(R.string.workout), bundle)

                if ((binding.etDistance.text.toString() != "") && (binding.etTime.text.toString() != "")) {
                    calculateCalorieBurned(workout_selected, binding.etTime.text.toString())
                }
            }
        }
        binding.etDistance.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (!(s.equals(""))) {
                        calculateCalorieBurned(workout_selected, binding.etTime.text.toString())
                    }
                }
            }
        })

        binding.etTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                if (s != null) {
                    if (!(s.equals(""))) {
                        if (binding.etDistance.text.toString() != "") {
                            calculateCalorieBurned(workout_selected, s.toString())
                        }
                    }
                }
            }
        })
    }

    //method for action onclicks
    private fun onClick() {
        val sharedPreference = activity?.getSharedPreferences(resources.getString(R.string.APP), Context.MODE_PRIVATE)
        var uid = sharedPreference?.getString(resources.getString(R.string.UID), "")
        var dateselected = sharedPreference?.getString(resources.getString(R.string.date_selected), "")

        val sdf = SimpleDateFormat(resources.getString(R.string.date_pattern))
        var selectedDateStr = sdf.format(Date())

        // onclick done button
        binding.tvDone!!.setOnClickListener {

            var db = activity?.let { Room.databaseBuilder(it, MyHealthDatabase::class.java, resources.getString(R.string.my_health_database)).build() }

            if (binding.etTime.text.toString().equals("")) {
                if (binding.etCalEntered.text.toString().equals(""))
                {
                    Toast.makeText(activity, activity?.getString(R.string.enter_missing_fields), Toast.LENGTH_LONG).show();
                }
                else {
                    val excercideDone = ExerciseDone(uid.toString(), dateselected.toString(), workoutId, workout_selected.toString(), DefaultValues.DEFAULT_VALUE.toString(), DefaultValues.DEFAULT_VALUE.toString(), DefaultValues.DEFAULT_VALUE.toString(), binding.etCalEntered.text.toString(), resources.getString(R.string.manual), selectedDateStr.toString(), selectedDateStr.toString())
                    var newRecord = true

                    // insert to db
                    val thread = Thread {

                        // fetch Records
                        db?.exerciseDoneDao()?.getExerciseDoneData()?.forEach()
                        {

                            if (it.dateNTime.equals(dateselected)) {
                                newRecord = false
                            }
                        }

                        if (newRecord) {
                            // insert Records
                            db?.exerciseDoneDao()?.insert(excercideDone)
                        } else {
                            cal_burned = (cal_burned + (binding.etCalEntered.text.toString().toInt()))
                            db?.exerciseDoneDao()?.update((cal_burned.toString()), dateselected)
                        }
                    }
                    thread.start()
                    thread.interrupt()

                    findNavController().navigate(R.id.homeScreenDest)
                }
            }
            else if (binding.etDistance.text.toString().equals("")) {
                Toast.makeText(activity, activity?.getString(R.string.enter_missing_fields), Toast.LENGTH_LONG).show();
            }
            else {

                val excercideDone = ExerciseDone(uid.toString(), dateselected.toString(), workoutId, workout_selected.toString(), binding.etTime.text.toString(), DefaultValues.DEFAULT_VALUE.toString(), binding.etDistance.text.toString(), binding.tvCalBurnedText.text.toString(), resources.getString(R.string.calculation), selectedDateStr.toString(), selectedDateStr.toString())
                var newRecord = true
                // insert to db
                val thread = Thread {
                    // fetch Records
                    db?.exerciseDoneDao()?.getExerciseDoneData()?.forEach()
                    {

                        if (it.dateNTime.equals(dateselected)) {
                            newRecord = false
                        }
                    }
                    if (newRecord) {
                        // insert Records
                        db?.exerciseDoneDao()?.insert(excercideDone)
                    } else {
                        cal_burned = (cal_burned + (binding.tvCalBurnedText.text.toString().toInt()))
                        db?.exerciseDoneDao()?.update((cal_burned.toString()), dateselected)
                    }
                }
                thread.start()
                thread.interrupt()
                findNavController().navigate(R.id.homeScreenDest)
            }
        }
    }

    //method to get data from db
    private fun getDataFromDB() {
        var db = activity?.let { Room.databaseBuilder(it, MyHealthDatabase::class.java, resources.getString(R.string.my_health_database)).build() }
        val sharedPreference = activity?.getSharedPreferences(resources.getString(R.string.APP), Context.MODE_PRIVATE)
        var selectedDateStr = sharedPreference?.getString(resources.getString(R.string.date_selected), "")

        val thread = Thread {
            // fetch Records
            db?.userProfileDao()?.getUserData()?.forEach()
            {
                try {
                    weight = it.userWeightInKGInt.toInt()
                } catch (e: NumberFormatException) {
                    CommonUtil.logger(e.toString(),TAG)
                }
            }

            // fetch Records
            db?.exerciseDoneDao()?.getExerciseDoneData()?.forEach()
            {
                if (selectedDateStr.equals(it.dateNTime)) {
                    var calBurnstr = it.calorieBurned
                    cal_burned = calBurnstr.toInt()
                }
            }
        }
        thread.start()
        thread.interrupt()
    }
    /**
     * calculation for cal burn
     *
     * @param string worktype
     * @param  string time
     */
    private fun calculateCalorieBurned(workoutType: String?, time: String) {
        var met = 0.0
        when (workoutType) {
           resources.getString(R.string.walking) -> met = DefaultValues.MET_WALKING
            resources.getString(R.string.running)-> met = DefaultValues.MET_RUNNING
            resources.getString(R.string.bicycling)-> met = DefaultValues.MET_CYCLING
            resources.getString(R.string.swimming_freestyle) -> met = DefaultValues.MET_SWIMMING
            else -> { // Note the block
                met = DefaultValues.DEFAULT_DOUBLEVALUE
            }
        }

        var time_decimal = DefaultValues.DEFAULT_DOUBLEVALUE
        try {
            time_decimal = time.toDouble()
        } catch (e: java.lang.NumberFormatException) {
            CommonUtil.logger(e.toString(),TAG)
            time_decimal = DefaultValues.DEFAULT_DOUBLEVALUE
        }

        var cal_burned = (met * weight * DefaultValues.MET_CYCLING) / DefaultValues.VALUE_200
        cal_burned = (cal_burned * time_decimal * DefaultValues.VALUE_60) / DefaultValues.VALUE_10
        cal_burned = (cal_burned / DefaultValues.VALUE_3)

        val twoDForm = DecimalFormat(resources.getString(R.string.double_pattern))
        cal_burned = java.lang.Double.valueOf(twoDForm.format(cal_burned))

        var int_cal_burned = DefaultValues.DEFAULT_VALUE
        int_cal_burned = cal_burned.toInt()
        binding.tvCalBurned.setText(resources.getString(R.string.Cal_burn))
        binding.tvCalBurnedText.setText(int_cal_burned.toString())
    }}


