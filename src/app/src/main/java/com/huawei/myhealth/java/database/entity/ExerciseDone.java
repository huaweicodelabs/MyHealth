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
@Entity(tableName = "exercise_done")
public class ExerciseDone {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    @NonNull
    String userUIDStr;
    @ColumnInfo(name = "date_time")
    String dateNTime;
    @ColumnInfo(name = "exercise_id")
    int exerciseId;
    @ColumnInfo(name = "exercise_name")
    String exerciseName;
    //In Minutes
    @ColumnInfo(name = "exercise_duration")
    String exerciseDuration;
    @ColumnInfo(name = "speed")
    String speed;
    //In Kilo Meters
    @ColumnInfo(name = "distance_covered")
    String distanceCovered;
    @ColumnInfo(name = "calorie_burned")
    String calorieBurned;
    @ColumnInfo(name = "details_added_from")
    String detailsAddedFrom;
    @ColumnInfo(name = "created_at")
    String createdAt;
    @ColumnInfo(name = "last_update_at")
    String lastUpdateAt;

    public ExerciseDone(String userUIDStr, String dateNTime, int exerciseId, String exerciseName, String exerciseDuration, String speed, String distanceCovered, String calorieBurned, String detailsAddedFrom, String createdAt, String lastUpdateAt) {
        this.userUIDStr = userUIDStr;
        this.dateNTime = dateNTime;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.exerciseDuration = exerciseDuration;
        this.speed = speed;
        this.distanceCovered = distanceCovered;
        this.calorieBurned = calorieBurned;
        this.detailsAddedFrom = detailsAddedFrom;
        this.createdAt = createdAt;
        this.lastUpdateAt = lastUpdateAt;
    }

    public String getUserUIDStr() {
        return userUIDStr;
    }

    public void setUserUIDStr(String userUIDStr) {
        this.userUIDStr = userUIDStr;
    }

    public String getDateNTime() {
        return dateNTime;
    }

    public void setDateNTime(String dateNTime) {
        this.dateNTime = dateNTime;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseDuration() {
        return exerciseDuration;
    }

    public void setExerciseDuration(String exerciseDuration) {
        this.exerciseDuration = exerciseDuration;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDistanceCovered() {
        return distanceCovered;
    }

    public void setDistanceCovered(String distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public String getCalorieBurned() {
        return calorieBurned;
    }

    public void setCalorieBurned(String calorieBurned) {
        this.calorieBurned = calorieBurned;
    }

    public String getDetailsAddedFrom() {
        return detailsAddedFrom;
    }

    public void setDetailsAddedFrom(String detailsAddedFrom) {
        this.detailsAddedFrom = detailsAddedFrom;
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
