package com.foodsnap.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    val id: String,
    val name: String,
    val imageUrl: String,
    val date: String,
    val calories: Float,
    val carbs: Float,
    val protein: Float,
    val fats: Float
) : Parcelable
