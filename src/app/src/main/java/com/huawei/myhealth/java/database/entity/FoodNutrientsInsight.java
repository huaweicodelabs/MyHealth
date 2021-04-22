package com.huawei.myhealth.java.database.entity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * @author Huawei DTSE India
 * @since 2020
 */
@Entity(tableName = "food_nutrients_insight")
public class FoodNutrientsInsight {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "food_id")
    private int foodId;
    @ColumnInfo(name = "food_name")
    private String foodName;
    @ColumnInfo(name = "food_portion_type")
    private String foodPortionType;
    @ColumnInfo(name = "food_portion")
    private int foodPortion;
    @ColumnInfo(name = "thumbnail")
    private String foodThumbnail;
    @ColumnInfo(name = "full_pic")
    private String foodFullPic;
    @ColumnInfo(name = "energy") // In Calorie
    private int energyKCal;
    @ColumnInfo(name = "protein") // In Grams
    private String protein;
    @ColumnInfo(name = "total_lipid_fat") //In Grams
    private String totalLipidFat;
    @ColumnInfo(name = "carbohydrate") //In Grams
    private String carbohydrate;
    @ColumnInfo(name = "fiber_total_dietary") //In Grams
    private String fiberTotalDietary;
    @ColumnInfo(name = "sugars_total_including_nlea") //In Grams
    private String sugarsTotalIncludingNLEA;
    @ColumnInfo(name = "calcium_ca") //In Milli Grams
    private String calciumCa;
    @ColumnInfo(name = "iron_fe") //In Milli Grams
    private String ironFe;
    @ColumnInfo(name = "sodium_na") //In Milli Grams
    private String sodiumNa;
    @ColumnInfo(name = "potassium_k") //In Milli Grams
    private String potassiumK;
    @ColumnInfo(name = "iodine_i")
    private String iodineI;
    @ColumnInfo(name = "zinc_zn")
    private String zincZn;
    @ColumnInfo(name = "magnesium_mg")
    private String magnesiumMg;
    @ColumnInfo(name = "cholesterol_mg") //In Milli Grams
    private String cholesterolMg;
    @ColumnInfo(name = "fatty_acid_saturated") //In Grams
    private String fattyAcidSaturated;
    @ColumnInfo(name = "fatty_acid_monounsaturated") //In Grams
    private String fattyAcidMonounsaturated;
    @ColumnInfo(name = "fatty_acid_polyunsaturated") //In Grams
    private String fattyAcidPolyunsaturated;
    @ColumnInfo(name = "fatty_acid_trans") //In Grams
    private String fattyAcidTrans;
    @ColumnInfo(name = "vitamin_a")
    private String vitaminA;
    @ColumnInfo(name = "vitamin_b")
    private String vitaminB;
    @ColumnInfo(name = "vitamin_b2")
    private String vitaminB2;
    @ColumnInfo(name = "vitamin_b3")
    private String vitaminB3;
    @ColumnInfo(name = "vitamin_b5")
    private String vitaminB5;
    @ColumnInfo(name = "vitamin_b6")
    private String vitaminB6;
    @ColumnInfo(name = "vitamin_b7")
    private String vitaminB7;
    @ColumnInfo(name = "vitamin_b9")
    private String vitaminB9;
    @ColumnInfo(name = "vitamin_b12")
    private String vitaminB12;
    @ColumnInfo(name = "vitamin_c")
    private String vitaminC;
    @ColumnInfo(name = "vitamin_d")
    private String vitaminD;
    @ColumnInfo(name = "vitamin_e")
    private String vitaminE;
    @ColumnInfo(name = "vitamin_k")
    private String vitaminK;
    @ColumnInfo(name = "caffeine") //In Milli Grams
    private String caffeine;

    public FoodNutrientsInsight(int foodId, String foodName, String foodPortionType, int foodPortion,
                                String foodThumbnail, String foodFullPic, int energyKCal, String protein,
                                String totalLipidFat, String carbohydrate, String fiberTotalDietary,
                                String sugarsTotalIncludingNLEA, String calciumCa, String ironFe,
                                String sodiumNa, String potassiumK, String iodineI, String zincZn,
                                String magnesiumMg, String cholesterolMg, String fattyAcidSaturated,
                                String fattyAcidMonounsaturated, String fattyAcidPolyunsaturated,
                                String fattyAcidTrans, String vitaminA, String vitaminB, String vitaminB2,
                                String vitaminB3, String vitaminB5, String vitaminB6, String vitaminB7,
                                String vitaminB9, String vitaminB12, String vitaminC, String vitaminD,
                                String vitaminE, String vitaminK, String caffeine) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPortionType = foodPortionType;
        this.foodPortion = foodPortion;
        this.foodThumbnail = foodThumbnail;
        this.foodFullPic = foodFullPic;
        this.energyKCal = energyKCal;
        this.protein = protein;
        this.totalLipidFat = totalLipidFat;
        this.carbohydrate = carbohydrate;
        this.fiberTotalDietary = fiberTotalDietary;
        this.sugarsTotalIncludingNLEA = sugarsTotalIncludingNLEA;
        this.calciumCa = calciumCa;
        this.ironFe = ironFe;
        this.sodiumNa = sodiumNa;
        this.potassiumK = potassiumK;
        this.iodineI = iodineI;
        this.zincZn = zincZn;
        this.magnesiumMg = magnesiumMg;
        this.cholesterolMg = cholesterolMg;
        this.fattyAcidSaturated = fattyAcidSaturated;
        this.fattyAcidMonounsaturated = fattyAcidMonounsaturated;
        this.fattyAcidPolyunsaturated = fattyAcidPolyunsaturated;
        this.fattyAcidTrans = fattyAcidTrans;
        this.vitaminA = vitaminA;
        this.vitaminB = vitaminB;
        this.vitaminB2 = vitaminB2;
        this.vitaminB3 = vitaminB3;
        this.vitaminB5 = vitaminB5;
        this.vitaminB6 = vitaminB6;
        this.vitaminB7 = vitaminB7;
        this.vitaminB9 = vitaminB9;
        this.vitaminB12 = vitaminB12;
        this.vitaminC = vitaminC;
        this.vitaminD = vitaminD;
        this.vitaminE = vitaminE;
        this.vitaminK = vitaminK;
        this.caffeine = caffeine;
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

    public String getFoodPortionType() {
        return foodPortionType;
    }

    public void setFoodPortionType(String foodPortionType) {
        this.foodPortionType = foodPortionType;
    }

    public int getFoodPortion() {
        return foodPortion;
    }

    public void setFoodPortion(int foodPortion) {
        this.foodPortion = foodPortion;
    }

    public String getFoodThumbnail() {
        return foodThumbnail;
    }

    public void setFoodThumbnail(String foodThumbnail) {
        this.foodThumbnail = foodThumbnail;
    }

    public String getFoodFullPic() {
        return foodFullPic;
    }

    public void setFoodFullPic(String foodFullPic) {
        this.foodFullPic = foodFullPic;
    }

    public int getEnergyKCal() {
        return energyKCal;
    }

    public void setEnergyKCal(int energyKCal) {
        this.energyKCal = energyKCal;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getTotalLipidFat() {
        return totalLipidFat;
    }

    public void setTotalLipidFat(String totalLipidFat) {
        this.totalLipidFat = totalLipidFat;
    }

    public String getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(String carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public String getFiberTotalDietary() {
        return fiberTotalDietary;
    }

    public void setFiberTotalDietary(String fiberTotalDietary) {
        this.fiberTotalDietary = fiberTotalDietary;
    }

    public String getSugarsTotalIncludingNLEA() {
        return sugarsTotalIncludingNLEA;
    }

    public void setSugarsTotalIncludingNLEA(String sugarsTotalIncludingNLEA) {
        this.sugarsTotalIncludingNLEA = sugarsTotalIncludingNLEA;
    }

    public String getCalciumCa() {
        return calciumCa;
    }

    public void setCalciumCa(String calciumCa) {
        this.calciumCa = calciumCa;
    }

    public String getIronFe() {
        return ironFe;
    }

    public void setIronFe(String ironFe) {
        this.ironFe = ironFe;
    }

    public String getSodiumNa() {
        return sodiumNa;
    }

    public void setSodiumNa(String sodiumNa) {
        this.sodiumNa = sodiumNa;
    }

    public String getPotassiumK() {
        return potassiumK;
    }

    public void setPotassiumK(String potassiumK) {
        this.potassiumK = potassiumK;
    }

    public String getIodineI() {
        return iodineI;
    }

    public void setIodineI(String iodineI) {
        this.iodineI = iodineI;
    }

    public String getZincZn() {
        return zincZn;
    }

    public void setZincZn(String zincZn) {
        this.zincZn = zincZn;
    }

    public String getMagnesiumMg() {
        return magnesiumMg;
    }

    public void setMagnesiumMg(String magnesiumMg) {
        this.magnesiumMg = magnesiumMg;
    }

    public String getCholesterolMg() {
        return cholesterolMg;
    }

    public void setCholesterolMg(String cholesterolMg) {
        this.cholesterolMg = cholesterolMg;
    }

    public String getFattyAcidSaturated() {
        return fattyAcidSaturated;
    }

    public void setFattyAcidSaturated(String fattyAcidSaturated) {
        this.fattyAcidSaturated = fattyAcidSaturated;
    }

    public String getFattyAcidMonounsaturated() {
        return fattyAcidMonounsaturated;
    }

    public void setFattyAcidMonounsaturated(String fattyAcidMonounsaturated) {
        this.fattyAcidMonounsaturated = fattyAcidMonounsaturated;
    }

    public String getFattyAcidPolyunsaturated() {
        return fattyAcidPolyunsaturated;
    }

    public void setFattyAcidPolyunsaturated(String fattyAcidPolyunsaturated) {
        this.fattyAcidPolyunsaturated = fattyAcidPolyunsaturated;
    }

    public String getFattyAcidTrans() {
        return fattyAcidTrans;
    }

    public void setFattyAcidTrans(String fattyAcidTrans) {
        this.fattyAcidTrans = fattyAcidTrans;
    }

    public String getVitaminA() {
        return vitaminA;
    }

    public void setVitaminA(String vitaminA) {
        this.vitaminA = vitaminA;
    }

    public String getVitaminB() {
        return vitaminB;
    }

    public void setVitaminB(String vitaminB) {
        this.vitaminB = vitaminB;
    }

    public String getVitaminB2() {
        return vitaminB2;
    }

    public void setVitaminB2(String vitaminB2) {
        this.vitaminB2 = vitaminB2;
    }

    public String getVitaminB3() {
        return vitaminB3;
    }

    public void setVitaminB3(String vitaminB3) {
        this.vitaminB3 = vitaminB3;
    }

    public String getVitaminB5() {
        return vitaminB5;
    }

    public void setVitaminB5(String vitaminB5) {
        this.vitaminB5 = vitaminB5;
    }

    public String getVitaminB6() {
        return vitaminB6;
    }

    public void setVitaminB6(String vitaminB6) {
        this.vitaminB6 = vitaminB6;
    }

    public String getVitaminB7() {
        return vitaminB7;
    }

    public void setVitaminB7(String vitaminB7) {
        this.vitaminB7 = vitaminB7;
    }

    public String getVitaminB9() {
        return vitaminB9;
    }

    public void setVitaminB9(String vitaminB9) {
        this.vitaminB9 = vitaminB9;
    }

    public String getVitaminB12() {
        return vitaminB12;
    }

    public void setVitaminB12(String vitaminB12) {
        this.vitaminB12 = vitaminB12;
    }

    public String getVitaminC() {
        return vitaminC;
    }

    public void setVitaminC(String vitaminC) {
        this.vitaminC = vitaminC;
    }

    public String getVitaminD() {
        return vitaminD;
    }

    public void setVitaminD(String vitaminD) {
        this.vitaminD = vitaminD;
    }

    public String getVitaminE() {
        return vitaminE;
    }

    public void setVitaminE(String vitaminE) {
        this.vitaminE = vitaminE;
    }

    public String getVitaminK() {
        return vitaminK;
    }

    public void setVitaminK(String vitaminK) {
        this.vitaminK = vitaminK;
    }

    public String getCaffeine() {
        return caffeine;
    }

    public void setCaffeine(String caffeine) {
        this.caffeine = caffeine;
    }

    //==============================================================================================
    public static final DiffUtil.ItemCallback<FoodNutrientsInsight> DIFF_CALLBACK = new DiffUtil.ItemCallback<FoodNutrientsInsight>() {
        @Override
        public boolean areItemsTheSame(@NonNull FoodNutrientsInsight oldItem, @NonNull FoodNutrientsInsight newItem) {
            return oldItem.foodName.equals(newItem.foodName);
        }

        @Override
        public boolean areContentsTheSame(@NonNull FoodNutrientsInsight oldItem, @NonNull FoodNutrientsInsight newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodNutrientsInsight that = (FoodNutrientsInsight) o;
        return foodId == that.foodId &&
                foodPortion == that.foodPortion &&
                energyKCal == that.energyKCal &&
                foodName.equals(that.foodName) &&
                foodPortionType.equals(that.foodPortionType) &&
                Objects.equals(foodThumbnail, that.foodThumbnail) &&
                Objects.equals(foodFullPic, that.foodFullPic) &&
                Objects.equals(protein, that.protein) &&
                Objects.equals(totalLipidFat, that.totalLipidFat) &&
                Objects.equals(carbohydrate, that.carbohydrate) &&
                Objects.equals(fiberTotalDietary, that.fiberTotalDietary) &&
                Objects.equals(sugarsTotalIncludingNLEA, that.sugarsTotalIncludingNLEA) &&
                Objects.equals(calciumCa, that.calciumCa) &&
                Objects.equals(ironFe, that.ironFe) &&
                Objects.equals(sodiumNa, that.sodiumNa) &&
                Objects.equals(potassiumK, that.potassiumK) &&
                Objects.equals(iodineI, that.iodineI) &&
                Objects.equals(zincZn, that.zincZn) &&
                Objects.equals(magnesiumMg, that.magnesiumMg) &&
                Objects.equals(cholesterolMg, that.cholesterolMg) &&
                Objects.equals(fattyAcidSaturated, that.fattyAcidSaturated) &&
                Objects.equals(fattyAcidMonounsaturated, that.fattyAcidMonounsaturated) &&
                Objects.equals(fattyAcidPolyunsaturated, that.fattyAcidPolyunsaturated) &&
                Objects.equals(fattyAcidTrans, that.fattyAcidTrans) &&
                Objects.equals(vitaminA, that.vitaminA) &&
                Objects.equals(vitaminB, that.vitaminB) &&
                Objects.equals(vitaminB2, that.vitaminB2) &&
                Objects.equals(vitaminB3, that.vitaminB3) &&
                Objects.equals(vitaminB5, that.vitaminB5) &&
                Objects.equals(vitaminB6, that.vitaminB6) &&
                Objects.equals(vitaminB7, that.vitaminB7) &&
                Objects.equals(vitaminB9, that.vitaminB9) &&
                Objects.equals(vitaminB12, that.vitaminB12) &&
                Objects.equals(vitaminC, that.vitaminC) &&
                Objects.equals(vitaminD, that.vitaminD) &&
                Objects.equals(vitaminE, that.vitaminE) &&
                Objects.equals(vitaminK, that.vitaminK) &&
                Objects.equals(caffeine, that.caffeine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, foodName, foodPortionType, foodPortion, foodThumbnail, foodFullPic, energyKCal, protein, totalLipidFat, carbohydrate, fiberTotalDietary, sugarsTotalIncludingNLEA, calciumCa, ironFe, sodiumNa, potassiumK, iodineI, zincZn, magnesiumMg, cholesterolMg, fattyAcidSaturated, fattyAcidMonounsaturated, fattyAcidPolyunsaturated, fattyAcidTrans, vitaminA, vitaminB, vitaminB2, vitaminB3, vitaminB5, vitaminB6, vitaminB7, vitaminB9, vitaminB12, vitaminC, vitaminD, vitaminE, vitaminK, caffeine);
    }
}
