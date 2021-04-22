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
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.huawei.myhealth.R
import com.huawei.myhealth.database.dbabstract.MyHealthDatabase
import com.huawei.myhealth.database.entity.ExerciseDone
import com.huawei.myhealth.database.entity.StepsWalked
import com.huawei.myhealth.databinding.FragmentStepsWalkedBinding
import com.huawei.myhealth.utils.DefaultValues

/**
 * @since 2020
 * @author Huawei DTSE India
 */

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StepsWalkedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StepsWalkedFragment : Fragment() {
    var step_count = 0
   private var _binding: FragmentStepsWalkedBinding? = null
    private val binding get() = _binding!!

    var db: MyHealthDatabase? = null
    var sharedPreference: SharedPreferences? = null
    var calorie_val = 0.027 // cal data for 1 step count
    var totalCal_burned = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStepsWalkedBinding.inflate(inflater, container, false)
        val view = binding.root

        // To Show / Hide Action Bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        // initialize the room db
        db = activity?.let { Room.databaseBuilder(it, MyHealthDatabase::class.java, resources.getString(R.string.my_health_database)).build() }
        // initialize the shared preference
        sharedPreference = activity?.getSharedPreferences(resources.getString(R.string.APP), Context.MODE_PRIVATE)

        // get data from db
        getStepDataFromDB()

        // onClick funtions
        onClick()

        return view
    }

    private fun onClick() {
        binding.etSteps.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                if (binding.etSteps.text.toString() != "") {
                    var step_data = step_count.toString().toInt()
                    var step_entereddata = binding.etSteps.text.toString().toInt()
                    step_data = step_entereddata + step_data
                    binding.tvWatertaken.setText(step_data.toString() + resources.getString(R.string.steps_value))
                    binding.pbProgressWatertaken.setProgress(step_count + binding.etSteps.text.toString().toInt())
                }
            }
        })

        binding.tvDone!!.setOnClickListener {

            if (binding.etSteps.text.toString() != "") {
                val sharedPreference = activity?.getSharedPreferences(resources.getString(R.string.APP), Context.MODE_PRIVATE)
                var uid = sharedPreference?.getString(resources.getString(R.string.UID), "")
                var dateselected = sharedPreference?.getString(resources.getString(R.string.date_selected), "")

                val stepsWalked = StepsWalked(uid.toString(), dateselected.toString(), binding.etSteps.text.toString())
                var db = activity?.let { Room.databaseBuilder(it, MyHealthDatabase::class.java, resources.getString(R.string.my_health_database)).build() }
                var newRecord = true
                var newRecord_calorie = true

                var calCount = 0.0
                var calCountToInt = 0
                calCount = ((calorie_val) * (binding.etSteps.text.toString().toDouble()))
                calCountToInt = calCount.toInt()
                calCountToInt = totalCal_burned + calCountToInt;
                var step_entereddata = binding.etSteps.text.toString().toInt()

                // insert step data to db
                val thread = Thread {

                    // fetch Records
                    db?.stepsWalkedDao()?.getStepsWalkedData()?.forEach()
                    {
                        if (it.date.equals(dateselected)) {
                            newRecord = false
                        }
                    }
                    if (newRecord) {
                        // insert Records
                        db?.stepsWalkedDao()?.insert(stepsWalked)
                    } else {
                        var step_data = step_count.toString().toInt()
                        step_data = step_entereddata + step_data
                        db?.stepsWalkedDao()?.update((step_data.toString()), dateselected)
                    }

                    // insert calorie data to db
                    // fetch Records
                    db?.exerciseDoneDao()?.getExerciseDoneData()?.forEach()
                    {

                        if (it.dateNTime.equals(dateselected)) {
                            newRecord_calorie = false
                        }
                    }

                    if (newRecord_calorie) {
                        val excercideDone = ExerciseDone(uid.toString(), dateselected.toString(), DefaultValues.EXCERCISEID, resources.getString(R.string.STEPS_DEFAULT), DefaultValues.DEFAULT_VALUE.toString(), DefaultValues.DEFAULT_VALUE.toString(), DefaultValues.DEFAULT_VALUE.toString(), calCountToInt.toString(), resources.getString(R.string.steps_walked), dateselected.toString(), dateselected.toString())
                        // insert Records
                        db?.exerciseDoneDao()?.insert(excercideDone)
                    } else {
                        db?.exerciseDoneDao()?.update((calCountToInt.toString()), dateselected)
                    }
                }
                thread.start()
                thread.interrupt()
                findNavController().navigate(R.id.homeScreenDest)
            } else {
                Toast.makeText(activity, activity?.getString(R.string.enter_missing_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun getStepDataFromDB() {
        var selectedDateStr = sharedPreference?.getString(resources.getString(R.string.date_selected), "")
        // getData from db
        val thread = Thread {

            // fetch Records
            db?.stepsWalkedDao()?.getStepsWalkedData()?.forEach()
            {
                if (selectedDateStr.equals(it.date)) {
                    step_count = it.stepsWalked.toInt()
                }

                binding.pbProgressWatertaken.setProgress(step_count)
                binding.tvWatertaken.setText((step_count).toString() + resources.getString(R.string.steps_value))
            }
        }
        thread.start()
        thread.interrupt()
        // get cal data
        getCalDataFromDB()
    }

    private fun getCalDataFromDB() {
        var db = activity?.let { Room.databaseBuilder(it, MyHealthDatabase::class.java, resources.getString(R.string.my_health_database)).build() }
        var sharedPreference = activity?.getSharedPreferences(resources.getString(R.string.APP), Context.MODE_PRIVATE)
        var selectedDateStr = sharedPreference?.getString(resources.getString(R.string.date_selected), "")

        val thread = Thread {
            // fetch Records
            db?.exerciseDoneDao()?.getExerciseDoneData()?.forEach()
            {
                if (selectedDateStr.equals(it.dateNTime)) {
                    var calBurnstr = it.calorieBurned
                    totalCal_burned = calBurnstr.toInt()
                }
            }
        }
        thread.start()
        thread.interrupt()
    }
}