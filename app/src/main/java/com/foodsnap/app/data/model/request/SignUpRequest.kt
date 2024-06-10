package com.foodsnap.app.data.model.request

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String
)