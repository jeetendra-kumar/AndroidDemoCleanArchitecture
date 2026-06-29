package com.jeet.androidappdemo.data.remote.api

import com.jeet.androidappdemo.data.remote.dto.UsersDto
import retrofit2.http.GET

interface UserApiService {
    @GET("users")
    suspend fun getUsers(): List<UsersDto>
}