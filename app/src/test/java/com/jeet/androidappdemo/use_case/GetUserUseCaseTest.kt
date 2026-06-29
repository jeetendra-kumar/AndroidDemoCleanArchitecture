package com.jeet.androidappdemo.use_case

import com.jeet.androidappdemo.common.Resource
import com.jeet.androidappdemo.domain.model.User
import com.jeet.androidappdemo.domain.repository.UserRepository
import com.jeet.androidappdemo.domain.usecase.GetUserUseCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetUserUseCaseTest {

    private lateinit var userRepository: UserRepository

    private lateinit var userUseCase: GetUserUseCase

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
        userRepository = mock()
        userUseCase = GetUserUseCase(userRepository)
    }

    @Test
    fun `invoke emits Loading then Success when repository succeeds`() = runTest {
        whenever(userRepository.getUsers()).thenReturn(flow {
            emit(Resource.Loading)
            emit(Resource.Success(fakeUsers))
        })

        val emissions = userUseCase().toList()

        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        val success = emissions[1] as Resource.Success
        assertEquals(fakeUsers, success.data)
    }

    @Test
    fun `invoke emits Loading then Error when repository fails`() = runTest {
        val errorMessage = "Network error"
        whenever(userRepository.getUsers()).thenReturn(flow {
            emit(Resource.Loading)
            emit(Resource.Error(errorMessage))
        })

        val emissions = userUseCase().toList()

        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is Resource.Loading)
        val error = emissions[1] as Resource.Error
        assertEquals(errorMessage, error.message)
    }

    @Test
    fun `invoke emits Success with empty list when repository returns no users`() = runTest {
        whenever(userRepository.getUsers()).thenReturn(flow {
            emit(Resource.Loading)
            emit(Resource.Success(emptyList()))
        })

        val emissions = userUseCase().toList()

        assertEquals(2, emissions.size)
        val success = emissions[1] as Resource.Success
        assertTrue(success.data.isEmpty())
    }

    @Test
    fun `invoke delegates directly to repository`() = runTest {
        whenever(userRepository.getUsers()).thenReturn(flow {
            emit(Resource.Success(fakeUsers))
        })

        val result = userUseCase().toList()

        assertTrue(result.first() is Resource.Success)
        assertEquals(fakeUsers, (result.first() as Resource.Success).data)
    }
}