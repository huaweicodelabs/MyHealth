package com.huawei.myhealth.java.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.huawei.myhealth.java.database.entity.FoodIntake;

import java.util.List;

/**
 * @author Huawei DTSE India
 * @since 2020
 */
@Dao
public interface FoodIntakeDao {

    //insert query
    @Insert
    void insert(FoodIntake foodIntake);

    //get allfood data query
    @Query("SELECT * FROM food_intake ORDER BY created_at ASC")
    LiveData<List<FoodIntake>> getAllFoodIntake();

    //get foodtaken by id query
    @Query("SELECT * FROM food_intake WHERE food_id = :foodId")
    LiveData<List<FoodIntake>> getFoodTakenByFoodId(int foodId);

    //get calorie data query
    @Query("SELECT * FROM food_intake WHERE date(datetime(created_at / 1000 , 'unixepoch')) = date('now')  ")
    LiveData<List<FoodIntake>> getCalorieTakenToday();

    //get calorie data for other dates query
    @Query("SELECT * FROM food_intake WHERE date(datetime(created_at / 1000 , 'unixepoch')) = date('now', '-1 day') ")
    LiveData<List<FoodIntake>> getCalorieTakenYesterday();

    //delete query
    @Query("DELETE FROM food_intake")
    void deleteAll();
}
