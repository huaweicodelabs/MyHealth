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

package com.huawei.myhealth.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
/**
 * @since 2020
 * @author Huawei DTSE India
 */
@Entity(tableName = "food_nutrients_insight")
class FoodNutrientsInsight(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "food_id")
        val foodId: Int,
        @ColumnInfo(name = "food_name")
        val foodName: String,
        @ColumnInfo(name = "food_portion_type")
        val foodPortionType: String,
        @ColumnInfo(name = "food_portion")
        val foodPortion: Int,
        @ColumnInfo(name = "thumbnail")
        val foodThumbnail: String,
        @ColumnInfo(name = "full_pic")
        val foodFullPic: String,
        @ColumnInfo(name = "energy") // In Calorie
        val energyKCal: Int,
        @ColumnInfo(name = "protein") // In Grams
        val protein: String,
        @ColumnInfo(name = "total_lipid_fat") //In Grams
        val totalLipidFat: String,
        @ColumnInfo(name = "carbohydrate") //In Grams
        val carbohydrate: String,
        @ColumnInfo(name = "fiber_total_dietary") //In Grams
        val fiberTotalDietary: String,
        @ColumnInfo(name = "sugars_total_including_nlea") //In Grams
        val sugarsTotalIncludingNLEA: String,
        @ColumnInfo(name = "calcium_ca") //In Milli Grams
        val calciumCa: String,
        @ColumnInfo(name = "iron_fe") //In Milli Grams
        val ironFe: String,
        @ColumnInfo(name = "sodium_na") //In Milli Grams
        val sodiumNa: String,
        @ColumnInfo(name = "potassium_k") //In Milli Grams
        val potassiumK: String,
        @ColumnInfo(name = "iodine_i")
        val iodineI: String,
        @ColumnInfo(name = "zinc_zn")
        val zincZn: String,
        @ColumnInfo(name = "magnesium_mg")
        val magnesiumMg: String,
        @ColumnInfo(name = "cholesterol_mg") //In Milli Grams
        val cholesterolMg: String,
        @ColumnInfo(name = "fatty_acid_saturated") //In Grams
        val fattyAcidSaturated: String,
        @ColumnInfo(name = "fatty_acid_monounsaturated") //In Grams
        val fattyAcidMonounsaturated: String,
        @ColumnInfo(name = "fatty_acid_polyunsaturated") //In Grams
        val fattyAcidPolyunsaturated: String,
        @ColumnInfo(name = "fatty_acid_trans") //In Grams
        val fattyAcidTrans: String,
        @ColumnInfo(name = "vitamin_a")
        val vitaminA: String,
        @ColumnInfo(name = "vitamin_b")
        val vitaminB: String,
        @ColumnInfo(name = "vitamin_b2")
        val vitaminB2: String,
        @ColumnInfo(name = "vitamin_b3")
        val vitaminB3: String,
        @ColumnInfo(name = "vitamin_b5")
        val vitaminB5: String,
        @ColumnInfo(name = "vitamin_b6")
        val vitaminB6: String,
        @ColumnInfo(name = "vitamin_b7")
        val vitaminB7: String,
        @ColumnInfo(name = "vitamin_b9")
        val vitaminB9: String,
        @ColumnInfo(name = "vitamin_b12")
        val vitaminB12: String,
        @ColumnInfo(name = "vitamin_c")
        val vitaminC: String,
        @ColumnInfo(name = "vitamin_d")
        val vitaminD: String,
        @ColumnInfo(name = "vitamin_e")
        val vitaminE: String,
        @ColumnInfo(name = "vitamin_k")
        val vitaminK: String,
        @ColumnInfo(name = "caffeine") //In Milli Grams
        val caffeine: String
)