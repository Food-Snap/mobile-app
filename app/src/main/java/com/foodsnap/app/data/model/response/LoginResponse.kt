package com.foodsnap.app.data.model.response

data class LoginResponse(
    val message: String,
    val token: String,
    val userId: String,
)
