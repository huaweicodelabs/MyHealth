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
import com.huawei.myhealth.database.entity.FoodNutrientsInsight
import com.huawei.myhealth.database.repository.FoodNutrientsRepository
import kotlinx.coroutines.launch
/**
 * @since 2020
 * @author Huawei DTSE India
 */

open class FoodNutrientsViewModel(private val foodNutrientsRepository: FoodNutrientsRepository, var foodId: Int = 0) : ViewModel() {

    val allFoodList: LiveData<List<FoodNutrientsInsight>> = foodNutrientsRepository.allFoodRecords.asLiveData()
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(foodNutrientsInsight: FoodNutrientsInsight) = viewModelScope.launch {
        foodNutrientsRepository.insert(foodNutrientsInsight)
    }

    class FoodNutrientsViewModelFactory(private val repository: FoodNutrientsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FoodNutrientsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FoodNutrientsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}