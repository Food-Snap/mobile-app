package com.foodsnap.app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PredictedFood(
    @field:SerializedName("foodId")
    val foodId: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("calories")
    val calories: Float,
    @field:SerializedName("carbs")
    val carbs: Float,
    @field:SerializedName("protein")
    val protein: Float,
    @field:SerializedName("fat")
    val fat: Float
):Parcelable