package com.jeet.androidappdemo.data.repository

import com.jeet.androidappdemo.common.Resource
import com.jeet.androidappdemo.data.remote.api.UserApiService
import com.jeet.androidappdemo.data.remote.dto.toUser
import com.jeet.androidappdemo.domain.model.User
import com.jeet.androidappdemo.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApiService
) : UserRepository {

    override fun getUsers(): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading)
        try {
            val users = apiService.getUsers().map { it.toUser() }
            emit(Resource.Success(data = users))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach to service, Check your internet connection."))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Something went wrong, Try again later."))
        }

    }
}