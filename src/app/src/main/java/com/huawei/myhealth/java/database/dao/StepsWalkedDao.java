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


import com.huawei.myhealth.java.database.entity.StepsWalked;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import kotlin.coroutines.Continuation;
/**
 * @since 2020
 * @author Huawei DTSE India
 */
@Dao
public interface StepsWalkedDao {
    //get steps data query
    @Query("SELECT * FROM steps_walked")
    List<com.huawei.myhealth.java.database.entity.StepsWalked> getStepsWalkedData();
    //insert query
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(@NotNull StepsWalked stepsWalked);
    //update query
    @Query("UPDATE steps_walked SET steps_walked=:stepData WHERE date = :date")
    void update(@Nullable String stepData, @Nullable String date);

}
