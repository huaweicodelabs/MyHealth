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
import java.util.*
/**
 * @since 2020
 * @author Huawei DTSE India
 */
@Entity(tableName = "food_intake")
data class FoodIntake(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "serial_no")
        val sNo: Int,
        @ColumnInfo(name = "uid")
        val userUIDStr: String,
        @ColumnInfo(name = "time_period")
        val timePeriod: String,
        @ColumnInfo(name = "food_id")
        val foodId: Int,
        @ColumnInfo(name = "food_name")
        val foodName: String,
        @ColumnInfo(name = "energy_cal")
        val energy: Int,
        @ColumnInfo(name = "quantity_taken")
        val quantityTaken: String,
        @ColumnInfo(name = "created_at")
        val createdAt: Date,
        @ColumnInfo(name = "last_update_at")
        val lastUpdateAt: Date
)