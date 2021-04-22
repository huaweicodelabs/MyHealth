package com.huawei.myhealth.java.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.huawei.myhealth.java.database.entity.FoodNutrientsInsight;

import java.util.List;

/**
 * @author Huawei DTSE India
 * @since 2020
 */
@Dao
public interface FoodNutrientsDao {
    //insert query
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FoodNutrientsInsight foodNutrientsInsight);

    //get all food data query
    @Query("SELECT * FROM food_nutrients_insight ORDER BY food_id ASC")
    LiveData<List<FoodNutrientsInsight>> getAllFoodList();

    //get food by id query
    @Query("SELECT * FROM food_nutrients_insight WHERE food_id = :foodId LIMIT 1")
    LiveData<List<FoodNutrientsInsight>> getFoodById(int foodId);

    //get calories by foodid query
    @Query("SELECT * FROM food_nutrients_insight WHERE food_id = :foodId LIMIT 1")
    LiveData<List<FoodNutrientsInsight>> getCalorieInFoodById(int foodId);

    //delete query
    @Query("DELETE FROM food_nutrients_insight")
    void deleteAll();
}
