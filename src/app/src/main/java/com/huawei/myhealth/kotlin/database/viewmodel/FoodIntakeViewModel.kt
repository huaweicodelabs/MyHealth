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

package com.huawei.myhealth.database.viewmodel

import androidx.lifecycle.*
import com.huawei.myhealth.database.entity.FoodIntake
import com.huawei.myhealth.database.repository.FoodIntakeRepository
import kotlinx.coroutines.launch
/**
 * @since 2020
 * @author Huawei DTSE India
 */

class FoodIntakeViewModel(private val foodIntakeRepository: FoodIntakeRepository, var foodId: Int = 0) : ViewModel() {
    val calorieTakenToday: LiveData<List<FoodIntake>> = foodIntakeRepository.calorieTakenToday.asLiveData()
    /*
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(foodIntake: FoodIntake) = viewModelScope.launch {
        foodIntakeRepository.insert(foodIntake)
    }

    class FoodIntakeViewModelFactory(private val foodIntakeRepository: FoodIntakeRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FoodIntakeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FoodIntakeViewModel(foodIntakeRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}