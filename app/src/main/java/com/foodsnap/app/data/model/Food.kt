package com.foodsnap.app.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity("food")
@Parcelize
data class Food(
    @PrimaryKey
    val date: String,
    val name: String,
    val imageUrl: String,
    val calories: Float,
    val carbs: Float,
    val protein: Float,
    val fats: Float
) : Parcelable
