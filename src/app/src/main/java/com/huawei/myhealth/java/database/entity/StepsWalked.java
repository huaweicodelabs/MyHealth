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
package com.huawei.myhealth.java.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * @since 2020
 * @author Huawei DTSE India
 */
@Entity(tableName = "steps_walked")
public class StepsWalked {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    @NonNull
    String userUIDStr;

    public String getUserUIDStr() {
        return userUIDStr;
    }

    public void setUserUIDStr(String userUIDStr) {
        this.userUIDStr = userUIDStr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStepsWalked() {
        return stepsWalked;
    }

    public void setStepsWalked(String stepsWalked) {
        this.stepsWalked = stepsWalked;
    }

    @ColumnInfo(name = "date")
    String date;
    @ColumnInfo(name="steps_walked")
    String stepsWalked;

    public StepsWalked(String userUIDStr, String date, String stepsWalked) {
        this.userUIDStr = userUIDStr;
        this.date = date;
        this.stepsWalked = stepsWalked;
    }




}
