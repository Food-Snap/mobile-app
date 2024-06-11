package com.foodsnap.app.ui.image

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsnap.app.data.Repository
import com.foodsnap.app.data.Result
import com.foodsnap.app.data.model.response.PredictResponse
import kotlinx.coroutines.launch

class ImageViewModel(private val repository: Repository) : ViewModel() {
    fun predictFood(context: Context, uri: Uri) : LiveData<Result<PredictResponse>> {
        val result = MutableLiveData<Result<PredictResponse>>()
        result.value = Result.Loading
        viewModelScope.launch {
            result.value = repository.predictFood(context, uri)
        }
        return result
    }
}