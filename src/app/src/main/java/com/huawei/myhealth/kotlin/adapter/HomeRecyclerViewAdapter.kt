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

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.huawei.myhealth.R
import com.huawei.myhealth.model.HomeDataModel
/**
 * @since 2020
 * @author Huawei DTSE India
 */
class HomeRecyclerViewAdapter(val homeDataModelList: ArrayList<HomeDataModel>, val parentFragment: Fragment) : RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        public var homeCardParentLay: LinearLayout? = null

        fun bindItems(homeDataModel: HomeDataModel) {
            val homeCardTitleTV = itemView.findViewById(R.id.homeCardTitleTV) as TextView
            val homeDataCountTV = itemView.findViewById(R.id.homeDataCountTV) as TextView
            val homeCardIConsIMV = itemView.findViewById(R.id.homeCardIConsIMV) as ImageView

            homeCardParentLay = itemView.findViewById(R.id.homeCardParentLay) as LinearLayout

            homeCardTitleTV.text = homeDataModel.homeCardTitleTV
            homeDataCountTV.text = homeDataModel.homeDataCountTV
            homeCardIConsIMV.setImageResource(homeDataModel.homeCardIConsIMV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_items, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //bind the items
        holder.bindItems(homeDataModelList[position])
        holder.homeCardParentLay!!.setOnClickListener {
            //action to navigate food screen
            if (position == 0) {
                parentFragment.findNavController().navigate(R.id.action_homeScreenDest_to_timePeriodDialog)
            }
            //action to navigate water intake
            if (position == 1) {
                parentFragment.findNavController().navigate(R.id.waterIntakeFragment)
            }
            //action to navigate stepwalkscreen
            else if(position == 2)
            {
                //parentFragment.findNavController().navigate(R.id.stepsWalkedFragment)
            }
            //action to navigate calBurn screen
            else if(position == 3)
            {
                parentFragment.findNavController().navigate(R.id.calBurnedFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return homeDataModelList.size
    }
}

