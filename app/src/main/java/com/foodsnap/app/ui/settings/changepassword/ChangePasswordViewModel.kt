package com.foodsnap.app.ui.settings.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsnap.app.data.Repository
import com.foodsnap.app.data.Result
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val repository: Repository) : ViewModel() {
    private val result = MutableLiveData<Result<String>>()
    fun changePassword(oldPassword: String, newPassword: String): LiveData<Result<String>> {
        result.value = Result.Loading
        viewModelScope.launch {
            result.value = repository.changePassword(oldPassword, newPassword)
        }
        return result
    }
}