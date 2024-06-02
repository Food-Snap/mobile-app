package com.foodsnap.app.model

data class User(
    val token: String,
    val userId: String,
    val name: String,
    val email: String,
    val weight: Float? = null,
    val height: Float? = null,
    val gender: String? = null,
    val isLogin: Boolean = false
)
