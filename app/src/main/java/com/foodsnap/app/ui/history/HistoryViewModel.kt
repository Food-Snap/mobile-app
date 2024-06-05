package com.foodsnap.app.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsnap.app.data.Repository
import com.foodsnap.app.utils.FakeData
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: Repository) : ViewModel() {
    val searchedFood = MutableLiveData(FakeData.generateFood())

    fun searchFood(query: String) {
        viewModelScope.launch {
            searchedFood.postValue(
                FakeData.searchFood(query)
            )
        }
    }
}