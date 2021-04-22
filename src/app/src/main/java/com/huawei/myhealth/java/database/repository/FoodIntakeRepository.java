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
package com.huawei.myhealth.java.database.repository;

import androidx.lifecycle.LiveData;

import com.huawei.myhealth.java.database.dao.FoodIntakeDao;
import com.huawei.myhealth.java.database.dbabstract.MyHealthDatabase;
import com.huawei.myhealth.java.database.entity.FoodIntake;

import java.util.List;

/**
 * @author Huawei DTSE India
 * @since 2020
 */
public class FoodIntakeRepository {

    private FoodIntakeDao foodIntakeDao;

    public FoodIntakeRepository(FoodIntakeDao foodIntakeDao) {
        this.foodIntakeDao = foodIntakeDao;
    }

    public LiveData<List<FoodIntake>> allFoodIntake() {
        return foodIntakeDao.getAllFoodIntake();
    }

    public LiveData<List<FoodIntake>> foodTakenByFoodId(int foodIdPassed) {
        return foodIntakeDao.getFoodTakenByFoodId(foodIdPassed);
    }

    public LiveData<List<FoodIntake>> calorieTakenToday() {
        return foodIntakeDao.getCalorieTakenToday();
    }

    LiveData<List<FoodIntake>> calorieTakenYesterday() {
        return foodIntakeDao.getCalorieTakenYesterday();
    }

    public void insert(FoodIntake foodIntake) {
        MyHealthDatabase.databaseWriteExecutor.execute(() -> {
            foodIntakeDao.insert(foodIntake);
        });
    }
}
