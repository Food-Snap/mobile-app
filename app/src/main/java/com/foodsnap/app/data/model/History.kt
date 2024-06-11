package com.foodsnap.app.data.model

import com.google.gson.annotations.SerializedName

data class History (
    @field:SerializedName("Date")
    val date: String,
    @field:SerializedName("predictedFood")
    val predictedFood: PredictedFood,
    @field:SerializedName("imageUrl")
    val imageUrl: String
) {
    fun toFood(): Food {
        return Food(
            predictedFood.foodId,
            predictedFood.name,
            imageUrl,
            date,
            predictedFood.calories,
            predictedFood.carbs,
            predictedFood.protein,
            predictedFood.fat
        )
    }
}