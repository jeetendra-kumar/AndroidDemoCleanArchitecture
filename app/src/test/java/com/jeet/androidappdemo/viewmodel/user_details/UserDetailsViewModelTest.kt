package com.jeet.androidappdemo.viewmodel.user_details

import androidx.lifecycle.SavedStateHandle
import com.jeet.androidappdemo.presentation.userdetails.UserDetailViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class UserDetailsViewModelTest {
    private fun savedStateHandle(vararg pairs: Pair<String, Any?>) =
        SavedStateHandle(pairs.toMap())

    @Test
    fun `userId is correctly read from SavedStateHandle`() {
        val vm = UserDetailViewModel(savedStateHandle("userId" to 42))

        assertEquals(42, vm.userId)
    }

    @Test
    fun `userId works for id value 1`() {
        val vm = UserDetailViewModel(savedStateHandle("userId" to 1))

        assertEquals(1, vm.userId)
    }

    @Test
    fun `userId works for large id values`() {
        val vm = UserDetailViewModel(savedStateHandle("userId" to Int.MAX_VALUE))

        assertEquals(Int.MAX_VALUE, vm.userId)
    }

    @Test
    fun `userId works for zero`() {
        val vm = UserDetailViewModel(savedStateHandle("userId" to 0))

        assertEquals(0, vm.userId)
    }

    // ── Missing key guard ──────────────────────────────────────────────────────

    @Test
    fun `throws IllegalStateException when userId key is absent`() {
        assertThrows(IllegalStateException::class.java) {
            UserDetailViewModel(SavedStateHandle())
        }
    }

    @Test
    fun `throws IllegalStateException when userId is null`() {
        assertThrows(IllegalStateException::class.java) {
            UserDetailViewModel(savedStateHandle("userId" to null))
        }
    }
}