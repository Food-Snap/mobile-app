package com.foodsnap.app.data.api

import com.foodsnap.app.data.model.request.EditProfileRequest
import com.foodsnap.app.data.model.request.LoginRequest
import com.foodsnap.app.data.model.request.SignUpRequest
import com.foodsnap.app.data.model.response.EditProfileResponse
import com.foodsnap.app.data.model.response.LoginResponse
import com.foodsnap.app.data.model.response.ProfileResponse
import com.foodsnap.app.data.model.response.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("signup")
    suspend fun signup(
        @Body request: SignUpRequest
    ): SignUpResponse

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
}