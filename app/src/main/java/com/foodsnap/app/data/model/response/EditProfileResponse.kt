package com.foodsnap.app.data.model.response

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("bmi")
    val bmi: Float
)
