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
import com.huawei.myhealth.database.entity.StepsWalked
/**
 * @since 2020
 * @author Huawei DTSE India
 */

@Dao
interface StepsWalkedDao {
    //get steps data query
    @Query("SELECT * FROM steps_walked")
    fun getStepsWalkedData(): List<StepsWalked>
    //insert query
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(stepsWalked: StepsWalked)
    //update query
    @Query("UPDATE steps_walked SET steps_walked=:stepData WHERE date = :date")
    fun update(stepData: String?, date: String?)
    //delete query
    @Query("DELETE FROM steps_walked")
    suspend fun deleteAll()
}