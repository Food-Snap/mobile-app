package com.foodsnap.app.data.api

import com.foodsnap.app.data.model.request.ChangePasswordRequest
import com.foodsnap.app.data.model.request.EditProfileRequest
import com.foodsnap.app.data.model.request.LoginRequest
import com.foodsnap.app.data.model.request.SaveRequest
import com.foodsnap.app.data.model.request.SignUpRequest
import com.foodsnap.app.data.model.response.EditProfileResponse
import com.foodsnap.app.data.model.response.HistoryResponse
import com.foodsnap.app.data.model.response.LoginResponse
import com.foodsnap.app.data.model.response.MessageResponse
import com.foodsnap.app.data.model.response.PredictResponse
import com.foodsnap.app.data.model.response.ProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {
    @POST("signup")
    suspend fun signup(
        @Body request: SignUpRequest
    ): MessageResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @PUT("edit-profile")
    suspend fun editProfile(
        @Body request: EditProfileRequest
    ): EditProfileResponse

    @GET("profile")
    suspend fun getProfile(): ProfileResponse

    @PUT("change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): MessageResponse

    @Multipart
    @POST("predict")
    suspend fun predictFood(
        @Part
        image: MultipartBody.Part,
    ): PredictResponse

    @POST("save")
    suspend fun saveFood(
        @Body request: SaveRequest
    ): MessageResponse

    @GET("history")
    suspend fun getHistory(): HistoryResponse
}