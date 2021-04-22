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

package com.huawei.myhealth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huawei.myhealth.R
import com.huawei.myhealth.database.entity.FoodNutrientsInsight
import com.huawei.myhealth.ui.fragments.FoodSearchFragmentDirections


/**
 * @since 2020
 * @author Huawei DTSE India
 */


 class FoodNutrientsListAdapter(private val parentFragment: Fragment) : ListAdapter<FoodNutrientsInsight, FoodNutrientsListAdapter.FoodNutrientsViewHolder>(FoodNutrientsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodNutrientsViewHolder {
        return FoodNutrientsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FoodNutrientsViewHolder, position: Int) {
        val current = getItem(position)
        //foodname will update
        holder.bind(current.foodName)
        //action on foodName
        holder.foodNameItemView.setOnClickListener(View.OnClickListener {
            val foodIdGot = current.foodId
            val calorieInFoodGot = current.energyKCal
           // val action = FoodSearchFragmentDirections.actionFoodSearchFragmentToViewAndAddFoodFragment(foodIdGot, calorieInFoodGot)
            val action = FoodSearchFragmentDirections.actionFoodSearchFragmentToViewAndAddFoodFragment()
            action.setFoodIdArg(foodIdGot)
            action.setCalorieInFoodArg(calorieInFoodGot)
            parentFragment.findNavController().navigate(action)
        })
    }

    class FoodNutrientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val foodNameItemView: TextView = itemView.findViewById(R.id.foodNameItemTV)

        fun bind(text: String?) {
            foodNameItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): FoodNutrientsViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_food_name_item, parent, false)
                return FoodNutrientsViewHolder(view)
            }
        }
    }

    class FoodNutrientsComparator : DiffUtil.ItemCallback<FoodNutrientsInsight>() {
        override fun areItemsTheSame(oldItem: FoodNutrientsInsight, newItem: FoodNutrientsInsight): Boolean {
            // return oldItem === newItem
            return false
        }

        override fun areContentsTheSame(oldItem: FoodNutrientsInsight, newItem: FoodNutrientsInsight): Boolean {
            // return oldItem.foodName   == newItem.foodName
            return false
        }
    }
}