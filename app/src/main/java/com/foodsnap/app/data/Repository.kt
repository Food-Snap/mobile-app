package com.foodsnap.app.data

import android.util.Log
import com.foodsnap.app.data.api.ApiService
import com.foodsnap.app.data.model.Session
import com.foodsnap.app.data.model.User
import com.foodsnap.app.data.model.request.EditProfileRequest
import com.foodsnap.app.data.model.request.LoginRequest
import com.foodsnap.app.data.model.request.SignUpRequest
import com.foodsnap.app.data.model.response.EditProfileResponse
import com.foodsnap.app.data.model.response.LoginResponse
import com.foodsnap.app.data.model.response.SignUpResponse
import com.foodsnap.app.data.pref.UserPreference
import com.foodsnap.app.ui.ViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.net.SocketTimeoutException

class Repository(private val apiService: ApiService, private val pref: UserPreference) {

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
            val errorResponse = Gson().fromJson(errorBody, SignUpResponse::class.java)
            result = Result.Error(errorResponse.message)
            Log.d(TAG, "signup error: $errorResponse")
            Log.d(TAG, "signup error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "signup timeout: ${e.message}")
            result = Result.Error("Socket Timeout")
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
            result = Result.Success(response.message)
            ViewModelFactory.deleteInstance()
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            result = Result.Error(errorResponse.message)
            Log.d(TAG, "login error: $errorResponse")
            Log.d(TAG, "login error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "login timeout: ${e.message}")
            result = Result.Error("Socket Timeout")
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
    ): Result<String> {
        var result: Result<String>
        try {
            val request = EditProfileRequest(name, email, age, weight, height, gender)
            val response = apiService.editProfile(request)
            Log.d(TAG, "editProfile: $response")
            Log.d(TAG, "editProfile: ${response.message}")
            val user = User(name, email, age, weight, height, gender, response.bmi)
            pref.saveUser(user)
            result = Result.Success(response.message)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, EditProfileResponse::class.java)
            result = Result.Error(errorResponse.message)
            Log.d(TAG, "editProfile error: $errorResponse")
            Log.d(TAG, "editProfile error: ${errorResponse.message}")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "editProfile timeout: ${e.message}")
            result = Result.Error("Socket Timeout")
        }
        return result
    }

    suspend fun getProfile(){
        try {
            val response = apiService.getProfile()
            Log.d(TAG, "getProfile: $response")
            pref.saveUser(response.user)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, EditProfileResponse::class.java)
            Log.d(TAG, "getProfile error: $errorResponse")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "getProfile timeout: ${e.message}")
        }
    }

    fun getUser(): Flow<User> {
        return pref.getUser()
    }

    fun getSession(): Flow<Session> {
        return pref.getSession()
    }

    suspend fun logout() {
        pref.logout()
        ViewModelFactory.deleteInstance()
    }

    companion object {
        private const val TAG = "Repository"
    }
}