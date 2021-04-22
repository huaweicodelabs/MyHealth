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

package com.huawei.myhealth.database.dbabstract

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.huawei.myhealth.database.converters.Converters
import com.huawei.myhealth.database.dao.*
import com.huawei.myhealth.database.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
/**
 * @since 2020
 * @author Huawei DTSE India
 */
// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [UserProfile::class, FoodNutrientsInsight::class,WaterIntake::class,FoodIntake::class, ExerciseDone::class, StepsWalked::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyHealthDatabase : RoomDatabase() {
    //dao for particular query
    abstract fun userProfileDao(): UserProfileDao
    abstract fun foodNutrientsDao(): FoodNutrientsDao
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun exerciseDoneDao(): ExerciseDoneDao
    abstract fun stepsWalkedDao(): StepsWalkedDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MyHealthDatabase? = null
        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): MyHealthDatabase {
            // if the INSTANCE is null create database and return it
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyHealthDatabase::class.java,
                        "my_health_database"
                )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this codelab.
                        .fallbackToDestructiveMigration()
                        .addCallback(FoodNutrientsDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class FoodNutrientsDatabaseCallback(
                private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {
                    scope.launch {
                    }
                }
            }
        }
    }
}