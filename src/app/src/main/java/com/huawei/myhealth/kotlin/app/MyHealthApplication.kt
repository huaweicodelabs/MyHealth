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

package com.huawei.myhealth.kotlin.app

import android.app.Application
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.myhealth.database.dbabstract.MyHealthDatabase
import com.huawei.myhealth.database.repository.FoodIntakeRepository
import com.huawei.myhealth.database.repository.FoodNutrientsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
/**
 * @since 2020
 * @author Huawei DTSE India
 */

class MyHealthApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { MyHealthDatabase.getDatabase(this, applicationScope) }
    val foodNutrientsRepository by lazy { FoodNutrientsRepository(database.foodNutrientsDao()) }
    val foodIntakeRepository by lazy { FoodIntakeRepository(database.foodIntakeDao()) }
}