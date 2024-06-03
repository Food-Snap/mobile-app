package com.foodsnap.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Food(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val date: String,
    val cal: Int,
    val carbs: Int,
    val protein: Int,
    val fats: Int,
    val calcium: Int,
) : Parcelable
