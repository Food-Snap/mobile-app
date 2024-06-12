package com.foodsnap.app.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsnap.app.data.Repository
import com.foodsnap.app.data.Result
import kotlinx.coroutines.launch

class FoodDetailViewModel(private val repository: Repository) : ViewModel() {
    fun saveFood(foodId: String, imageUrl: String): LiveData<Result<String>> {
        val result = MutableLiveData<Result<String>>()
        result.value = Result.Loading
        viewModelScope.launch {
            result.value = repository.saveFood(foodId, imageUrl)
        }
        return result
    }
}