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
@Entity(tableName = "water_intake")
public class WaterIntake {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    @NonNull
    private String userUIDStr;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "no_of_glass_consumed")
    private int noOfGlassConsumed;

    public WaterIntake(@NonNull String userUIDStr, String date, int noOfGlassConsumed) {
        this.userUIDStr = userUIDStr;
        this.date = date;
        this.noOfGlassConsumed = noOfGlassConsumed;
    }

    @NonNull
    public String getUserUIDStr() {
        return userUIDStr;
    }

    public void setUserUIDStr(@NonNull String userUIDStr) {
        this.userUIDStr = userUIDStr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNoOfGlassConsumed() {
        return noOfGlassConsumed;
    }

    public void setNoOfGlassConsumed(int noOfGlassConsumed) {
        this.noOfGlassConsumed = noOfGlassConsumed;
    }
}
