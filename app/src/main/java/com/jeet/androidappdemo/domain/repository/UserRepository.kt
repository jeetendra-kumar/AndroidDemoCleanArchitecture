package com.jeet.androidappdemo.domain.repository

import com.jeet.androidappdemo.common.Resource
import com.jeet.androidappdemo.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers() : Flow<Resource<List<User>>>
}