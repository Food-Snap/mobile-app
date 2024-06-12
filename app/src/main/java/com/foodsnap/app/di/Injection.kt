package com.foodsnap.app.di

import android.content.Context
import com.foodsnap.app.data.Repository
import com.foodsnap.app.data.api.ApiConfig
import com.foodsnap.app.data.local.FoodDatabase
import com.foodsnap.app.data.pref.UserPreference
import com.foodsnap.app.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val foodDao = FoodDatabase.getInstance(context).foodDao()
        return Repository(apiService, foodDao, pref)
    }
}