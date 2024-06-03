package com.foodsnap.app.data

import android.util.Log
import com.foodsnap.app.data.api.ApiService
import com.foodsnap.app.data.model.Session
import com.foodsnap.app.data.model.User
import com.foodsnap.app.data.model.request.AuthRequest
import com.foodsnap.app.data.model.request.EditProfileRequest
import com.foodsnap.app.data.model.response.EditProfileResponse
import com.foodsnap.app.data.model.response.LoginResponse
import com.foodsnap.app.data.model.response.SignUpResponse
import com.foodsnap.app.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.net.SocketTimeoutException

class Repository(private val apiService: ApiService, private val pref: UserPreference) {

    suspend fun signup(request: AuthRequest): Result<String> {
        var result: Result<String>
        try {
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

    suspend fun login(request: AuthRequest): Result<String> {
        var result: Result<String>
        try {
            val response = apiService.login(request)
            Log.d(TAG, "login: $response")
            Log.d(TAG, "login: ${response.message}")
            pref.saveSession(Session(response.token, response.userId, true))
            result = Result.Success(response.message)
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

    suspend fun editProfile(request: EditProfileRequest) : Result<String> {
        var result: Result<String>
        try {
            val response = apiService.editProfile(request)
            Log.d(TAG, "editProfile: $response")
            Log.d(TAG, "editProfile: ${response.message}")
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

    suspend fun getProfile(): Result<User> {
        val result: Result<User>
        try {
            val response = apiService.getProfile()
            Log.d(TAG, "editProfile: $response")
            pref.saveUser(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, EditProfileResponse::class.java)
            Log.d(TAG, "editProfile error: $errorResponse")
        } catch (e: SocketTimeoutException) {
            Log.d(TAG, "editProfile timeout: ${e.message}")
        } finally {
            val user = pref.getUser().first()
            result = Result.Success(user)
        }
        return result
    }

    companion object {
        private const val TAG = "Repository"
    }
}