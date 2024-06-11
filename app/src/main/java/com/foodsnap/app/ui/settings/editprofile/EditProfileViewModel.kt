package com.foodsnap.app.ui.settings.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.foodsnap.app.data.Repository
import com.foodsnap.app.data.Result
import com.foodsnap.app.data.model.response.EditProfileResponse
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repository: Repository) : ViewModel() {
    private val result = MutableLiveData<Result<EditProfileResponse>>()
    fun getUser() = repository.getUser().asLiveData()

    fun editProfile(
        name: String,
        email: String,
        weight: Float,
        height: Float,
        gender: String,
        age: Int
    ): LiveData<Result<EditProfileResponse>> {
        result.value = Result.Loading
        viewModelScope.launch {
            result.value = repository.editProfile(name, email, weight, height, gender, age)
        }
        return result
    }
}