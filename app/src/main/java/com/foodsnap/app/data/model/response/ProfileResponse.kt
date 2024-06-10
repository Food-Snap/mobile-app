package com.foodsnap.app.data.model.response

import com.foodsnap.app.data.model.User
import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @field:SerializedName("user")
    val user: User
)