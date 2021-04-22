package com.huawei.myhealth.java.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.huawei.myhealth.java.database.converters.Converters;

import java.util.*;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
@Entity(tableName = "food_intake")
public class FoodIntake {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "serial_no")
    public int sNo;
    @ColumnInfo(name = "uid")
    private String userUIDStr;
    @ColumnInfo(name = "time_period")
    private String timePeriod;
    @ColumnInfo(name = "food_id")
    private int foodId;
    @ColumnInfo(name = "food_name")
    private String foodName;
    @ColumnInfo(name = "energy_cal")
    private int energy;
    @ColumnInfo(name = "quantity_taken")
    private String quantityTaken;
    @ColumnInfo(name = "created_at")
    private Date createdAt;
    @ColumnInfo(name = "last_update_at")
    private Date lastUpdateAt;


    public FoodIntake(int sNo, String userUIDStr, String timePeriod, int foodId, String foodName, int energy, String quantityTaken, Date createdAt, Date lastUpdateAt) {
        this.sNo = sNo;
        this.userUIDStr = userUIDStr;
        this.timePeriod = timePeriod;
        this.foodId = foodId;
        this.foodName = foodName;
        this.energy = energy;
        this.quantityTaken = quantityTaken;
        this.createdAt = createdAt;
        this.lastUpdateAt = lastUpdateAt;
    }

    public int getsNo() {
        return sNo;
    }

    public void setsNo(int sNo) {
        this.sNo = sNo;
    }

    public String getUserUIDStr() {
        return userUIDStr;
    }

    public void setUserUIDStr(String userUIDStr) {
        this.userUIDStr = userUIDStr;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public String getQuantityTaken() {
        return quantityTaken;
    }

    public void setQuantityTaken(String quantityTaken) {
        this.quantityTaken = quantityTaken;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(Date lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }
}
