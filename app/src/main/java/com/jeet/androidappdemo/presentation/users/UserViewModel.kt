package com.jeet.androidappdemo.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeet.androidappdemo.common.Resource
import com.jeet.androidappdemo.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState

    init {
        getUsers()
    }

    fun getUsers() {
        getUserUseCase()
            .onEach { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = UserUiState(isLoading = true)
                    is Resource.Success -> _uiState.value = UserUiState(users = result.data)
                    is Resource.Error -> _uiState.value = UserUiState(error = result.message)
                }
            }.launchIn(viewModelScope)
    }
}