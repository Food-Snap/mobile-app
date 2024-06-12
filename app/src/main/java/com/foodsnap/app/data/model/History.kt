package com.foodsnap.app.data.model

import com.foodsnap.app.utils.toTitleCase
import com.google.gson.annotations.SerializedName

data class History (
    @field:SerializedName("date")
    val date: String,
    @field:SerializedName("predictedFood")
    val predictedFood: PredictedFood,
    @field:SerializedName("imageUrl")
    val imageUrl: String
) {
    fun toFood(): Food {
        return Food(
            date = date,
            name = predictedFood.name.toTitleCase(),
            imageUrl = imageUrl,
            calories = predictedFood.calories,
            carbs = predictedFood.carbs,
            protein = predictedFood.protein,
            fats = predictedFood.fat
        )
    }
}