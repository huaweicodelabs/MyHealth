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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.huawei.myhealth.R
import com.huawei.myhealth.adapter.FoodNutrientsListAdapter
import com.huawei.myhealth.kotlin.app.MyHealthApplication
import com.huawei.myhealth.database.entity.FoodNutrientsInsight
import com.huawei.myhealth.database.viewmodel.FoodNutrientsViewModel
import com.huawei.myhealth.databinding.FragmentFoodSearchBinding
import com.huawei.myhealth.utils.DefaultValues

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class FoodSearchFragment : Fragment() {
    private var _binding: FragmentFoodSearchBinding? = null
    private val binding get() = _binding!!

    private val foodNutrientsViewModel: FoodNutrientsViewModel by viewModels {
        FoodNutrientsViewModel.FoodNutrientsViewModelFactory((activity?.application as MyHealthApplication).foodNutrientsRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFoodSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        val foodNameListAdapter = FoodNutrientsListAdapter(this)
        binding.foodNameListRecyclerView.adapter = foodNameListAdapter
        binding.foodNameListRecyclerView.layoutManager = LinearLayoutManager(context)
       //get the data from db
        populateDatabase()

        foodNutrientsViewModel.allFoodList.observe(viewLifecycleOwner) { foodList ->
            foodList.let { foodNameListAdapter.submitList(it) }
        }

        return view
    }

    //Method to get the data
    private fun populateDatabase() {
        // Add sample words.
        val foodItem = FoodNutrientsInsight(
                DefaultValues.FOODID, resources.getString(R.string.foodname), resources.getString(R.string.foodportiontype), DefaultValues.FOODID, resources.getString(R.string.foodthumbnail), resources.getString(R.string.foodpic),
                DefaultValues.ENERGY_VALUE, resources.getString(R.string.foodprotein), resources.getString(R.string.foodfat), resources.getString(R.string.foodcarbohydrate), resources.getString(R.string.foodfibre), resources.getString(R.string.foodsugar),
                DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(),
                DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(),
                DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(),
                DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString(), DefaultValues.ENERGY_VALUE.toString()
        )
        //insert data
        foodNutrientsViewModel.insert(foodItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}