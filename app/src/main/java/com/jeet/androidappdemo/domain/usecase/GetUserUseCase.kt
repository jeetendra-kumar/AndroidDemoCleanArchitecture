package com.jeet.androidappdemo.domain.usecase

import com.jeet.androidappdemo.common.Resource
import com.jeet.androidappdemo.domain.model.User
import com.jeet.androidappdemo.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(): Flow<Resource<List<User>>> = userRepository.getUsers()
}