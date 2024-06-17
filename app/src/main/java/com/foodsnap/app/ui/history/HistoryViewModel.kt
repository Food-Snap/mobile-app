package com.foodsnap.app.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foodsnap.app.data.Repository
import com.foodsnap.app.data.model.Food
import com.foodsnap.app.utils.searchFood

class HistoryViewModel(repository: Repository) : ViewModel() {
    private val listFood = repository.getFood()
    private val food = MutableLiveData<List<Food>>()

    init {
        getFood()
    }

    fun searchFood(query: String) {
        food.value = listFood.value?.searchFood(query) ?: emptyList()
    }

    fun getFood(): LiveData<List<Food>> {
        listFood.observeForever {
            food.value = it ?: emptyList()
        }
        return food
    }
}