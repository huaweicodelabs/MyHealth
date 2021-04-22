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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.myhealth.R
import com.huawei.myhealth.database.dbabstract.MyHealthDatabase
import com.huawei.myhealth.database.entity.WaterIntake
import com.huawei.myhealth.databinding.FragmentWaterIntakeBinding

/**
 * @since 2020
 * @author Huawei DTSE India
 */

class WaterIntakeFragment : Fragment() {

    var db: MyHealthDatabase? = null
    var sharedPreference: SharedPreferences? = null
    var count: Int = 0
    private var _binding: FragmentWaterIntakeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentWaterIntakeBinding.inflate(inflater, container, false)
        val view = binding.root

        // To Show / Hide Action Bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        // initilize room db
        db = activity?.let { Room.databaseBuilder(it, MyHealthDatabase::class.java, resources.getString(R.string.my_health_database)).build() }
        // initialize shared preference
        sharedPreference = activity?.getSharedPreferences( resources.getString(R.string.APP), Context.MODE_PRIVATE)

        // onClick funtions
        onClick()

        // get data from data
        getDataFromDB()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClick() {
        // onclick of add water
        binding.ivAddwater!!.setOnClickListener {
            addWaterCount()
        }
    }

    private fun addWaterCount() {
        ++count

        var instance: HiAnalyticsInstance? = null
        // Generate the Analytics Instance
        instance = HiAnalytics.getInstance(activity)
        val bundle = Bundle()
        bundle.putString( resources.getString(R.string.watercount), count.toString())
        instance.onEvent( resources.getString(R.string.watercount), bundle)
        binding.tvProgressWatertaken.setProgress(count)
        binding.tvWatertaken.setText("" + count + resources.getString(R.string.watercount_total) )

        if (count >= 10) {
            binding.tvProgressWatertaken.setProgress(count)
            binding.tvWatertaken.setText("" + count + resources.getString(R.string.watercount_total))
            binding.tvStatus.setText(resources.getString(R.string.completed))
        }
        var uid = sharedPreference?.getString(resources.getString(R.string.UID), "")
        var dateselected = sharedPreference?.getString(resources.getString(R.string.date_selected), "")
        val waterIntake = WaterIntake(uid.toString(), dateselected.toString(), count)

        // insert to db
        val thread = Thread {
            if (count == 1) {
                // insert Records
                db?.waterIntakeDao()?.insert(waterIntake)
            } else {
                // update Records
                db?.waterIntakeDao()?.update(count.toString(), dateselected)
            }
        }
        thread.start()
        thread.interrupt()
    }

    private fun getDataFromDB() {
        var selectedDateStr = sharedPreference?.getString(resources.getString(R.string.date_selected), "")
        // getData from db
        val thread = Thread {

            // fetch Records
            db?.waterIntakeDao()?.getWaterIntakeData()?.forEach()
            {
                if (selectedDateStr.equals(it.date)) {
                    count = it.noOfGlassConsumed

                    binding.tvProgressWatertaken.setProgress(count)
                    binding.tvWatertaken.setText("" + count + resources.getString(R.string.watercount_total))

                    if (count >= 10) {
                        binding.tvProgressWatertaken.setProgress(count)
                        binding.tvWatertaken.setText("" + count + resources.getString(R.string.watercount_total))
                        binding.tvStatus.setText(resources.getString(R.string.completed))
                    }
                }
            }
        }
        thread.start()
        thread.interrupt()
    }
}