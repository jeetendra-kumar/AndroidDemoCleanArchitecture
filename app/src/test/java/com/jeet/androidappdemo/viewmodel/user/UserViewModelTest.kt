package com.jeet.androidappdemo.viewmodel.user

import com.jeet.androidappdemo.common.Resource
import com.jeet.androidappdemo.domain.model.User
import com.jeet.androidappdemo.domain.usecase.GetUserUseCase
import com.jeet.androidappdemo.presentation.users.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getUserUseCase: GetUserUseCase

    private val fakeUsers = listOf(
        User(
            id = 1,
            name = "Alice",
            company = "Acme",
            username = "alice",
            email = "alice@example.com",
            address = "123 Main St",
            zip = "10001",
            state = "NY",
            country = "USA",
            phone = "555-0100",
            photo = "https://example.com/alice.jpg"
        ),
        User(
            id = 2,
            name = "Bob",
            company = "Globex",
            username = "bob",
            email = "bob@example.com",
            address = "456 Elm St",
            zip = "90210",
            state = "CA",
            country = "USA",
            phone = "555-0200",
            photo = "https://example.com/bob.jpg"
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getUserUseCase = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): UserViewModel {
        whenever(getUserUseCase()).thenReturn(flow {
            emit(Resource.Loading)
            emit(Resource.Success(fakeUsers))
        })
        return UserViewModel(getUserUseCase)
    }

    @Test
    fun `initial uiState has default values`() = runTest {
        whenever(getUserUseCase()).thenReturn(flow { })
        val vm = UserViewModel(getUserUseCase)

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.users.isEmpty())
        assertTrue(state.error.isEmpty())
    }

    // ── Loading state ──────────────────────────────────────────────────────────

    @Test
    fun `uiState shows isLoading true while resource is loading`() = runTest {
        whenever(getUserUseCase()).thenReturn(flow {
            emit(Resource.Loading)
        })
        val vm = UserViewModel(getUserUseCase)
        advanceUntilIdle()

        assertTrue(vm.uiState.value.isLoading)
        assertTrue(vm.uiState.value.error.isEmpty())
    }

    // ── Success state ──────────────────────────────────────────────────────────

    @Test
    fun `uiState contains users list on success`() = runTest {
        val vm = createViewModel()
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertEquals(fakeUsers, state.users)
        assertTrue(state.error.isEmpty())
    }

    @Test
    fun `uiState users count matches fake data`() = runTest {
        val vm = createViewModel()
        advanceUntilIdle()

        assertEquals(2, vm.uiState.value.users.size)
    }

    @Test
    fun `uiState users contain correct ids`() = runTest {
        val vm = createViewModel()
        advanceUntilIdle()

        val ids = vm.uiState.value.users.map { it.id }
        assertEquals(listOf(1, 2), ids)
    }

    // ── Error state ────────────────────────────────────────────────────────────

    @Test
    fun `uiState shows error message on failure`() = runTest {
        val errorMsg = "Something went wrong"
        whenever(getUserUseCase()).thenReturn(flow {
            emit(Resource.Loading)
            emit(Resource.Error(errorMsg))
        })
        val vm = UserViewModel(getUserUseCase)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.users.isEmpty())
        assertNotNull(state.error)
        assertEquals(errorMsg, state.error)
    }

    @Test
    fun `loadUsers calls use case on init`() = runTest {
        val vm = createViewModel()
        advanceUntilIdle()

        // Once from init block
        verify(getUserUseCase, times(1)).invoke()
    }

    @Test
    fun `loadUsers can be called multiple times`() = runTest {
        whenever(getUserUseCase()).thenReturn(flow {
            emit(Resource.Loading)
            emit(Resource.Success(fakeUsers))
        })
        val vm = UserViewModel(getUserUseCase)
        advanceUntilIdle()

        vm.getUsers()
        advanceUntilIdle()

        // Once from init + once from explicit call
        verify(getUserUseCase, times(2)).invoke()
    }
}