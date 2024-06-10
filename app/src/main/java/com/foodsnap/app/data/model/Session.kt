package com.foodsnap.app.data.model

data class Session (
    val token: String,
    val isLogin: Boolean = false
)