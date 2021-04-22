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
@Entity(tableName = "user_profile")
public class UserProfile {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    @NonNull
    String userUIDStr;
    @ColumnInfo(name = "full_name")
    String fullNameStr;
    @ColumnInfo(name = "mobile_no")
    String mobileNoStr;
    @ColumnInfo(name = "email_id")
    String emailIdStr;
    @ColumnInfo(name = "date_of_birth")
    String userDateOfBirthStr;
    @ColumnInfo(name = "bio")
    String userBioStr;
    @ColumnInfo(name = "gender")
    String userGenderStr;
    @ColumnInfo(name = "height")
    String userHeightInCMInt;
    @ColumnInfo(name = "weight")
    String userWeightInKGInt;
    @ColumnInfo(name = "created_at")
    String createdAt;
    @ColumnInfo(name = "last_update_at")
    String lastUpdateAt;

    public UserProfile(@NonNull String userUIDStr, String fullNameStr, String mobileNoStr, String emailIdStr, String userDateOfBirthStr, String userBioStr, String userGenderStr, String userHeightInCMInt, String userWeightInKGInt, String createdAt, String lastUpdateAt) {
        this.userUIDStr = userUIDStr;
        this.fullNameStr = fullNameStr;
        this.mobileNoStr = mobileNoStr;
        this.emailIdStr = emailIdStr;
        this.userDateOfBirthStr = userDateOfBirthStr;
        this.userBioStr = userBioStr;
        this.userGenderStr = userGenderStr;
        this.userHeightInCMInt = userHeightInCMInt;
        this.userWeightInKGInt = userWeightInKGInt;
        this.createdAt = createdAt;
        this.lastUpdateAt = lastUpdateAt;
    }

    public String getUserUIDStr() {
        return userUIDStr;
    }

    public void setUserUIDStr(String userUIDStr) {
        this.userUIDStr = userUIDStr;
    }

    public String getFullNameStr() {
        return fullNameStr;
    }

    public void setFullNameStr(String fullNameStr) {
        this.fullNameStr = fullNameStr;
    }

    public String getMobileNoStr() {
        return mobileNoStr;
    }

    public void setMobileNoStr(String mobileNoStr) {
        this.mobileNoStr = mobileNoStr;
    }

    public String getEmailIdStr() {
        return emailIdStr;
    }

    public void setEmailIdStr(String emailIdStr) {
        this.emailIdStr = emailIdStr;
    }

    public String getUserDateOfBirthStr() {
        return userDateOfBirthStr;
    }

    public void setUserDateOfBirthStr(String userDateOfBirthStr) {
        this.userDateOfBirthStr = userDateOfBirthStr;
    }

    public String getUserBioStr() {
        return userBioStr;
    }

    public void setUserBioStr(String userBioStr) {
        this.userBioStr = userBioStr;
    }

    public String getUserGenderStr() {
        return userGenderStr;
    }

    public void setUserGenderStr(String userGenderStr) {
        this.userGenderStr = userGenderStr;
    }

    public String getUserHeightInCMInt() {
        return userHeightInCMInt;
    }

    public void setUserHeightInCMInt(String userHeightInCMInt) {
        this.userHeightInCMInt = userHeightInCMInt;
    }

    public String getUserWeightInKGInt() {
        return userWeightInKGInt;
    }

    public void setUserWeightInKGInt(String userWeightInKGInt) {
        this.userWeightInKGInt = userWeightInKGInt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(String lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }
}
