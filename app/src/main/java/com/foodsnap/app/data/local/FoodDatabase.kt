package com.foodsnap.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.foodsnap.app.data.model.Food

@Database(entities = [Food::class], version = 1, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    companion object {
        @Volatile
        private var instance: FoodDatabase? = null
        fun getInstance(context: Context): FoodDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FoodDatabase::class.java, "FoodDatabase.db"
                ).build()
            }
    }
}