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
import androidx.room.Query
import com.huawei.myhealth.database.entity.FoodIntake
import kotlinx.coroutines.flow.Flow
/**
 * @since 2020
 * @author Huawei DTSE India
 */
@Dao
interface FoodIntakeDao {
    //insert query
    @Insert
    suspend fun insert(foodIntake: FoodIntake)
    //get allfood data query
    @Query("SELECT * FROM food_intake ORDER BY created_at ASC")
    fun getAllFoodIntake(): Flow<List<FoodIntake>>
   //get foodtaken by id query
    @Query("SELECT * FROM food_intake WHERE food_id = :foodId")
    fun getFoodTakenByFoodId(foodId: Int): Flow<List<FoodIntake>>
    //get calorie data query
    @Query("SELECT * FROM food_intake WHERE date(datetime(created_at / 1000 , 'unixepoch')) = date('now')  ")
    fun getCalorieTakenToday(): Flow<List<FoodIntake>>
     //get calorie data for other dates query
    @Query("SELECT * FROM food_intake WHERE date(datetime(created_at / 1000 , 'unixepoch')) = date('now', '-1 day') ")
    fun getCalorieTakenYesterday(): Flow<List<FoodIntake>>
   //delete query
    @Query("DELETE FROM food_intake")
    suspend fun deleteAll()
}