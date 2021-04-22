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

package com.huawei.myhealth.database.repository

import androidx.annotation.WorkerThread
import com.huawei.myhealth.database.dao.FoodNutrientsDao
import com.huawei.myhealth.database.entity.FoodNutrientsInsight
import kotlinx.coroutines.flow.Flow
/**
 * @since 2020
 * @author Huawei DTSE India
 */
class FoodNutrientsRepository(private val foodNutrientsDao: FoodNutrientsDao) {

    val allFoodRecords: Flow<List<FoodNutrientsInsight>> = foodNutrientsDao.getAllFoodList()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(foodNutrientsInsight: FoodNutrientsInsight) {
        foodNutrientsDao.insert(foodNutrientsInsight)
    }
}