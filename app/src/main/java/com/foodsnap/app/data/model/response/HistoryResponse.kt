package com.foodsnap.app.data.model.response

import com.foodsnap.app.data.model.History
import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @field:SerializedName("historyData")
    val historyData: List<History>
)