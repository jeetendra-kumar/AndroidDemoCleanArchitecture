package com.jeet.androidappdemo.presentation.users

import com.jeet.androidappdemo.domain.model.User

data class UserUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String = ""
)
