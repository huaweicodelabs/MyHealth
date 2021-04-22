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

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.huawei.myhealth.R
import com.huawei.myhealth.kotlin.app.MyHealthApplication
import com.huawei.myhealth.database.viewmodel.FoodNutrientsViewModel
import com.huawei.myhealth.databinding.FragmentInsightsBinding
import com.huawei.myhealth.utils.DefaultValues
import java.text.SimpleDateFormat
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class InsightsFragment : Fragment() {
    private var _binding: FragmentInsightsBinding? = null
    private val binding get() = _binding!!
    var selectedDateStr: String? = null
    private val calorieTakenTodayList: MutableList<String>? = ArrayList()

    private val foodNutrientsViewModel: FoodNutrientsViewModel by viewModels {
        FoodNutrientsViewModel.FoodNutrientsViewModelFactory((activity?.application as MyHealthApplication).foodNutrientsRepository)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInsightsBinding.inflate(inflater, container, false)
        val view = binding.root
        // To Show / Hide Action Bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        // initialize
        init(view)
        // Receiving Data Via Observer
        foodNutrientsViewModel.allFoodList.observe(viewLifecycleOwner) { foodNutrients ->
            foodNutrients.let {
                for (calorieInEachFood in foodNutrients) {
                    calorieTakenTodayList?.add(calorieInEachFood.protein)
                    calorieTakenTodayList?.add(calorieInEachFood.totalLipidFat)
                    calorieTakenTodayList?.add(calorieInEachFood.carbohydrate)
                    calorieTakenTodayList?.add(calorieInEachFood.fiberTotalDietary)
                }
               //update the UI
                UpdateUI()

            }
        }

        return view
    }

    private fun UpdateUI() {
        if ((calorieTakenTodayList != null) && (calorieTakenTodayList.size > 0)) {
            binding.tvProteinQ.setText("" + calorieTakenTodayList.get(0) + resources.getString(R.string.g))
            binding.tvFatQ.setText("" + calorieTakenTodayList.get(1) + resources.getString(R.string.g))
            binding.tvCarbsQ.setText("" + calorieTakenTodayList.get(2) + resources.getString(R.string.g))
            binding.tvFiberQ.setText("" + calorieTakenTodayList.get(3) + resources.getString(R.string.g))
            // set percentage
            var protein_percentage = 0
            protein_percentage = (((calorieTakenTodayList.get(0).toDouble()) * DefaultValues.VALUE_10) / DefaultValues.VALUE_2).toInt()
            binding.tvProteinPercentage.setText(protein_percentage.toString() + resources.getString(R.string.percentage))
            binding.pbProtein.setProgress(protein_percentage)

            var fat_percentage = 0
            fat_percentage = (((calorieTakenTodayList.get(1).toDouble()) * DefaultValues.VALUE_16) / DefaultValues.VALUE_2).toInt()
            binding.tvFatPercentage.setText(fat_percentage.toString() + resources.getString(R.string.percentage))
            binding.pbFat.setProgress(fat_percentage)

            var carbs_percentage = 0
            carbs_percentage = (((calorieTakenTodayList.get(2).toDouble()) * DefaultValues.VALUE_7) / DefaultValues.VALUE_2).toInt()
            binding.tvCarbsPercentage.setText(carbs_percentage.toString() + resources.getString(R.string.percentage))
            binding.pbCarbs.setProgress(carbs_percentage)

            var fiber_percentage = 0
            fiber_percentage = (((calorieTakenTodayList.get(3).toDouble()) * DefaultValues.VALUE_69) / DefaultValues.VALUE_2).toInt()
            binding.tvFiberPercentage.setText(fiber_percentage.toString() + resources.getString(R.string.percentage))
            binding.pbFiber.setProgress(fiber_percentage)
        } else {
            binding.tvProteinQ.setText(resources.getString(R.string.zerog))
            binding.tvFatQ.setText(resources.getString(R.string.zerog))
            binding.tvCarbsQ.setText(resources.getString(R.string.zerog))
            binding.tvFiberQ.setText(resources.getString(R.string.zerog))

            binding.tvProteinPercentage.setText(resources.getString(R.string.zeropercentage))
            binding.pbProtein.setProgress(DefaultValues.DEFAULT_VALUE)

            binding.tvFatPercentage.setText(resources.getString(R.string.zeropercentage))
            binding.pbFat.setProgress(DefaultValues.DEFAULT_VALUE)

            binding.tvCarbsPercentage.setText(resources.getString(R.string.zeropercentage))
            binding.pbCarbs.setProgress(DefaultValues.DEFAULT_VALUE)

            binding.tvFiberPercentage.setText(resources.getString(R.string.zeropercentage))
            binding.pbFiber.setProgress(DefaultValues.DEFAULT_VALUE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init(view: View) {
        //set current date to calendar
        val simpleDateFormat = SimpleDateFormat(resources.getString(R.string.date_pattern))
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        binding.tvDate.setText(currentDateAndTime)


        binding.rlCalendar!!.setOnClickListener {
            val cal = Calendar.getInstance()
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)
            //action for datepicker
            val datepickerdialog:DatePickerDialog = activity?.let { it1 ->
                DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = resources.getString(R.string.date_pattern) // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    // Display Selected date in textbox
                    binding.tvDate.setText(""+sdf.format(cal.time))
                    selectedDateStr = ""+sdf.format(cal.time)
                    var selectedDateStr1 = sdf.format(Date())
                    if (selectedDateStr1.equals(selectedDateStr)) {
                        // Receiving Data Via Observer
                        foodNutrientsViewModel.allFoodList.observe(viewLifecycleOwner) { foodNutrients ->
                            foodNutrients.let {
                                for (calorieInEachFood in foodNutrients) {
                                    calorieTakenTodayList?.add(calorieInEachFood.protein)
                                    calorieTakenTodayList?.add(calorieInEachFood.totalLipidFat)
                                    calorieTakenTodayList?.add(calorieInEachFood.carbohydrate)
                                    calorieTakenTodayList?.add(calorieInEachFood.fiberTotalDietary)
                                }
                                //update the UI
                                UpdateUI()
                            }
                        }
                    } else {
                        calorieTakenTodayList?.clear()
                        //update the UI
                        UpdateUI()
                    }

                }, y, m, d)
            }!!

            datepickerdialog.show()
        }
    }
}