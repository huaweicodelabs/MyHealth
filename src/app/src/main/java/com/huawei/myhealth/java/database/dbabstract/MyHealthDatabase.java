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

package com.huawei.myhealth.java.database.dbabstract;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.huawei.myhealth.database.converters.Converters;

import com.huawei.myhealth.java.database.dao.ExerciseDoneDao;
import com.huawei.myhealth.java.database.dao.FoodIntakeDao;
import com.huawei.myhealth.java.database.dao.FoodNutrientsDao;
import com.huawei.myhealth.java.database.dao.StepsWalkedDao;
import com.huawei.myhealth.java.database.dao.UserProfileDao;
import com.huawei.myhealth.java.database.dao.WaterIntakeDao;
import com.huawei.myhealth.java.database.entity.ExerciseDone;
import com.huawei.myhealth.java.database.entity.FoodIntake;
import com.huawei.myhealth.java.database.entity.FoodNutrientsInsight;
import com.huawei.myhealth.java.database.entity.StepsWalked;
import com.huawei.myhealth.java.database.entity.UserProfile;
import com.huawei.myhealth.java.database.entity.WaterIntake;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = {UserProfile.class, FoodNutrientsInsight.class, WaterIntake.class, FoodIntake.class, ExerciseDone.class, StepsWalked.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class MyHealthDatabase extends RoomDatabase {

    private static volatile MyHealthDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    public abstract ExerciseDoneDao excerciseDoneDao();

    public abstract UserProfileDao userProfileDao();

    public abstract FoodIntakeDao foodIntakeDao();

    public abstract FoodNutrientsDao foodNutrientsDao();

    public abstract StepsWalkedDao stepsWalkedDao();

    public abstract WaterIntakeDao waterIntakeDao();

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static MyHealthDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyHealthDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyHealthDatabase.class, "my_health_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(FoodNutrientsDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback FoodNutrientsDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {

            });
        }
    };
}
