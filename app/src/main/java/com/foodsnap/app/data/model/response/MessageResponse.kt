package com.foodsnap.app.data.model.response

import com.google.gson.annotations.SerializedName

class MessageResponse (
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("error")
    val error: String?
)
