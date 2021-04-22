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
package com.huawei.myhealth.java.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.huawei.myhealth.java.database.entity.FoodNutrientsInsight;
import com.huawei.myhealth.java.database.repository.FoodNutrientsRepository;

import java.util.List;

public class FoodNutrientsViewModel extends AndroidViewModel {

    private FoodNutrientsRepository foodNutrientsRepository;
    private int foodId = 0;

    public FoodNutrientsViewModel(@NonNull Application application,
                                  FoodNutrientsRepository foodNutrientsRepository, int foodId) {
        super(application);
        this.foodNutrientsRepository = foodNutrientsRepository;
        this.foodId = foodId;
    }

    public LiveData<List<FoodNutrientsInsight>> allFoodList() {
        return foodNutrientsRepository.allFoodRecords();
    }

    public void insert(FoodNutrientsInsight foodNutrientsInsight) {
        foodNutrientsRepository.insert(foodNutrientsInsight);
    }
}
