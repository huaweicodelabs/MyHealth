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

package com.huawei.myhealth.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huawei.myhealth.database.entity.FoodNutrientsInsight
import kotlinx.coroutines.flow.Flow
/**
 * @since 2020
 * @author Huawei DTSE India
 */
@Dao
interface FoodNutrientsDao {
    //insert query
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(foodNutrientsInsight: FoodNutrientsInsight)
    //getallfood data query
    @Query("SELECT * FROM food_nutrients_insight ORDER BY food_id ASC")
    fun getAllFoodList(): Flow<List<FoodNutrientsInsight>>
    //getfood by id query
    @Query("SELECT * FROM food_nutrients_insight WHERE food_id = :foodId LIMIT 1")
    fun getFoodById(foodId: Int): Flow<List<FoodNutrientsInsight>>
   //getcalories by foodid query
    @Query("SELECT * FROM food_nutrients_insight WHERE food_id = :foodId LIMIT 1")
    fun getCalorieInFoodById(foodId: Int): Flow<List<FoodNutrientsInsight>>
    //delete query
    @Query("DELETE FROM food_nutrients_insight")
    suspend fun deleteAll()
}