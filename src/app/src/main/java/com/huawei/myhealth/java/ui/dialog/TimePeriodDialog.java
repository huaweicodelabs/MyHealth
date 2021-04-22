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

package com.huawei.myhealth.java.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.huawei.myhealth.R;
import com.huawei.myhealth.databinding.DialogTimePeriodBinding;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class TimePeriodDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private DialogTimePeriodBinding _binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _binding = DialogTimePeriodBinding.inflate(inflater, container, false);
        View view = _binding.getRoot();

        _binding.breakFastTV.setOnClickListener(this);
        _binding.morningSnackTV.setOnClickListener(this);
        _binding.lunchTV.setOnClickListener(this);
        _binding.eveningSnackTV.setOnClickListener(this);
        _binding.dinnerTV.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.breakFastTV) {
            Navigation.findNavController(view).navigate(R.id.action_timePeriodDialog_to_foodSearchFragment);
        }
    }
}
