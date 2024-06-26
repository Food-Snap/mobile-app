package com.foodsnap.app.data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.foodsnap.app.data.api.ApiConfig
import com.foodsnap.app.data.api.ApiService
import com.foodsnap.app.data.local.FoodDao
import com.foodsnap.app.data.model.Food
import com.foodsnap.app.data.model.Session
import com.foodsnap.app.data.model.User
import com.foodsnap.app.data.model.request.ChangePasswordRequest
import com.foodsnap.app.data.model.request.EditProfileRequest
import com.foodsnap.app.data.model.request.LoginRequest
import com.foodsnap.app.data.model.request.SaveRequest
import com.foodsnap.app.data.model.request.SignUpRequest
import com.foodsnap.app.data.model.response.EditProfileResponse
import com.foodsnap.app.data.model.response.MessageResponse
import com.foodsnap.app.data.model.response.PredictResponse
import com.foodsnap.app.data.pref.UserPreference
import com.foodsnap.app.utils.reduceFileImage
import com.foodsnap.app.utils.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class Repository(
    private var apiService: ApiService,
    private val foodDao: FoodDao,
    private val pref: UserPreference
) {

    suspend fun signup(email: String, password: String, name: String): Result<String> {
        var result: Result<String>
        try {
            val request = SignUpRequest(email, password, name)
            val response = apiService.signup(request)
            Log.d(TAG, "signup: $response")
            Log.d(TAG, "signup: ${response.message}")
            result = Result.Success(response.message)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            result = Result.Error(errorResponse.error ?: errorResponse.message)
            Log.d(TAG, "signup error: $errorResponse")
            Log.d(TAG, "signup error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "signup timeout: ${e.message}")
            result = Result.Error("Socket Timeout")
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.message}")
            result = Result.Error("Something went wrong")
        }
        return result
    }

    suspend fun login(email: String, password: String): Result<String> {
        var result: Result<String>
        try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)
            Log.d(TAG, "login: $response")
            Log.d(TAG, "login: ${response.message}")
            pref.saveSession(Session(response.token, true))
            apiService = ApiConfig.getApiService(response.token)
            getDataFromApi()
            result = Result.Success(response.message)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            result = Result.Error(errorResponse.error ?: errorResponse.message)
            Log.d(TAG, "login error: $errorResponse")
            Log.d(TAG, "login error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "login timeout: ${e.message}")
            result = Result.Error("Socket Timeout")
        } catch (e: ConnectException) {
            Log.d(TAG, "connection exception: ${e.message}")
            result = Result.Error("Please check your network connection.")
            e.printStackTrace()
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.message}")
            result = Result.Error("Something went wrong")
        }
        return result
    }

    suspend fun editProfile(
        name: String,
        email: String,
        weight: Float,
        height: Float,
        gender: String,
        age: Int
    ): Result<EditProfileResponse> {
        var result: Result<EditProfileResponse>
        try {
            val request = EditProfileRequest(name, email, age, weight, height, gender)
            val response = apiService.editProfile(request)
            Log.d(TAG, "editProfile: $response")
            Log.d(TAG, "editProfile: ${response.message}")
            val user = User(name, email, age, weight, height, gender, response.bmi)
            pref.saveUser(user)
            result = Result.Success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            result = Result.Error(errorResponse.error ?: errorResponse.message)
            Log.d(TAG, "editProfile error: $errorResponse")
            Log.d(TAG, "editProfile error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "editProfile timeout: ${e.message}")
            result = Result.Error("Socket Timeout")
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.message}")
            result = Result.Error("Something went wrong")
        }
        return result
    }

    suspend fun getDataFromApi(): Result<String> {
        var result: Result<String>
        try {
            val profileResponse = apiService.getProfile()
            val historyResponse = apiService.getHistory()
            Log.d(TAG, "getDataFromApi: $profileResponse")
            Log.d(TAG, "getDataFromApi: $historyResponse")
            val listHistory = historyResponse.historyData.map { it.toFood() }
            pref.saveUser(profileResponse.user)
            foodDao.insertFood(listHistory)
            result = Result.Success("Success")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            Log.d(TAG, "getDataFromApi error: $errorResponse")
            result = Result.Error(errorResponse.error ?: errorResponse.message)
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "getDataFromApi timeout: ${e.message}")
            result = Result.Error("Socket Timeout")
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.message}")
            result = Result.Error("Something went wrong")
        }
        return result
    }

    suspend fun changePassword(oldPassword: String, newPassword: String): Result<String> {
        var result: Result<String>
        try {
            val request = ChangePasswordRequest(oldPassword, newPassword)
            val response = apiService.changePassword(request)
            Log.d(TAG, "changePassword: $response")
            result = Result.Success(response.message)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            Log.d(TAG, "changePassword error: $errorResponse")
            result = Result.Error(errorResponse.error ?: errorResponse.message)
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "changePassword timeout: ${e.message}")
            result = Result.Error("Socket Timeout")
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.message}")
            result = Result.Error("Something went wrong")
        }
        return result
    }

    suspend fun predictFood(context: Context, uri: Uri): Result<PredictResponse> {
        val imageFile = uriToFile(uri, context).reduceFileImage()
        Log.d("Image File", "showImage: ${imageFile.path}")
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )
        var result: Result<PredictResponse>
        try {
            val response = apiService.predictFood(multipartBody)
            result = Result.Success(response)
            Log.d(TAG, "uploadImage success: $response")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            result = Result.Error(errorResponse.error ?: errorResponse.message)
            Log.d(TAG, "uploadImage error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "uploadImage timeout: ${e.message}")
            result = Result.Error("Socket Timeout")
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.message}")
            result = Result.Error("Something went wrong")
        }
        return result
    }

    suspend fun saveFood(foodId: String, imageUrl: String): Result<String> {
        var result: Result<String>
        try {
            val request = SaveRequest(foodId, imageUrl)
            val response = apiService.saveFood(request)
            Log.d(TAG, "saveFood: $response")
            result = Result.Success(response.message)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
            Log.d(TAG, "saveFood error: $errorResponse")
            result = Result.Error(errorResponse.error ?: errorResponse.message)
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "saveFood timeout: ${e.message}")
            result = Result.Error("Socket Timeout")
        } catch (e: Exception) {
            Log.d(TAG, "exception: ${e.message}")
            result = Result.Error("Something went wrong")
        }
        return result
    }

    fun getFood(): LiveData<List<Food>> {
        return foodDao.getAllFood()
    }

    fun getUser(): Flow<User> {
        return pref.getUser()
    }

    fun getSession(): Flow<Session> {
        return pref.getSession()
    }

    suspend fun logout() {
        pref.logout()
        foodDao.deleteAllFoods()
        apiService = ApiConfig.getApiService("")
    }

    companion object {
        private const val TAG = "Repository"
    }
}