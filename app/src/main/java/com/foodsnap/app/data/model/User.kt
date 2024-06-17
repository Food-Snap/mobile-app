package com.foodsnap.app.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("age")
    val age: Int,

    @field:SerializedName("weight")
    val weight: Float,

    @field:SerializedName("height")
    val height: Float,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("bmi")
    val bmi: Float
) {
    fun calculateDailyCaloricNeeds(): Float {
        val bmr = when (gender) {
            "Male" -> 88.362f + (13.397f * weight) + (4.799f * height) - (5.677f * age)
            "Female" -> 447.593f + (9.247f * weight) + (3.098f * height) - (4.330f * age)
            else -> throw IllegalArgumentException("Unknown gender: $gender")
        }
        return bmr
    }
}
