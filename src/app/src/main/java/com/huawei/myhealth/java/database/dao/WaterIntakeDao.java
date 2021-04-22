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

package com.huawei.myhealth.java.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.huawei.myhealth.java.database.entity.ExerciseDone;
import com.huawei.myhealth.java.database.entity.WaterIntake;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import kotlin.coroutines.Continuation;
/**
 * @since 2020
 * @author Huawei DTSE India
 */
@Dao
public interface WaterIntakeDao {
    //get water data query
    @Query("SELECT * FROM water_intake")
    List<WaterIntake> getWaterIntakeData();

    //insert query
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(@NotNull WaterIntake waterIntake);

    //update query
    @Query("UPDATE water_intake SET no_of_glass_consumed=:count WHERE date = :date")
    void update(String count,String date);


}
