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

import com.huawei.myhealth.java.database.dao.FoodNutrientsDao;
import com.huawei.myhealth.java.database.dbabstract.MyHealthDatabase;
import com.huawei.myhealth.java.database.entity.FoodNutrientsInsight;

import java.util.List;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class FoodNutrientsRepository {

    private FoodNutrientsDao foodNutrientsDao;

    public FoodNutrientsRepository(FoodNutrientsDao foodNutrientsDao) {
        this.foodNutrientsDao = foodNutrientsDao;
    }

    public LiveData<List<FoodNutrientsInsight>> allFoodRecords(){
        return foodNutrientsDao.getAllFoodList();
    }

    LiveData<List<FoodNutrientsInsight>> foodNutrientsDetailsById(int foodIdPassed){
        return foodNutrientsDao.getFoodById(foodIdPassed);
    }

    LiveData<List<FoodNutrientsInsight>> foodCalorieById(int foodIdPassed){
        return foodNutrientsDao.getCalorieInFoodById(foodIdPassed);
    }

    public void insert(FoodNutrientsInsight foodNutrientsInsight) {
        MyHealthDatabase.databaseWriteExecutor.execute(() -> {
            foodNutrientsDao.insert(foodNutrientsInsight);
        });
    }
}
