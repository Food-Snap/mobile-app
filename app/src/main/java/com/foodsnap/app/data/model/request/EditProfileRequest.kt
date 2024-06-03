package com.foodsnap.app.data.model.request

data class EditProfileRequest(
    val name: String,
    val email: String,
    val age: Int,
    val weight: Float,
    val height: Float,
    val gender: String
)
