package com.foodsnap.app.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.foodsnap.app.data.model.Food

@Dao
interface FoodDao {
    @Query("SELECT * FROM food ORDER BY date DESC")
    fun getAllFood(): LiveData<List<Food>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFood(food: List<Food>)

    @Query("DELETE FROM food")
    suspend fun deleteAllFoods()
}