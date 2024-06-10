package com.foodsnap.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.foodsnap.app.data.Repository
import com.foodsnap.app.data.model.User
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    fun getProfile() = viewModelScope.launch {
        repository.getProfile()
    }

    fun getUser(): LiveData<User> = repository.getUser().asLiveData()

    fun logout() = viewModelScope.launch {
        repository.logout()
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}