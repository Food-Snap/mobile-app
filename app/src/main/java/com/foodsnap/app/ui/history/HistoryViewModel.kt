package com.foodsnap.app.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.foodsnap.app.utils.FakeData
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    val searchedFood = MutableLiveData(FakeData.generateFood())

    fun searchFood(query: String) {
        viewModelScope.launch {
            searchedFood.postValue(
                FakeData.searchFood(query)
            )
        }
    }

    class ViewModelInjector :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            HistoryViewModel() as T
    }
}