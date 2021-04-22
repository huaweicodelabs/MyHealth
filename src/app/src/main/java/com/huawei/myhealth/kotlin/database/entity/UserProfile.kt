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
@Entity(tableName = "user_profile")
data class UserProfile(
        @PrimaryKey
        @ColumnInfo(name = "uid")
        val userUIDStr: String,
        @ColumnInfo(name = "full_name")
        val fullNameStr: String,
        @ColumnInfo(name = "mobile_no")
        val mobileNoStr: String,
        @ColumnInfo(name = "email_id")
        val emailIdStr: String,
        @ColumnInfo(name = "date_of_birth")
        val userDateOfBirthStr: String,
        @ColumnInfo(name = "bio")
        val userBioStr: String,
        @ColumnInfo(name = "gender")
        val userGenderStr: String,
        @ColumnInfo(name = "height")
        val userHeightInCMInt: String,
        @ColumnInfo(name = "weight")
        val userWeightInKGInt: String,
        @ColumnInfo(name = "created_at")
        val createdAt: String,
        @ColumnInfo(name = "last_update_at")
        val lastUpdateAt: String




)