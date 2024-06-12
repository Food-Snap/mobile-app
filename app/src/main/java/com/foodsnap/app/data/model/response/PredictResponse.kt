package com.foodsnap.app.data.model.response

import android.os.Parcelable
import com.foodsnap.app.data.model.Food
import com.foodsnap.app.data.model.PredictedFood
import com.foodsnap.app.utils.toTitleCase
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictResponse(
    @field:SerializedName("predictedFood")
    val predictedFood: PredictedFood,
    @field:SerializedName("imageUrl")
    val imageUrl: String
) : Parcelable {
    fun toFood(): Food {
        return Food(
            "",
            predictedFood.name.toTitleCase(),
            imageUrl,
            predictedFood.calories,
            predictedFood.carbs,
            predictedFood.protein,
            predictedFood.fat
        )
    }
}