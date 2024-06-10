package com.foodsnap.app.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.foodsnap.app.data.Repository
import com.foodsnap.app.data.model.Session

class SplashScreenViewModel(private val repository: Repository): ViewModel() {
    fun getSession(): LiveData<Session> = repository.getSession().asLiveData()
}