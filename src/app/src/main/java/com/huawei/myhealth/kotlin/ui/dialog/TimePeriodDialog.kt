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

package com.huawei.myhealth.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.huawei.myhealth.R
import com.huawei.myhealth.databinding.DialogTimePeriodBinding
/**
 * @since 2020
 * @author Huawei DTSE India
 */
class TimePeriodDialog : BottomSheetDialogFragment(), View.OnClickListener {
    private var _binding: DialogTimePeriodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogTimePeriodBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.breakFastTV.setOnClickListener(this)
        binding.morningSnackTV.setOnClickListener(this)
        binding.lunchTV.setOnClickListener(this)
        binding.eveningSnackTV.setOnClickListener(this)
        binding.dinnerTV.setOnClickListener(this)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onClick(v: View?) {
        val id = v?.id

        when (id) {
            binding.breakFastTV.id -> {
                findNavController().navigate(R.id.action_timePeriodDialog_to_foodSearchFragment)
            }
        }
    }
}