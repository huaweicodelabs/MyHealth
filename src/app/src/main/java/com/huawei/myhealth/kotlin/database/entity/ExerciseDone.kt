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

package com.huawei.myhealth.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * @since 2020
 * @author Huawei DTSE India
 */
@Entity(tableName = "exercise_done")
data class ExerciseDone(
        @PrimaryKey
        @ColumnInfo(name = "uid")
        val userUIDStr: String,
        @ColumnInfo(name = "date_time")
        val dateNTime: String,
        @ColumnInfo(name = "exercise_id")
        val exerciseId: Int,
        @ColumnInfo(name = "exercise_name")
        val exerciseName: String,
        //In Minutes
        @ColumnInfo(name = "exercise_duration")
        val exerciseDuration: String,
        @ColumnInfo(name = "speed")
        val speed: String,
        //In Kilo Meters
        @ColumnInfo(name = "distance_covered")
        val distanceCovered: String,
        @ColumnInfo(name = "calorie_burned")
        val calorieBurned: String,
        @ColumnInfo(name = "details_added_from")
        val detailsAddedFrom: String,
        @ColumnInfo(name = "created_at")
        val createdAt: String,
        @ColumnInfo(name = "last_update_at")
        val lastUpdateAt: String
)