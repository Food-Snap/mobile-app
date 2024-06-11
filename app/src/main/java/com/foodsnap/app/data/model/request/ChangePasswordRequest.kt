package com.foodsnap.app.data.model.request

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)