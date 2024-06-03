package com.foodsnap.app.data.model

data class Session (
    val token: String,
    val userId: String,
    val isLogin: Boolean = false
)