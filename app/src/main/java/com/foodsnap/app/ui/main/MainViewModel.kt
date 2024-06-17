package com.foodsnap.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.foodsnap.app.data.Repository
import com.foodsnap.app.data.Result
import com.foodsnap.app.data.model.Food
import com.foodsnap.app.data.model.User
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val result = MutableLiveData<Result<String>>()
    fun getDataFromApi(): LiveData<Result<String>> {
        result.value = Result.Loading
        viewModelScope.launch {
            result.value = repository.getDataFromApi()
        }
        return result
    }

    fun getUser(): LiveData<User> = repository.getUser().asLiveData()

    fun getFood(): LiveData<List<Food>> = repository.getFood()


    fun logout() = viewModelScope.launch {
        repository.logout()
    }
}