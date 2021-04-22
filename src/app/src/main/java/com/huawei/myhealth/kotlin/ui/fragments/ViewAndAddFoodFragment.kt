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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.huawei.myhealth.R
import com.huawei.myhealth.kotlin.app.MyHealthApplication
import com.huawei.myhealth.database.entity.FoodIntake
import com.huawei.myhealth.database.viewmodel.FoodIntakeViewModel
import com.huawei.myhealth.database.viewmodel.FoodNutrientsViewModel
import com.huawei.myhealth.databinding.FragmentViewAddFoodBinding
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */

/*
Using this fragment we can show the particular food selected through food search add it in the database by user
 */
class ViewAndAddFoodFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentViewAddFoodBinding? = null
    private val binding get() = _binding!!
    private var foodIdGot: Int = 0
    private var calorieInFoodGot: Int = 0
    private var portionTaken: String = "0"
    private var portionTakenListPosition: Int = 0
    private var portionType: String? = null

    private val args: ViewAndAddFoodFragmentArgs by navArgs()

    private val foodNutrientsViewModel: FoodNutrientsViewModel by viewModels {
        FoodNutrientsViewModel.FoodNutrientsViewModelFactory((activity?.application as MyHealthApplication).foodNutrientsRepository)
    }

    private val foodIntakeViewModel: FoodIntakeViewModel by viewModels {
        FoodIntakeViewModel.FoodIntakeViewModelFactory((activity?.application as MyHealthApplication).foodIntakeRepository)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentViewAddFoodBinding.inflate(inflater, container, false)
        val view = binding.root

        // To Show / Hide Action Bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        // Food Id Received From Previous Fragment
        foodIdGot = args.foodIdArg
        calorieInFoodGot = args.calorieInFoodArg

        populateNoOfPortion()
        populatePortionType()

        binding.addCalorieCountBtn.setOnClickListener(this)

        binding.noOfPortionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                portionTakenListPosition = position
                if (position != 0) {
                    portionTaken = parent?.getItemAtPosition(position).toString()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.portionTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) {
                    portionType = parent?.getItemAtPosition(position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return view
    }

    private fun populateNoOfPortion() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                activity as AppCompatActivity,
                R.array.no_of_portion,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.noOfPortionSpinner.adapter = adapter
        }
    }
    private fun populatePortionType() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                activity as AppCompatActivity,
                R.array.portion_type_for_fruits,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.portionTypeSpinner.adapter = adapter
        }
    }

    private fun saveFoodTakenData(foodIdGot: Int, calorieInFood: Int) {
        val calorieTaken = FoodIntake(0, resources.getString(R.string.admin), resources.getString(R.string.morning), foodIdGot, resources.getString(R.string.foodname), calorieInFood, portionTaken, Date(), Date())
        foodIntakeViewModel.insert(calorieTaken)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        val id = v?.id

        when (id) {
            R.id.addCalorieCountBtn -> {
                if (portionTakenListPosition != 0) {
                    saveFoodTakenData(foodIdGot, calorieInFoodGot)
                    Snackbar.make(binding.viewAddFoodParentLay, resources.getString(R.string.intake_success), Snackbar.LENGTH_LONG).show()
                    findNavController().navigateUp()
                } else {
                    Snackbar.make(binding.viewAddFoodParentLay, resources.getString(R.string.select_portion), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}