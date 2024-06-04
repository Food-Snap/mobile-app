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
)
