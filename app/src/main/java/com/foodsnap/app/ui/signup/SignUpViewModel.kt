package com.foodsnap.app.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsnap.app.data.Repository
import com.foodsnap.app.data.Result
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: Repository): ViewModel() {
    private val result = MutableLiveData<Result<String>>()

    fun signup(email: String, password: String, name: String): LiveData<Result<String>>{
        result.value = Result.Loading
        viewModelScope.launch {
            result.value = repository.signup(email, password, name)
        }
        return result
    }
}